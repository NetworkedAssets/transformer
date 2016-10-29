package com.networkedassets.condoc.transformer.manageUsers.core.boundary;

import com.networkedassets.condoc.transformer.manageUsers.core.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserManager {
    void persist(User user);

    /**
     * @return Updated entity
     */
    User save(String username, User user);

    Optional<User> getByUsername(String username);

    List<User> getAllUsers();

    void removeByUsername(String username);

    User merge(User user);

    Optional<User> getByCredentials(User.Credentials credentials);
}
