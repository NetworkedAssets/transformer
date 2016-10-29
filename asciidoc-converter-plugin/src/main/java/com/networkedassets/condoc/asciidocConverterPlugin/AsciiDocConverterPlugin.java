package com.networkedassets.condoc.asciidocConverterPlugin;

import com.networkedassets.condoc.transformer.common.Documentation;
import com.networkedassets.condoc.transformer.common.docitem.DocItem;
import com.networkedassets.condoc.transformer.common.docitem.HtmlDocItem;
import com.networkedassets.condoc.transformer.dispatchRawData.core.boundary.require.RawData;
import com.networkedassets.condoc.transformer.managePlugins.core.boundary.provide.ConverterPlugin;
import org.asciidoctor.AsciiDocDirectoryWalker;
import org.asciidoctor.Asciidoctor;
import org.asciidoctor.DirectoryWalker;
import org.asciidoctor.Options;

import javax.inject.Inject;
import javax.servlet.annotation.WebListener;
import java.io.File;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@WebListener
public class AsciiDocConverterPlugin implements ConverterPlugin {

    @Inject
    Asciidoctor asciidoctor;

    @Inject
    Options options;

    @Override
    public Documentation convert(RawData rawData) {
        Documentation asciiDocumentation = new Documentation()
                .setType(Documentation.DocumentationType.HTML);

        Set<DocItem> output = new HashSet<>();

        Path directory = findLocalDirectory(rawData);
        DirectoryWalker directoryWalker = new AsciiDocDirectoryWalker(directory.toString());
        List<File> asciiDocFiles = directoryWalker.scan();

        asciiDocFiles.forEach(file -> output.add(new HtmlDocItem()
                .setOriginalPath(directory.relativize(file.toPath()).toString())
                .setContent(asciidoctor.renderFile(file, options))
                .setFullName(file.getName())));

        asciiDocumentation.addDocItems(output);

        return asciiDocumentation;
    }

    @Override
    public String getIdentifier() {
        return "asciiDoc-converter";
    }

    static Path findLocalDirectory(RawData rawData) {
        return rawData.getDataPaths().stream().min(Comparator.comparing(item -> item.toString().length())).get();
    }
}
