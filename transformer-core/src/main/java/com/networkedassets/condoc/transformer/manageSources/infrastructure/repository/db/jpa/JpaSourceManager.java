package com.networkedassets.condoc.transformer.manageSources.infrastructure.repository.db.jpa;

import com.networkedassets.condoc.transformer.common.PersistenceManager;
import com.networkedassets.condoc.transformer.common.SourceNodeIdentifier;
import com.networkedassets.condoc.transformer.common.exceptions.SourceNotFoundException;
import com.networkedassets.condoc.transformer.common.exceptions.WrongSourceSettingsException;
import com.networkedassets.condoc.transformer.managePermissions.core.entity.SourceStructureRootNode;
import com.networkedassets.condoc.transformer.managePlugins.core.boundary.PluginNotFoundException;
import com.networkedassets.condoc.transformer.managePlugins.core.boundary.provide.PluginManager;
import com.networkedassets.condoc.transformer.managePlugins.core.boundary.provide.SourcePlugin;
import com.networkedassets.condoc.transformer.manageSources.core.boundary.SourceManager;
import com.networkedassets.condoc.transformer.manageSources.core.entity.Source;
import com.networkedassets.condoc.transformer.manageSources.core.entity.SourceSettingField;
import com.networkedassets.condoc.transformer.util.functional.Optionals;

import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Singleton
public class JpaSourceManager extends PersistenceManager<Source> implements SourceManager {

    @Inject
    private PluginManager pluginManager;

    @Override
    public void verifyAndPersist(Source source) {
        verifySourceOrThrow(source);
        persist(source);
    }

    @Override
    @Transactional
    public Source verifyAndSave(Integer id, Source source) {
        Source originalSource = getByIdentifier(id).orElseThrow(SourceNotFoundException::new);
        source.setId(originalSource.getId());

        List<SourceSettingField> settingsWithPasswords = source.getSettings().stream().map(ssf -> {
            if (ssf.getType() != SourceSettingField.Type.PASSWORD ||
                    !SourceSettingField.PASSWORD_PLACEHOLDER.equals(ssf.getValue())) {
                return ssf;
            }
            return originalSource.getSettingByKey(ssf.getName())
                    .filter(o -> o.getType() == SourceSettingField.Type.PASSWORD)
                    .orElse(ssf);
        }).collect(Collectors.toList());

        source.setSettings(settingsWithPasswords);
        verifySourceOrThrow(source);
        originalSource.getSettings().clear();
        em.flush();     //because hibernate ain't too smart

//        originalSource.getSettings().addAll(source.getSettings());
        return merge(source);
    }

    private void verifySourceOrThrow(Source source) {
        SourcePlugin.VerificationStatus status = pluginManager
                .getSourcePluginForGivenSource(source)
                .orElseThrow(() -> new PluginNotFoundException(source.getPluginIdentifier()))
                .verify(source);
        if (status != SourcePlugin.VerificationStatus.OK) {
            throw new WrongSourceSettingsException(status.toString());
        }
    }

    @Override
    @Transactional
    public void removeByIdentifier(Integer sourceIdentifier) {
        getByIdentifier(sourceIdentifier).ifPresent(this::remove);
    }

    @Override
    public void removeBySourceNodeIdentifier(SourceNodeIdentifier sourceNodeIdentifier) {
        removeByIdentifier(sourceNodeIdentifier.getSourceIdentifier());
    }

    @Override
    public Optional<Source> getByIdentifier(Integer sourceIdentifier) {
        return find(sourceIdentifier);
    }

    @Override
    public Source getByIdentifierOrThrow(Integer sourceIdentifier) {
        return getByIdentifier(sourceIdentifier).orElseThrow(() -> new SourceNotFoundException("Source by identifier: " + sourceIdentifier));
    }

    @Override
    public Optional<Source> getWithSettingsByIdentifier(Integer sourceIdentifier) {
        Optional<Source> source = getByIdentifier(sourceIdentifier);
        source.ifPresent(s -> s.getSettings().size()); // triggers lazy fetch
        return source;
    }

    @Override
    public Optional<Source> getByUrl(String sourceUrl) {
        return Optionals.ofThrowing(() ->
                em.createQuery("select s from Source s where s.url = :url", Source.class)
                        .setParameter("url", sourceUrl)
                        .getSingleResult()
        );
    }

    @Override
    public Source getWithSettingsByIdentifierOrThrow(Integer sourceIdentifier) {
        return getWithSettingsByIdentifier(sourceIdentifier).orElseThrow(() -> new SourceNotFoundException("Source by identifier: " + sourceIdentifier));
    }

    @Override
    public Optional<Source> getBySourceNodeIdentifier(SourceNodeIdentifier sourceNodeIdentifier) {
        return getByIdentifier(sourceNodeIdentifier.getSourceIdentifier());
    }

    @Override
    public SourceStructureRootNode getStructureFromSource(Source source) {
        return pluginManager
                .getSourcePluginForGivenSource(source)
                .orElseThrow(PluginNotFoundException::new)
                .fetchStructureForSource(source);
    }

    @Override
    public List<Source> getAllSources() {
        return em.createQuery("select s from Source s", Source.class).getResultList();
    }

    @Override
    protected Class<Source> getEntityClass() {
        return Source.class;
    }
}
