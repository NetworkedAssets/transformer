package com.networkedassets.condoc.javadocConverterPlugin.util;

import com.networkedassets.condoc.jsonDoclet.JsonDoclet;
import com.networkedassets.condoc.jsonDoclet.model.Root;
import com.networkedassets.condoc.transformer.dispatchRawData.core.boundary.require.RawData;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.net.URL;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Javadoc {
    private List<Path> sourceFiles;
    private Path javadocDirectory;

    /**
     * Constructor with empty file list
     *
     * @param javadocDirectory directory where the docs are going to be located
     */
    public Javadoc(Path javadocDirectory) {
        this.javadocDirectory = javadocDirectory;
        this.sourceFiles = new ArrayList<>();
    }

    /**
     * Convenience method for generating javadoc for a directory found in rawData and it's
     * subdirectories recursively
     *
     * @param rawData containing file paths
     * @throws JavadocException
     */

    @Nonnull
    public static Root structureFromRawData(@Nonnull RawData rawData) throws JavadocException {
        Path localDirectory = findLocalDirectory(rawData);
        Path javadocPath = localDirectory.resolve("javadoc");
        Javadoc javadoc = new Javadoc(javadocPath);
        List<Path> javaFiles = searchJavaFiles(localDirectory);
        javadoc.addFiles(javaFiles);
        return javadoc.generate(JsonDoclet.class);
    }

    @Nonnull
    public static List<Path> searchJavaFiles(@Nonnull Path localDirectory) throws JavadocException {
        try (Stream<Path> javaFiles = Files.walk(localDirectory, FileVisitOption.FOLLOW_LINKS)
                .filter(p -> p.toString().endsWith(".java"))) {
            return javaFiles.collect(Collectors.toList());
        } catch (IOException e) {
            throw new JavadocException("Could not search files", e);
        }
    }

    /**
     * Generates the javadoc

     * @param docletClass
     * @throws JavadocException
     */
    public Root generate(Class<?> docletClass) throws JavadocException {
        return JavadocRunner.executeJavadoc(docletClass, null, null, null,
                sourceFiles.stream().map(Path::toString).collect(Collectors.toList()), null);
    }

    /**
     * Adds the files that should be looked at during javadoc's generation
     *
     * @param javaFiles collection of files to be added
     */
    public void addFiles(Iterable<Path> javaFiles) {
        for (Path f : javaFiles) {
            addFile(f);
        }
    }

    /**
     * Adds the file to the list of java source files used to generate javadoc
     *
     * @param javaFile file that should be added
     */
    public void addFile(Path javaFile) {
        sourceFiles.add(javaFile);
    }

    public static Path findLocalDirectory(RawData rawData) {
        return rawData.getDataPaths().stream().min(Comparator.comparing(item -> item.toString().length())).get();
    }
}
