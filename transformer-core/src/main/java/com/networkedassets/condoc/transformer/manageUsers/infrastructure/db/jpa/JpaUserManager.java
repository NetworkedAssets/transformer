package com.networkedassets.condoc.transformer.manageUsers.infrastructure.db.jpa;

import com.google.common.base.Strings;
import com.networkedassets.condoc.transformer.common.PersistenceManager;
import com.networkedassets.condoc.transformer.common.exceptions.BadRequestException;
import com.networkedassets.condoc.transformer.common.exceptions.UserNotFoundException;
import com.networkedassets.condoc.transformer.manageUsers.core.boundary.UserGroupManager;
import com.networkedassets.condoc.transformer.manageUsers.core.boundary.UserManager;
import com.networkedassets.condoc.transformer.manageUsers.core.entity.User;
import com.networkedassets.condoc.transformer.util.functional.Optionals;

import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Singleton
public class JpaUserManager extends PersistenceManager<User> implements UserManager {

    @Inject
    UserGroupManager userGroupManager;

    @Override
    public void persist(User user) {
        if (!user.isValidUser()) {
            throw new BadRequestException("User data not valid");
        }
        super.persist(user);
        userGroupManager.addUserToDefaultGroups(user);
    }

    @Override
    public User save(String username, User user) {
        if (!user.isValidUserIgnorePassword()) {
            throw new BadRequestException("User data not valid");
        }
        User oldUser = find(username)
                .orElseThrow(UserNotFoundException::new);
        if (!Strings.isNullOrEmpty(user.getPasswordHash())) {
            oldUser.setPasswordHash(user.getPasswordHash());
        }
        merge(oldUser);
        return oldUser;
    }

    @Override
    public Optional<User> getByUsername(String username) {
        return find(username);
    }

    @Override
    public List<User> getAllUsers() {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<User> query = criteriaBuilder.createQuery(User.class);
        Root<User> root = query.from(User.class);
        query.select(root);

        return em.createQuery(query).getResultList();
    }

    @Override
    @Transactional
    public void removeByUsername(String username) {
        getByUsername(username).ifPresent(this::remove);
    }

    @Override
    public Optional<User> getByCredentials(User.Credentials credentials) {
        Optional<User> user = Optionals.ofThrowingSpecificException(NoResultException.class, () ->
                em.createQuery("select u from Users u where u.username = :username", User.class)
                        .setParameter("username", credentials.getUsername())
                        .getSingleResult()
        );

        return user.filter(u -> u.passwordMatches(credentials.getPassword()));
    }

    @Override
    public void remove(User user) {
        userGroupManager.removeUserFromDefaultGroupsAndCleanGroups(user);
        super.remove(user);
    }

    @Override
    protected Class<User> getEntityClass() {
        return User.class;
    }

}
