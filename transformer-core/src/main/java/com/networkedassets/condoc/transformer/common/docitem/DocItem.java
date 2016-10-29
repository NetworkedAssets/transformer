package com.networkedassets.condoc.transformer.common.docitem;

import com.networkedassets.condoc.transformer.common.Documentation;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/*
 * DANGER, DANGER: if you change anything in this class, make sure not to
 * break JavadocConverterPluginToolsJarWrapper (from the javadoc plugin) by accident
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class DocItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "TEXT")
    protected String fullName = "";

    @ElementCollection(fetch = FetchType.EAGER)
    @Column(columnDefinition = "TEXT")
    protected List<String> locationPath = new ArrayList<>();

    public List<String> getLocationPath() {
        return locationPath;
    }

    public DocItem setLocationPath(List<String> locationPath) {
        this.locationPath = locationPath;
        return this;
    }

    public String getFullName() {
        return fullName;
    }

    public DocItem setFullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    public Integer getId() {
        return id;
    }

    public DocItem setId(Integer id) {
        this.id = id;
        return this;
    }

    abstract public Documentation.DocumentationType getDocItemType();

    abstract public String getShortName();
}