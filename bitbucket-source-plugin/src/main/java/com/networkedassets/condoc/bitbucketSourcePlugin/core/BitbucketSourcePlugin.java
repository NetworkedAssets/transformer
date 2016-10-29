package com.networkedassets.condoc.bitbucketSourcePlugin.core;

import com.google.common.collect.ImmutableList;
import com.networkedassets.condoc.bitbucketSourcePlugin.core.boundary.BitbucketClient;
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
public class BitbucketSourcePlugin implements SourcePlugin {
    public final static String BITBUCKET_PLUGIN_IDENTIFIER = "bitbucket-source";

    @Inject
    private BitbucketClient bitbucketClient;

    @Override
    public Optional<RawData> fetchRawData(SourceNodeIdentifier sourceNodeIdentifier, Source source) {
        return Optional.ofNullable(bitbucketClient.fetchRawData(sourceNodeIdentifier, source));
    }

    @Override
    public List<SourceSettingField> getRequiredSettingsParameters() {
        return ImmutableList.of(
                new SourceSettingField().setName("username").setType(SourceSettingField.Type.TEXT),
                new SourceSettingField().setName("password").setType(SourceSettingField.Type.PASSWORD));
    }

    @Override
    public VerificationStatus verify(Source source) {
        return bitbucketClient.isVerified(source);
    }

    @Override
    public SourceStructureRootNode fetchStructureForSource(Source source) {
        return bitbucketClient.fetchStructure(source);
    }

    @Override
    public String getIdentifier() {
        return BITBUCKET_PLUGIN_IDENTIFIER;
    }
}
