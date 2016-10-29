package com.networkedassets.condoc.directorySourcePlugin;

import com.google.common.collect.ImmutableList;
import com.networkedassets.condoc.transformer.common.SourceNodeIdentifier;
import com.networkedassets.condoc.transformer.common.SourceUnit;
import com.networkedassets.condoc.transformer.dispatchRawData.core.boundary.DefaultRawData;
import com.networkedassets.condoc.transformer.dispatchRawData.core.boundary.require.RawData;
import com.networkedassets.condoc.transformer.managePermissions.core.entity.SourceStructureRootNode;
import com.networkedassets.condoc.transformer.managePlugins.core.boundary.provide.SourcePlugin;
import com.networkedassets.condoc.transformer.manageSources.core.entity.Source;
import com.networkedassets.condoc.transformer.manageSources.core.entity.SourceSettingField;
import com.networkedassets.condoc.transformer.util.functional.Optionals;

import javax.servlet.annotation.WebListener;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@WebListener
public class DirectorySourcePlugin implements SourcePlugin {
    @Override
    public String getIdentifier() {
        return "directory-source-plugin";
    }

    @Override
    public Optional<RawData> fetchRawData(SourceNodeIdentifier sourceNodeIdentifier, Source source) {
        return Optionals.ofThrowing(() ->
                DefaultRawData.of(Paths.get(source.getSettingByKeyOrThrow("path").getValue()))
        );
    }

    @Override
    public List<SourceSettingField> getRequiredSettingsParameters() {
        return ImmutableList.of(new SourceSettingField().setName("path").setType(SourceSettingField.Type.TEXT));
    }

    @Override
    public VerificationStatus verify(Source source) {
        return Files.isDirectory(Paths.get(source.getSettingByKeyOrThrow("path").getValue()))
                ? VerificationStatus.OK
                : VerificationStatus.SOURCE_NOT_FOUND;
    }

    @Override
    public SourceStructureRootNode fetchStructureForSource(Source source) {
        SourceStructureRootNode sourceStructureRootNode = new SourceStructureRootNode(source.getId());
        sourceStructureRootNode.addChildren(
                new SourceUnit(new SourceNodeIdentifier(source.getId(),
                        source.getSettingByKeyOrThrow("path").getValue()))
        );
        return sourceStructureRootNode;
    }
}
