package com.networkedassets.condoc.transformer.manageUsers

import com.networkedassets.condoc.transformer.common.PersistenceTestingAbility
import com.networkedassets.condoc.transformer.manageUsers.core.boundary.UserGroupManager
import com.networkedassets.condoc.transformer.manageUsers.core.boundary.UserManager
import com.networkedassets.condoc.transformer.manageUsers.core.entity.User
import com.networkedassets.condoc.transformer.manageUsers.core.entity.UserGroup
import com.networkedassets.condoc.transformer.manageUsers.infrastructure.db.jpa.JpaUserGroupManager
import com.networkedassets.condoc.transformer.manageUsers.infrastructure.db.jpa.JpaUserManager
import spock.lang.Specification

class UserManagerSpec extends Specification implements PersistenceTestingAbility {
    static UserManager userManager
    static UserGroupManager userGroupManager
    private User user1
    private User user2
    private UserGroup allGroup

    @SuppressWarnings("GroovyAccessibility")
    def setupSpec() {
        userManager = new JpaUserManager()
        userManager.em = entityManager
        userGroupManager = new JpaUserGroupManager()
        userGroupManager.em = entityManager
        userManager.userGroupManager = userGroupManager
    }

    def setup() {
        this.user1 = new User("AAA").setPassword("A")
        this.user2 = new User("BBB").setPassword("B")
        allGroup = new UserGroup("all")
        userGroupManager.persist(this.allGroup)
        flushAndClearCache()
    }

    def "Persists new users"() {
        when:
        userManager.persist(user1)
        userManager.persist(user2)
        flushAndClearCache()

        then:
        List<User> list = userManager.getAllUsers()
        list.size() == 2
        list.contains(user1)
        list.contains(user2)
    }

    def "Gets user by username"() {
        given:
        userManager.persist(user1)
        flushAndClearCache()

        when:
        Optional<User> fetchedUser = userManager.getByUsername(user1.getUsername())

        then:
        fetchedUser.isPresent()
        user1.equals(fetchedUser.get())
    }

    def "Removes users by object"() {
        given:
        userManager.persist(user1)
        flushAndClearCache()

        when:
        User fetchedUser = userManager.getByUsername(user1.getUsername()).get()
        userManager.remove(fetchedUser)

        then:
        userManager.getAllUsers().size() == 0
    }

    def "Removes users by username"() {
        given:
        userManager.persist(user1)
        flushAndClearCache()

        when:
        userManager.removeByUsername(user1.getUsername())

        then:
        userManager.getAllUsers().size() == 0
    }

    def "Adds new users to default groups"() {
        when:
        userManager.persist(user1)
        flushAndClearCache()

        then:
        UserGroup fetchedAllGroup = userGroupManager.getById(allGroup.getId()).get()
        fetchedAllGroup.getUsers().size() == 1
        fetchedAllGroup.getUsers().contains(user1)

        Optional<UserGroup> fetchedUsersGroup = userGroupManager.getByName(user1.getUsername())
        fetchedUsersGroup.isPresent()
        fetchedUsersGroup.get().users.size() == 1
        fetchedUsersGroup.get().users.contains(user1)
    }

    def cleanupSpec() {
        entityManager.close()
    }

}

