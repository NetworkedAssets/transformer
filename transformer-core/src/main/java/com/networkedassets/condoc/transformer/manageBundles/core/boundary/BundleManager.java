package com.networkedassets.condoc.transformer.manageBundles.core.boundary;

import com.networkedassets.condoc.transformer.manageBundles.core.entity.Bundle;
import com.networkedassets.condoc.transformer.manageUsers.core.entity.UserGroup;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface BundleManager {

    // todo rename methods?

    void persist(Bundle bundle);

    Bundle save(int id, Bundle bundle);

    Bundle saveForUserGroups(int id, Bundle bundle, Set<UserGroup> userGroups);

    Optional<Bundle> getById(Integer id);

    Optional<Bundle> getByIdIfAnyGroupHasAccess(Integer id, Set<UserGroup> userGroups);

    List<Bundle> getAllBundles();

    List<Bundle> getAllBundlesForUserGroups(Set<UserGroup> userGroups);

    void removeByIdForUserGroups(Integer id, Set<UserGroup> userGroups);
}
