package com.networkedassets.condoc.umlConverterPlugin.util;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.networkedassets.condoc.transformer.common.docitem.ClassDiagramDocItem;
import com.networkedassets.condoc.transformer.common.exceptions.TransformerException;
import com.networkedassets.condoc.transformer.dispatchRawData.core.boundary.require.RawData;
import net.sourceforge.plantuml.FileFormat;
import org.apache.commons.io.FileUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.logging.Logger;

public class PlantUML {
    private static final String objectFilter = "abstract_classes,classes,extensions,implementations,imports,interfaces,native_methods,static_imports";
    Logger log = Logger.getLogger(PlantUML.class.getName());

    /**
     * Convenience method for generating plantUML for a project clone to
     * localFileDirectory
     *
     * @param rawData       containing file paths
     * @param diagramFilter the names of object which will be showing on diagram ( def.
     *                      abstract_classes,classes,extensions,implementations,
     *                      imports,interfaces,native_methods,static_imports_
     * @param fileFormat    the name of file extension
     * @return plant UML description
     * @throws PlantUMLException
     */

    @Nonnull
    public static Path fromRawData(@Nonnull RawData rawData, @Nullable String diagramFilter,
                                   @Nullable FileFormat fileFormat) throws PlantUMLException {
        Path localDirectory = findLocalDirectory(rawData);
        Preconditions.checkNotNull(localDirectory);
        PlantUML plantUML = new PlantUML();
        return plantUML.generateUmlDescription(localDirectory, diagramFilter);
    }

    private Path generateUmlDescription(@Nonnull Path localDirectory, @Nullable String diagramFilter)
            throws PlantUMLException {

        Preconditions.checkNotNull(localDirectory);
        Path results;

        try {
            File plantUmlJar = File.createTempFile("plantUml", ".jar");
            File resultFile = File.createTempFile("prefix-", "-suffix");
            URL plantUmlResourceUrl = getClass().getClassLoader()
                    .getResource("plantuml-dependency-cli-1.4.0-jar-with-dependencies.jar");
            if (plantUmlResourceUrl == null) throw new PlantUMLException("plantUml jar not found");
            FileUtils.copyURLToFile(plantUmlResourceUrl, plantUmlJar);

            Process plantUmlProcess = Runtime.getRuntime().exec(new String[]{
                    "java",
                    "-jar", plantUmlJar.getAbsolutePath(),
                    "-o", resultFile.getAbsolutePath(),
                    "-b", localDirectory.toFile().getAbsolutePath(),
                    "-dt", Strings.isNullOrEmpty(diagramFilter) ? objectFilter : diagramFilter
            });

            if (plantUmlProcess.waitFor() != 0) {
                throw new PlantUMLException("plantUml program ended with non-0 return code");
            }

            results = Paths.get(resultFile.getAbsolutePath());

        } catch (InterruptedException e) {
            throw new PlantUMLException("interrupted", e);
        } catch (IOException e) {
            throw new PlantUMLException("general I/O exception", e);
        }
        return results;
    }

    public static Path findLocalDirectory(RawData rawData) {
        return rawData.getDataPaths()
                .stream()
                .min(Comparator.comparing(item -> item.toString().length()))
                .orElseThrow(TransformerException::new);
    }

    public static ClassDiagramDocItem.Relation lineToRelation(String line) {
        String[] parts = line.split(" ");
        return new ClassDiagramDocItem.Relation(parts[0], parts[2], ClassDiagramDocItem.Relation.Type.fromDescription(parts[1]));
    }
}
