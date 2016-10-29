package com.networkedassets.condoc.transformer.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.networkedassets.condoc.transformer.common.docitem.HtmlDocItem;
import com.networkedassets.condoc.transformer.manageBundles.core.entity.Bundle;
import com.networkedassets.condoc.transformer.managePermissions.core.entity.Node;

import javax.persistence.*;
import java.util.*;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class SourceUnit implements Node {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(Views.Persisted.class)
    private Integer id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(unique = true)
    private SourceNodeIdentifier sourceNodeIdentifier;

    @Transient
    private Node parent;

    @ManyToMany(mappedBy = "sourceUnits")
    @JsonIgnore
    private Set<Bundle> bundles = new HashSet<>();

    @OneToMany(mappedBy = "sourceUnit", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonView(Views.DocItems.class)
    private Set<Documentation> documentations = new HashSet<>();

    public SourceUnit() {
    }

    public SourceUnit(SourceNodeIdentifier sourceNodeIdentifier) {
        this.sourceNodeIdentifier = sourceNodeIdentifier;
    }

    @Override
    public SourceNodeIdentifier getSourceNodeIdentifier() {
        return sourceNodeIdentifier;
    }

    @Override
    public SourceUnit setParent(Node parent) {
        this.parent = parent;
        if (!parent.getChildren().contains(this)) {
            parent.addChildren(this);
        }
        return this;
    }

    @Override
    public Optional<Node> getParent() {
        return Optional.of(parent);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SourceUnit)) return false;
        SourceUnit that = (SourceUnit) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(sourceNodeIdentifier, that.sourceNodeIdentifier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sourceNodeIdentifier);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Node> getChildren() {
        return Collections.EMPTY_LIST;
    }

    @Override
    public boolean isLeaf() {
        return true;
    }

    @Override
    public boolean isRoot() {
        return false;
    }

    @Override
    public Node addChildren(Node children) {
        return null;
    }

    public Integer getId() {
        return id;
    }

    public SourceUnit setId(Integer id) {
        this.id = id;
        return this;
    }

    public Set<Bundle> getBundles() {
        return bundles;
    }

    public SourceUnit setBundles(Set<Bundle> bundles) {
        this.bundles = bundles;
        return this;
    }

    public SourceUnit setSourceNodeIdentifier(SourceNodeIdentifier sourceNodeIdentifier) {
        this.sourceNodeIdentifier = sourceNodeIdentifier;
        return this;
    }

    public Set<Documentation> getDocumentations() {
        return documentations;
    }

    public SourceUnit setDocumentations(Set<Documentation> documentations) {
        this.documentations.clear();
        this.documentations.addAll(documentations);
        return this;
    }

    public interface Views {

        interface Persisted extends SourceNodeIdentifier.Views.Persisted {

        }

        interface DocItems extends Persisted {

        }

        interface DocItemsWithContent extends HtmlDocItem.Views.Content{

        }

        interface Full extends DocItemsWithContent {

        }
    }
}
