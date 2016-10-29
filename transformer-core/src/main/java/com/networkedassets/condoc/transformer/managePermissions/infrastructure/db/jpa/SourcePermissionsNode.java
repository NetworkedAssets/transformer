package com.networkedassets.condoc.transformer.managePermissions.infrastructure.db.jpa;

import com.networkedassets.condoc.transformer.common.SourceNodeIdentifier;
import com.networkedassets.condoc.transformer.manageUsers.core.entity.UserGroup;

import javax.persistence.*;
import java.util.Set;

@Entity
public class SourcePermissionsNode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(unique = true)
    private SourceNodeIdentifier identifier;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<UserGroup> groups;

    public SourcePermissionsNode() {
    }

    public SourcePermissionsNode(SourceNodeIdentifier identifier, Set<UserGroup> groups) {
        this.identifier = identifier;
        this.groups = groups;
    }

    public SourceNodeIdentifier getIdentifier() {
        return identifier;
    }

    public SourcePermissionsNode setIdentifier(SourceNodeIdentifier identifier) {
        this.identifier = identifier;
        return this;
    }

    public Set<UserGroup> getGroups() {
        return groups;
    }

    public SourcePermissionsNode setGroups(Set<UserGroup> groups) {
        this.groups = groups;
        return this;
    }

    public Integer getId() {
        return id;
    }

    public SourcePermissionsNode setId(Integer id) {
        this.id = id;
        return this;
    }
}