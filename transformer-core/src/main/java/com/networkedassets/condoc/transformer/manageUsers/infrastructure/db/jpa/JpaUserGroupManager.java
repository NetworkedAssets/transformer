package com.networkedassets.condoc.transformer.manageUsers.infrastructure.db.jpa;

import com.networkedassets.condoc.transformer.common.PersistenceManager;
import com.networkedassets.condoc.transformer.common.exceptions.AllUserGroupNotFoundException;
import com.networkedassets.condoc.transformer.common.exceptions.BadRequestException;
import com.networkedassets.condoc.transformer.common.exceptions.UserGroupNotFoundException;
import com.networkedassets.condoc.transformer.manageUsers.core.boundary.UserGroupManager;
import com.networkedassets.condoc.transformer.manageUsers.core.boundary.UserManager;
import com.networkedassets.condoc.transformer.manageUsers.core.entity.User;
import com.networkedassets.condoc.transformer.manageUsers.core.entity.UserGroup;
import com.networkedassets.condoc.transformer.util.functional.Optionals;

import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Singleton
public class JpaUserGroupManager extends PersistenceManager<UserGroup> implements UserGroupManager {

    @Inject
    UserManager userManager;

    @Override
    @Transactional
    public UserGroup checkRestrictionsAndSave(Integer id, UserGroup group) {
        UserGroup oldGroup = find(id)
                .orElseThrow(UserGroupNotFoundException::new);
        if (oldGroup.isAutomaticallyCreated()) {
            group.setName(oldGroup.getName());
        }
        if (!oldGroup.isManualUserAssignementAllowed()) {
            group.setUsers(new HashSet<>(oldGroup.getUsers()));
        }
        return save(group.getId(), group);
    }

    @Override
    @Transactional
    public UserGroup save(Integer id, UserGroup group) {
        UserGroup oldGroup = find(id)
                .orElseThrow(UserGroupNotFoundException::new)
                .setName(group.getName());
        oldGroup.getUsers().clear();
        oldGroup.setUsers(group.getUsers());
        oldGroup.setRoles(group.getRoles());
        oldGroup.setAutomaticallyCreated(group.isAutomaticallyCreated());
        oldGroup.setManualUserAssignementAllowed(group.isManualUserAssignementAllowed());

        merge(oldGroup);
        return oldGroup;
    }

    @Override
    @Transactional
    public void removeById(Integer id) {
        find(id).ifPresent(this::remove);
    }

    @Override
    @Transactional
    public void removeByIdIfNotAutocreated(Integer id) {
        remove(find(id).filter(g -> !g.isAutomaticallyCreated())
                .orElseThrow(() -> new BadRequestException("Can't manually remove autocreated usergroup")));
    }

    @Override
    @Transactional
    public void remove(UserGroup userGroup) {
        userGroup.getUsers().clear();
        super.remove(userGroup);
    }

    @Override
    public Optional<UserGroup> getById(Integer id) {
        return find(id);
    }

    @Override
    @Transactional
    public Optional<UserGroup> getWithUsersById(Integer id) {
        Optional<UserGroup> group = getById(id);
        group.ifPresent(g -> g.getUsers().size()); //triggers lazy fetch
        return group;
    }

    @Override
    public Optional<UserGroup> getByName(String name) {
        return Optionals.ofThrowing(() ->
                em.createQuery("select g from UserGroup g where g.name = :name", UserGroup.class)
                        .setParameter("name", name)
                        .getSingleResult());
    }

    @Override
    public List<UserGroup> getAllGroups() {
        return em.createQuery("select g from UserGroup g", UserGroup.class).getResultList();
    }

    @Override
    @Transactional
    public void addUserToGroup(User user, Integer groupId) {
        UserGroup userGroup = getById(groupId).orElseThrow(UserGroupNotFoundException::new);
        userGroup.getUsers().add(user);
        merge(userGroup);
    }

    @Override
    public void addUserToDefaultGroups(User user) {
        UserGroup allGroup = getAllUserGroup();
        UserGroup usersGroup = getUserGroupForUser(user);
        addUserToGroup(user, allGroup.getId());
        addUserToGroup(user, usersGroup.getId());
    }

    @Override
    public void removeUserFromDefaultGroupsAndCleanGroups(User user) {
        UserGroup allGroup = getAllUserGroup();
        UserGroup usersGroup = getUserGroupForUser(user);
        removeUserFromGroup(user, allGroup.getId());
        removeUserFromGroup(user, usersGroup.getId());
        remove(usersGroup);
    }

    private UserGroup getUserGroupForUser(User user) {
        return getByName(user.getUsername())
                .orElseGet(() -> {
                    UserGroup userGroup = new UserGroup(user.getUsername())
                            .setAutomaticallyCreated(true)
                            .setManualUserAssignementAllowed(false);
                    persist(userGroup);
                    return userGroup;
                });
    }

    private UserGroup getAllUserGroup() {
        return getByName("all").orElseThrow(AllUserGroupNotFoundException::new);
    }

    @Override
    @Transactional
    public void removeUserFromGroup(User user, Integer groupId) {
        UserGroup userGroup = find(groupId).orElseThrow(UserGroupNotFoundException::new);
        userGroup.getUsers().remove(user);
        merge(userGroup);
    }

    @Override
    protected Class<UserGroup> getEntityClass() {
        return UserGroup.class;
    }
}
