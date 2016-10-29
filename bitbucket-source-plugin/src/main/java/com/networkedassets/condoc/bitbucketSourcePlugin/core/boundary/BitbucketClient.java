package com.networkedassets.condoc.bitbucketSourcePlugin.core.boundary;


import com.networkedassets.condoc.transformer.common.SourceNodeIdentifier;
import com.networkedassets.condoc.transformer.dispatchRawData.core.boundary.require.RawData;
import com.networkedassets.condoc.transformer.managePermissions.core.entity.SourceStructureRootNode;
import com.networkedassets.condoc.transformer.managePlugins.core.boundary.provide.SourcePlugin;
import com.networkedassets.condoc.transformer.manageSources.core.entity.Source;

public interface BitbucketClient {

    SourceStructureRootNode fetchStructure(Source source);

    SourcePlugin.VerificationStatus isVerified(Source source);

    RawData fetchRawData(SourceNodeIdentifier sourceNodeIdentifier, Source source);

}
