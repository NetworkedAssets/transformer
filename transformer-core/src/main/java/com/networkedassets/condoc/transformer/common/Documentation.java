package com.networkedassets.condoc.transformer.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.networkedassets.condoc.transformer.common.docitem.ClassDiagramDocItem;
import com.networkedassets.condoc.transformer.common.docitem.DocItem;
import com.networkedassets.condoc.transformer.common.docitem.HtmlDocItem;
import com.networkedassets.condoc.transformer.common.docitem.OmniDocItem;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/*
 * DANGER, DANGER: if you change anything in this class, make sure not to
 * break JavadocConverterPluginToolsJarWrapper (from the javadoc plugin) by accident
 */
@Entity
public class Documentation<DOCUMENTATION_TYPE extends DocItem> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JsonIgnore
    private SourceUnit sourceUnit;

    private DocumentationType type;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, targetEntity = DocItem.class)
    private Set<DOCUMENTATION_TYPE> docItems = new HashSet<>();

    public DocumentationType getType() {
        return type;
    }

    public Documentation<DOCUMENTATION_TYPE> setType(DocumentationType type) {
        this.type = type;
        return this;
    }

    public Integer getId() {
        return id;
    }

    public Documentation<DOCUMENTATION_TYPE> setId(Integer id) {
        this.id = id;
        return this;
    }

    public SourceUnit getSourceUnit() {
        return sourceUnit;
    }

    public Documentation<DOCUMENTATION_TYPE> setSourceUnit(SourceUnit sourceUnit) {
        this.sourceUnit = sourceUnit;
        return this;
    }

    public Set<DOCUMENTATION_TYPE> getDocItems() {
        return docItems;
    }

    public Documentation<DOCUMENTATION_TYPE> setDocItems(Set<DOCUMENTATION_TYPE> docItems) {
        this.docItems = docItems;
        return this;
    }

    public Documentation<DOCUMENTATION_TYPE> addDocItem(DOCUMENTATION_TYPE docItem) {
        this.docItems.add(docItem);
        return this;
    }

    public Documentation<DOCUMENTATION_TYPE> addDocItems(Collection<DOCUMENTATION_TYPE> docItems) {
        this.docItems.addAll(docItems);
        return this;
    }

    public enum DocumentationType {
        JSON, HTML, CLASS_DIAGRAM, OMNIDOC;

        public static <T extends DocItem> DocumentationType of(Class<T> clazz) {
            if (clazz.equals(OmniDocItem.class)) return OMNIDOC;
            if (clazz.equals(ClassDiagramDocItem.class)) return CLASS_DIAGRAM;
            if (clazz.equals(HtmlDocItem.class)) return HTML;
            throw new IllegalArgumentException("illegal class: " + clazz.getCanonicalName());
        }
    }

    public interface Views {
        interface Content extends HtmlDocItem.Views.Content {

        }
    }
}
