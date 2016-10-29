package com.networkedassets.condoc.transformer.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Allows to recognise a specific Source Unit
 */
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"sourceIdentifier", "unitIdentifier"})})
public final class SourceNodeIdentifier implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(Views.Persisted.class)
    private Integer id;

    private static final String JOIN_TOKEN = "|";
    private static final String SPLIT_TOKEN = "\\|";

    /**
     * Distinguishes a specific source like bitbucket or github, usually by URL
     */
    private Integer sourceIdentifier;

    /**
     * Distinguishes a specific unit within the source (ex. single branch in git)
     */
    private String unitIdentifier;

    public SourceNodeIdentifier() {
    }

    public SourceNodeIdentifier(Integer sourceIdentifier, String... unitIdentifierParts) {
        this.sourceIdentifier = sourceIdentifier;
        this.unitIdentifier = Arrays.stream(unitIdentifierParts).collect(Collectors.joining(JOIN_TOKEN));
    }

    public SourceNodeIdentifier setSourceIdentifier(Integer sourceIdentifier) {
        this.sourceIdentifier = sourceIdentifier;
        return this;
    }

    public SourceNodeIdentifier setUnitIdentifier(String unitIdentifier) {
        this.unitIdentifier = unitIdentifier;
        return this;
    }

    public Integer getSourceIdentifier() {
        return sourceIdentifier;
    }

    public String getUnitIdentifier() {
        return unitIdentifier;
    }

    @Transient
    public String getPartOfUnitIdentifier(int partNumber) {
        return this.unitIdentifier.split(SPLIT_TOKEN)[partNumber];
    }

    @JsonIgnore
    @Transient
    public String getLastPartOfUnitIdentifier() {
        String[] split = this.unitIdentifier.split(SPLIT_TOKEN);
        return split[split.length - 1];
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SourceNodeIdentifier)) return false;
        SourceNodeIdentifier that = (SourceNodeIdentifier) o;
        return Objects.equals(sourceIdentifier, that.sourceIdentifier) &&
                Objects.equals(unitIdentifier, that.unitIdentifier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sourceIdentifier, unitIdentifier);
    }

    @Override
    public String toString() {
        return "SourceNodeIdentifier{" +
                "sourceIdentifier='" + sourceIdentifier + '\'' +
                ", unitIdentifier='" + unitIdentifier + '\'' +
                '}';
    }


    public Integer getId() {
        return id;
    }

    public SourceNodeIdentifier setId(Integer id) {
        this.id = id;
        return this;
    }

    interface Views {
        interface Persisted {

        }
    }
}
