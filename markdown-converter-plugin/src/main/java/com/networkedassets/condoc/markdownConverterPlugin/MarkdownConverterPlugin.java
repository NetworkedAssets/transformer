package com.networkedassets.condoc.markdownConverterPlugin;

import com.networkedassets.condoc.transformer.common.Documentation;
import com.networkedassets.condoc.transformer.common.docitem.HtmlDocItem;
import com.networkedassets.condoc.transformer.dispatchRawData.core.boundary.require.RawData;
import com.networkedassets.condoc.transformer.managePlugins.core.boundary.provide.ConverterPlugin;
import org.pegdown.PegDownProcessor;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.annotation.WebListener;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.stream.Collectors;

@WebListener
public class MarkdownConverterPlugin implements ConverterPlugin {

    @Inject
    @Named("markdown-converter")
    PegDownProcessor pegDownProcessor;

    private Path startDirectoryPath;

    @Override
    public Documentation convert(RawData rawData) {
        Documentation documentation = new Documentation()
                .setType(Documentation.DocumentationType.HTML);

        startDirectoryPath = findLocalDirectory(rawData);

        try {
            searchAndConvertMarkdownFiles(new File(startDirectoryPath.toString()), documentation);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return documentation;
    }

    private void searchAndConvertMarkdownFiles(File file, Documentation documentation) throws IOException {
        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                searchAndConvertMarkdownFiles(f, documentation);
            }
        } else if (file.isFile() && file.getName().endsWith(".md")) {
            String content = Files.lines(file.toPath(), StandardCharsets.UTF_8).collect(Collectors.joining("\n"));
            HtmlDocItem htmlDocItem = new HtmlDocItem();
            htmlDocItem.setContent(pegDownProcessor.markdownToHtml(content.toCharArray()));
            htmlDocItem.setOriginalPath(startDirectoryPath.relativize(file.toPath()).toString());
            htmlDocItem.setFullName(file.getName());
            documentation.addDocItem(htmlDocItem);
        }
    }

    @Override
    public String getIdentifier() {
        return "markdown-converter";
    }

    public static Path findLocalDirectory(RawData rawData) {
        return rawData.getDataPaths().stream().min(Comparator.comparing(item -> item.toString().length())).get();
    }
}
