package com.networkedassets.condoc.transformer.manageUsers.core.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class UserGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true)
    private String name;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<User> users = new HashSet<>();

    private boolean automaticallyCreated;
    private boolean manualUserAssignementAllowed = true;

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> roles = new HashSet<>();

    public UserGroup() {
    }

    public UserGroup(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public UserGroup setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public UserGroup setName(String name) {
        this.name = name;
        return this;
    }

    public Set<User> getUsers() {
        return users;
    }

    public UserGroup setUsers(Set<User> users) {
        this.users = users;
        return this;
    }

    public boolean isAutomaticallyCreated() {
        return automaticallyCreated;
    }

    public UserGroup setAutomaticallyCreated(boolean automaticallyCreated) {
        this.automaticallyCreated = automaticallyCreated;
        return this;
    }

    public boolean hasRole(String role) {
        return getRoles().stream()
                .filter(role::equals)
                .findAny()
                .isPresent();
    }

    public Set<String> getRoles() {
        return roles;
    }

    public UserGroup setRoles(Set<String> roles) {
        this.roles = roles;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserGroup userGroup = (UserGroup) o;
        return Objects.equals(id, userGroup.id) &&
                Objects.equals(name, userGroup.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "UserGroup{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", users=" + users +
                ", automaticallyCreated=" + automaticallyCreated +
                '}';
    }

    public boolean isManualUserAssignementAllowed() {
        return manualUserAssignementAllowed;
    }

    public UserGroup setManualUserAssignementAllowed(boolean manualUserAssignementAllowed) {
        this.manualUserAssignementAllowed = manualUserAssignementAllowed;
        return this;
    }

    public interface Views {

        interface Users {
        }

        interface Full extends Users {
        }

    }
}
