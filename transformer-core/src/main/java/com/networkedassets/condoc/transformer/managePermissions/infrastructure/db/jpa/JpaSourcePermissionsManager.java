package com.networkedassets.condoc.transformer.managePermissions.infrastructure.db.jpa;

import com.networkedassets.condoc.transformer.common.PersistenceManager;
import com.networkedassets.condoc.transformer.common.SourceNodeIdentifier;
import com.networkedassets.condoc.transformer.common.exceptions.SourcePermissionsNodeDuplicateException;
import com.networkedassets.condoc.transformer.common.exceptions.UserGroupNotFoundException;
import com.networkedassets.condoc.transformer.common.exceptions.UserNotFoundException;
import com.networkedassets.condoc.transformer.managePermissions.core.boundary.SourcePermissionsManager;
import com.networkedassets.condoc.transformer.managePermissions.core.entity.Node;
import com.networkedassets.condoc.transformer.managePermissions.core.entity.SourceStructureNode;
import com.networkedassets.condoc.transformer.managePermissions.core.entity.SourceStructureRootNode;
import com.networkedassets.condoc.transformer.manageUsers.core.boundary.UserGroupManager;
import com.networkedassets.condoc.transformer.manageUsers.core.boundary.UserManager;
import com.networkedassets.condoc.transformer.manageUsers.core.entity.User;
import com.networkedassets.condoc.transformer.manageUsers.core.entity.UserGroup;
import com.networkedassets.condoc.transformer.util.functional.Optionals;

import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Singleton
public class JpaSourcePermissionsManager extends PersistenceManager<SourcePermissionsNode> implements SourcePermissionsManager {

    @Inject
    private UserManager userManager;

    @Inject
    private UserGroupManager userGroupManager;

    @Override
    public boolean groupHasAccessById(Integer groupId, SourceNodeIdentifier sourceNodeIdentifier) {
        return groupHasAccess(userGroupManager.getById(groupId).orElseThrow(UserGroupNotFoundException::new), sourceNodeIdentifier);
    }

    @Override
    public boolean groupHasAccess(UserGroup group, SourceNodeIdentifier sourceNodeIdentifier) {
        TypedQuery<Long> query = em.createQuery("" +
                "select count(spn.id) from SourcePermissionsNode spn join spn.groups ug " +
                "where spn.identifier.sourceIdentifier = :sourceIdentifier " +
                "and spn.identifier.unitIdentifier = :unitIdentifier " +
                "and ug.id = :userGroupId", Long.class)
                .setParameter("sourceIdentifier", sourceNodeIdentifier.getSourceIdentifier())
                .setParameter("unitIdentifier", sourceNodeIdentifier.getUnitIdentifier())
                .setParameter("userGroupId", group.getId());

        return query.getSingleResult() > 0;
    }

    public boolean anyGroupHasAccess(Collection<UserGroup> userGroups, SourceNodeIdentifier sourceNodeIdentifier) {
        return userGroups.stream()
                .filter(group -> groupHasAccess(group, sourceNodeIdentifier))
                .findAny()
                .isPresent();
    }

    @Override
    public boolean userHasAccess(User user, SourceNodeIdentifier sourceNodeIdentifier) {
        return anyGroupHasAccess(user.getGroups(), sourceNodeIdentifier);
    }

    @Override
    public boolean userHasAccessByUsername(String username, SourceNodeIdentifier sourceNodeIdentifier) {
        return userHasAccess(userManager.getByUsername(username).orElseThrow(UserNotFoundException::new), sourceNodeIdentifier);
    }

    @Override
    public SourceStructureRootNode filterStructureByUserGroupsPermission(SourceStructureRootNode structureRootNode, Collection<UserGroup> userGroups) {
        SourceNodeIdentifier sourceNodeIdentifier = structureRootNode.getSourceNodeIdentifier();

        return anyGroupHasAccess(userGroups, sourceNodeIdentifier) ?
                structureRootNode :
                copyAndFilterSubtreeByUserGroups(
                        structureRootNode,
                        userGroups,
                        new SourceStructureRootNode(structureRootNode.getSourceNodeIdentifier().getSourceIdentifier())
                );
    }

