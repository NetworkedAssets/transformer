package com.networkedassets.condoc.transformer.manageSources.core.entity;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"source_id", "name"})})
public class SourceSettingField {

    public static final String PASSWORD_PLACEHOLDER = "*****";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(Views.Persisted.class)
    private Integer id;

    private String name;

    @JsonView(Views.Persisted.class)
    private String value;

    @Enumerated(EnumType.STRING)
    private Type type;

    @ManyToOne
    @JsonIgnore
    private Source source;

    public Integer getId() {
        return id;
    }

    public SourceSettingField setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public SourceSettingField setName(String name) {
        this.name = name;
        return this;
    }

    public String getValue() {
        return value;
    }

    @JsonGetter("value")
    public String getValueWithCensoredOutPassword() {
        return type == Type.PASSWORD ? PASSWORD_PLACEHOLDER : value;
    }

    public SourceSettingField setValue(String value) {
        this.value = value;
        return this;
    }

    public Type getType() {
        return type;
    }

    public SourceSettingField setType(Type type) {
        this.type = type;
        return this;
    }

    public Source getSource() {
        return source;
    }

    public SourceSettingField setSource(Source source) {
        this.source = source;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SourceSettingField)) return false;
        SourceSettingField that = (SourceSettingField) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(value, that.value) &&
                type == that.type &&
                Objects.equals(source, that.source);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, value, type, source);
    }

    @Override
    public String toString() {
        return "SourceSettingField{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", value='" + value + '\'' +
                ", type=" + type +
                ", source=" + source +
                '}';
    }

    public enum Type {
        TEXT, PASSWORD, TEXTAREA
    }

    public interface Views {

        interface Persisted {
        }

        interface Full extends Persisted {

        }
    }
}
