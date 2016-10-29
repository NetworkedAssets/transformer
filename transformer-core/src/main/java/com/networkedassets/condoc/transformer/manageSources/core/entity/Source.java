package com.networkedassets.condoc.transformer.manageSources.core.entity;

import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.*;
import java.util.List;
import java.util.Optional;

@Entity
public class Source {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true)
    private String url;

    private String pluginIdentifier;

    private String name;

    @JsonView(Views.Persisted.class)
    @OneToMany(mappedBy = "source", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<SourceSettingField> settings;

    public Integer getId() {
        return id;
    }

    public Source setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public Source setUrl(String id) {
        this.url = id;
        return this;
    }

    public String getPluginIdentifier() {
        return pluginIdentifier;
    }


    public Source setPluginIdentifier(String pluginIdentifier) {
        this.pluginIdentifier = pluginIdentifier;
        return this;
    }

    public String getName() {
        return name;
    }

    public Source setName(String name) {
        this.name = name;
        return this;
    }

    public List<SourceSettingField> getSettings() {
        return settings;
    }

    public Source setSettings(List<SourceSettingField> settings) {
        this.settings = settings;
        this.settings.forEach(field -> field.setSource(this));
        return this;
    }

    public Optional<SourceSettingField> getSettingByKey(String key) {
        return settings
                .stream()
                .filter(field -> field.getName().equals(key))
                .findFirst();
    }

    public SourceSettingField getSettingByKeyOrThrow(String key) {
        return getSettingByKey(key)
                .orElseThrow(() -> new SettingNotFoundException(key));
    }

    private class SettingNotFoundException extends RuntimeException {
        SettingNotFoundException(String key) {
            super(key);
        }
    }

    public interface Views {

        interface Persisted extends SourceSettingField.Views.Persisted {
        }

        interface Full extends Persisted {
        }

    }
}