    /**
     * Recursively searches for children accessible by given user group
     *
     * @param parent     Node with children potentially accessible by given group
     * @param userGroups  Group with permissions to access part of the tree
     * @param parentCopy Copy of parent node used to insert children copies into
     * @return Copy of given parent with copied accessible part of the tree
     */
    private <T extends Node> T copyAndFilterSubtreeByUserGroups(Node parent, Collection<UserGroup> userGroups, T parentCopy) {
        for (Node n : parent.getChildren()) {
            if (anyGroupHasAccess(userGroups, n.getSourceNodeIdentifier())) {
                Node nodeCopy = new SourceStructureNode(n.getSourceNodeIdentifier());
                copyNodesAndAttachToParent(nodeCopy, n.getChildren());
                nodeCopy.setParent(parentCopy);
            } else {
                Node childWithPotentialPermissions = copyAndFilterSubtreeByUserGroups(n, userGroups, new SourceStructureNode(n.getSourceNodeIdentifier()));
                if (childWithPotentialPermissions.getChildren().size() > 0)
                    childWithPotentialPermissions.setParent(parentCopy);
            }
        }

        return parentCopy;
    }

    /**
     * Adds copies of all children, grandchildren etc. to given parent
     */
    private void copyNodesAndAttachToParent(Node parent, List<Node> nodes) {
        for (Node n : nodes) {
            Node childCopy = new SourceStructureNode(n.getSourceNodeIdentifier());
            childCopy.setParent(parent);
            copyNodesAndAttachToParent(childCopy, n.getChildren());
        }
    }

    @Override
    public List<SourcePermissionsNode> getAllNodes() {
        List<SourcePermissionsNode> resultList
                = em.createQuery("select spn from SourcePermissionsNode spn", SourcePermissionsNode.class).getResultList();
        resultList.forEach(r -> r.getGroups().size()); //triggers lazy fetch
        return resultList;
    }

    @Override
    public Optional<SourcePermissionsNode> getBySourceNodeIdentifier(SourceNodeIdentifier sourceNodeIdentifier) {
        return Optionals.ofThrowing(() ->
                em.createQuery("select spn from SourcePermissionsNode spn " +
                                "where spn.identifier.sourceIdentifier = :sourceIdentifier " +
                                "and spn.identifier.unitIdentifier = :unitIdentifier ",
                        SourcePermissionsNode.class)
                        .setParameter("sourceIdentifier", sourceNodeIdentifier.getSourceIdentifier())
                        .setParameter("unitIdentifier", sourceNodeIdentifier.getUnitIdentifier())
                        .getSingleResult());
    }

    @Override
    public SourcePermissionsNode save(SourcePermissionsNode sourcePermissionsNode) {
        return null;
    }

    @Override
    public void persist(SourcePermissionsNode sourcePermissionsNode) {
        getBySourceNodeIdentifier(sourcePermissionsNode.getIdentifier())
                .ifPresent(n -> {
                    throw new SourcePermissionsNodeDuplicateException();
                });
        findAndAssignGroups(sourcePermissionsNode);
        super.persist(sourcePermissionsNode);
    }

    private void findAndAssignGroups(SourcePermissionsNode sourcePermissionsNode) {
        Set<UserGroup> groups = sourcePermissionsNode.getGroups().stream()
                .map(g -> userGroupManager.getById(g.getId()).orElseGet(() -> {
                    throw new UserGroupNotFoundException();
                }))
                .collect(Collectors.toSet());
        sourcePermissionsNode.setGroups(groups);
    }

    @Override
    @Transactional
    public void grantAccess(UserGroup group, SourceNodeIdentifier sourceNodeIdentifier) {
        SourcePermissionsNode sourcePermissionsNode = getBySourceNodeIdentifier(sourceNodeIdentifier).orElseGet(() -> {
            Set<UserGroup> userGroups = new HashSet<>();
            return new SourcePermissionsNode(sourceNodeIdentifier, userGroups);
        });
        sourcePermissionsNode.getGroups().add(group);
        em.merge(sourcePermissionsNode);
    }

    @Override
    @Transactional
    public void revokeAccess(UserGroup group, SourceNodeIdentifier sourceNodeIdentifier) {
        getBySourceNodeIdentifier(sourceNodeIdentifier)
                .ifPresent((sourcePermissionsNode) -> sourcePermissionsNode.getGroups().remove(group));
    }

    @Override
    protected Class<SourcePermissionsNode> getEntityClass() {
        return SourcePermissionsNode.class;
    }

}
