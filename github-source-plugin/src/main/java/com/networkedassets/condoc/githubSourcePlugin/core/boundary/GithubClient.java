package com.networkedassets.condoc.githubSourcePlugin.core.boundary;


import com.networkedassets.condoc.transformer.common.SourceNodeIdentifier;
import com.networkedassets.condoc.transformer.dispatchRawData.core.boundary.require.RawData;
import com.networkedassets.condoc.transformer.managePermissions.core.entity.SourceStructureRootNode;
import com.networkedassets.condoc.transformer.managePlugins.core.boundary.provide.SourcePlugin;
import com.networkedassets.condoc.transformer.manageSources.core.entity.Source;

public interface GithubClient {

    RawData fetchRawData(SourceNodeIdentifier sourceNodeIdentifier, Source source);

    SourceStructureRootNode fetchStructure(Source source);

    SourcePlugin.VerificationStatus isVerified(Source source);


}
