package com.networkedassets.condoc.transformer.managePermissions.core.boundary;

import com.networkedassets.condoc.transformer.common.SourceNodeIdentifier;
import com.networkedassets.condoc.transformer.managePermissions.core.entity.SourceStructureRootNode;
import com.networkedassets.condoc.transformer.managePermissions.infrastructure.db.jpa.SourcePermissionsNode;
import com.networkedassets.condoc.transformer.manageUsers.core.entity.User;
import com.networkedassets.condoc.transformer.manageUsers.core.entity.UserGroup;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface SourcePermissionsManager {

    List<SourcePermissionsNode> getAllNodes();

    Optional<SourcePermissionsNode> getBySourceNodeIdentifier(SourceNodeIdentifier sourceNodeIdentifier);

    void persist(SourcePermissionsNode sourcePermissionsNode);

    SourcePermissionsNode save(SourcePermissionsNode sourcePermissionsNode);

    void grantAccess(UserGroup group, SourceNodeIdentifier sourceNodeIdentifier);

    void revokeAccess(UserGroup group, SourceNodeIdentifier sourceNodeIdentifier);

    boolean groupHasAccessById(Integer groupId, SourceNodeIdentifier sourceNodeIdentifier);

    boolean groupHasAccess(UserGroup group, SourceNodeIdentifier sourceNodeIdentifier);

    boolean userHasAccessByUsername(String username, SourceNodeIdentifier sourceNodeIdentifier);

    boolean userHasAccess(User user, SourceNodeIdentifier sourceNodeIdentifier);

    SourceStructureRootNode filterStructureByUserGroupsPermission(SourceStructureRootNode structureRootNode, Collection<UserGroup> userGroups);
}
