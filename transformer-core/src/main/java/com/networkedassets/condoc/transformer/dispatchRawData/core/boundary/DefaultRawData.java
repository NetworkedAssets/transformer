package com.networkedassets.condoc.transformer.dispatchRawData.core.boundary;

import com.networkedassets.condoc.transformer.dispatchRawData.core.boundary.require.RawData;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class DefaultRawData implements RawData {
    private Set<Path> paths = new HashSet<>();

    public static DefaultRawData of(Path... paths) {
        DefaultRawData defaultRawData = new DefaultRawData();
        defaultRawData.add(paths);
        return defaultRawData;
    }

    public void add(Path... paths) {
        this.paths.addAll(Arrays.asList(paths));
    }

    @Override
    public Set<Path> getDataPaths() {
        return paths;
    }
}
