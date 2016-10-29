package com.networkedassets.condoc.transformer.dispatchRawData.core.boundary.require;

import java.nio.file.Path;
import java.util.Set;

/*
 * DANGER, DANGER: if you change anything in this interface, make sure not to
 * break JavadocConverterPluginToolsJarWrapper (from the javadoc plugin) by accident
 */
public interface RawData {
    Set<Path> getDataPaths();
}
