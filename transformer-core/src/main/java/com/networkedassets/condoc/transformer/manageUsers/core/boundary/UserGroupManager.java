package com.networkedassets.condoc.transformer.manageUsers.core.boundary;

import com.networkedassets.condoc.transformer.manageUsers.core.entity.User;
import com.networkedassets.condoc.transformer.manageUsers.core.entity.UserGroup;

import java.util.List;
import java.util.Optional;

public interface UserGroupManager {
    void persist(UserGroup group);

    UserGroup merge(UserGroup group);

    UserGroup checkRestrictionsAndSave(Integer id, UserGroup group);

    UserGroup save(Integer id, UserGroup group);

    void removeById(Integer id);

    void removeByIdIfNotAutocreated(Integer id);

    Optional<UserGroup> getById(Integer id);

    Optional<UserGroup> getByName(String name);

    List<UserGroup> getAllGroups();

    void addUserToGroup(User user, Integer groupId);

    void addUserToDefaultGroups(User user);

    void removeUserFromDefaultGroupsAndCleanGroups(User user);

    void removeUserFromGroup(User user, Integer groupId);

    Optional<UserGroup> getWithUsersById(Integer groupId);
}
