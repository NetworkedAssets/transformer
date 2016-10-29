package com.networkedassets.condoc.transformer.manageUsers.core.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.google.common.base.Strings;
import com.networkedassets.condoc.transformer.common.exceptions.TransformerException;
import org.apache.commons.lang3.RandomStringUtils;

import javax.persistence.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Entity(name = "Users")
public class User implements Principal {

    @Id
    private String username;

    private String passwordHash;

    @ManyToMany(mappedBy = "users", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonView(Views.Groups.class)
    private Set<UserGroup> groups = new HashSet<>();

    private String passwordSalt;

    public User() {
    }

    public User(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public User setUsername(String username) {
        this.username = username;
        return this;
    }

    public Set<UserGroup> getGroups() {
        return groups;
    }

    public User setGroups(Set<UserGroup> groups) {
        this.groups = groups;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }

    @Override
    @JsonIgnore
    public String getName() {
        return getUsername();
    }

    public User setPasswordHash(String password) {
        this.passwordHash = password;
        return this;
    }

    public User setPassword(String password) {
        passwordSalt = RandomStringUtils.random(8);
        passwordHash = hashPassword(password, passwordSalt);
        return this;
    }

    @JsonView(Views.Full.class)
    public String getPasswordHash() {
        return passwordHash;
    }

    public Set<String> getRoles(){
        return getGroups()
                .stream()
                .map(UserGroup::getRoles)
                .flatMap(Set::stream)
                .collect(Collectors.toSet());
    }

    public boolean hasRole(String role) {
        return getGroups()
                .stream()
                .filter(group -> group.hasRole(role))
                .findAny().isPresent();
    }

    public boolean hasAnyOfRoles(Set<String> roles) {
        for (UserGroup group : groups) {
            for (String role : roles) {
                if (group.hasRole(role)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static String hashPassword(String password, String salt) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update((password + salt).getBytes());
            return String.format("%064x", new java.math.BigInteger(1, messageDigest.digest()));
        } catch (NoSuchAlgorithmException e) {
            throw new TransformerException(e);
        }
    }

    public boolean passwordMatches(String password) {
        return passwordHash.equals(hashPassword(password, passwordSalt));
    }

    @JsonIgnore
    public boolean isValidUser() {
        return isValidUserIgnorePassword()
                && !Strings.isNullOrEmpty(passwordHash);
    }

    @JsonIgnore
    public boolean isValidUserIgnorePassword() {
        return !Strings.isNullOrEmpty(username);
    }

    public interface Views {
        interface Groups {
        }

        interface Full extends Groups {

        }
    }

    public static class Credentials {
        private String username;
        private String password;

        public Credentials() {
        }

        public Credentials(String username, String password) {
            setUsername(username);
            setPassword(password);
        }

        public String getUsername() {
            return username;
        }

        public Credentials setUsername(String username) {
            this.username = username;
            return this;
        }

        public String getPassword() {
            return password;
        }

        public Credentials setPassword(String password) {
            this.password = password;
            return this;
        }
    }
}
