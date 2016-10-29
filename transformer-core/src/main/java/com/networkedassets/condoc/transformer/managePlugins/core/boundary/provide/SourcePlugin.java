package com.networkedassets.condoc.transformer.managePlugins.core.boundary.provide;

import com.networkedassets.condoc.transformer.common.SourceNodeIdentifier;
import com.networkedassets.condoc.transformer.dispatchRawData.core.boundary.require.RawData;
import com.networkedassets.condoc.transformer.managePermissions.core.entity.SourceStructureRootNode;
import com.networkedassets.condoc.transformer.manageSources.core.entity.Source;
import com.networkedassets.condoc.transformer.manageSources.core.entity.SourceSettingField;

import java.util.List;
import java.util.Optional;

public interface SourcePlugin extends Plugin {
    /**
     * Downloads the data from the source unit, using provided settings, saves them in the filesystem and
     * returns an object containing paths to this data
     *
     * @param sourceNodeIdentifier specifies the unit from which the data will be downloaded
     * @param source               settings allowing access to the source unit
     * @return object containing paths to downloaded data
     */
    Optional<RawData> fetchRawData(SourceNodeIdentifier sourceNodeIdentifier, Source source);

    /**
     * Plugin must provide names (keys) of the parameters that should be provided to it in sourcePluginSettings
     * in order to fetch the data from the source. It's usually the login, password etc
     * <p>
     * These parameters should be retrieved from the Administrator during configuration of SourceUnits
     *
     * @return the list of strings, being the configuration parameter keys
     */
    List<SourceSettingField> getRequiredSettingsParameters();

    VerificationStatus verify(Source source);

    /**
     * Fetches complete tree of SourceNodes, including SourceUnits.
     *
     * @return SourceNode Tree root
     * @param source for which structure will be fetched
     */
    SourceStructureRootNode fetchStructureForSource(Source source);

    enum VerificationStatus {
        OK, SOURCE_NOT_FOUND, WRONG_CREDENTIALS
    }
}
