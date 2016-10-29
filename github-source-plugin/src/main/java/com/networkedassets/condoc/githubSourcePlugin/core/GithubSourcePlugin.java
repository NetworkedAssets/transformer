package com.networkedassets.condoc.githubSourcePlugin.core;

import com.google.common.collect.ImmutableList;
import com.networkedassets.condoc.githubSourcePlugin.core.boundary.GithubClient;
import com.networkedassets.condoc.transformer.common.SourceNodeIdentifier;
import com.networkedassets.condoc.transformer.dispatchRawData.core.boundary.require.RawData;
import com.networkedassets.condoc.transformer.managePermissions.core.entity.SourceStructureRootNode;
import com.networkedassets.condoc.transformer.managePlugins.core.boundary.provide.SourcePlugin;
import com.networkedassets.condoc.transformer.manageSources.core.entity.Source;
import com.networkedassets.condoc.transformer.manageSources.core.entity.SourceSettingField;

import javax.inject.Inject;
import javax.servlet.annotation.WebListener;
import java.util.List;
import java.util.Optional;

@WebListener
public class GithubSourcePlugin implements SourcePlugin {
    public static final String GITHUB_PLUGIN_IDENTIFIER = "github-source";

    @Inject
    private GithubClient githubClient;

    @Override
    public Optional<RawData> fetchRawData(SourceNodeIdentifier sourceNodeIdentifier, Source source) {
        return Optional.ofNullable(githubClient.fetchRawData(sourceNodeIdentifier, source));
    }

    @Override
    public List<SourceSettingField> getRequiredSettingsParameters() {
        return ImmutableList.of(
                new SourceSettingField().setName("username").setType(SourceSettingField.Type.TEXT),
                new SourceSettingField().setName("password").setType(SourceSettingField.Type.PASSWORD));
    }

    @Override
    public VerificationStatus verify(Source source) {

        return githubClient.isVerified(source);
    }

    @Override
    public SourceStructureRootNode fetchStructureForSource(Source source) {

        return githubClient.fetchStructure(source);
    }

    @Override
    public String getIdentifier() {
        return GITHUB_PLUGIN_IDENTIFIER;
    }
}
