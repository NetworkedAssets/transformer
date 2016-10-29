package com.networkedassets.condoc.umlConverterPlugin;

import com.networkedassets.condoc.transformer.common.Documentation;
import com.networkedassets.condoc.transformer.common.docitem.ClassDiagramDocItem;
import com.networkedassets.condoc.transformer.common.exceptions.TransformerException;
import com.networkedassets.condoc.transformer.dispatchRawData.core.boundary.require.RawData;
import com.networkedassets.condoc.transformer.managePlugins.core.boundary.provide.ConverterPlugin;
import com.networkedassets.condoc.umlConverterPlugin.util.PlantUML;
import com.networkedassets.condoc.umlConverterPlugin.util.PlantUMLException;
import org.json.JSONException;

import javax.servlet.annotation.WebListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@WebListener
public class UmlConverterPlugin implements ConverterPlugin {

    @Override
    public Documentation convert(RawData rawData) {
        Documentation<ClassDiagramDocItem> documentation = new Documentation<ClassDiagramDocItem>()
                .setType(Documentation.DocumentationType.CLASS_DIAGRAM);

        try {
            if (rawData.getDataPaths().isEmpty()) {
                throw new PlantUMLException("No data paths provided");
            }

            Path plantUmlPath = PlantUML.fromRawData(rawData, null, null);
            List<ClassDiagramDocItem.Relation> relations = parsePlantUmlOutput(plantUmlPath);
            Map<String, ClassDiagramDocItem.Entity> entities = EntityProcessor.process(rawData);
            relations = relations.stream()
                    .filter(r -> entities.containsKey(r.getTarget()) && entities.containsKey(r.getSource()))
                    .collect(Collectors.toList());

            ClassDiagramDocItem docItem = new ClassDiagramDocItem();
            docItem.setContent(relations, entities);
            docItem.setFullName("all");
            documentation.addDocItem(docItem);

            documentation.addDocItems(processRelationsForEveryClass(relations, entities));

        } catch (PlantUMLException | JSONException | IOException e) {
            throw new TransformerException("Couldn't generate UML", e);
        }

        return documentation;
    }

    private Collection<ClassDiagramDocItem> processRelationsForEveryClass(List<ClassDiagramDocItem.Relation> relations,
                                                                          Map<String, ClassDiagramDocItem.Entity> entities) {
        return entities.values()
                .stream()
                .map(entity -> {
                    try {
                        Map<String, ClassDiagramDocItem.Entity> es = new HashMap<>();
                        List<ClassDiagramDocItem.Relation> rs = relations.stream()
                                .filter(r -> r.getSource().equals(entity.getFullName())
                                        || r.getTarget().equals(entity.getFullName()))
                                .collect(Collectors.toList());

                        rs.forEach(r -> {
                            if (entities.containsKey(r.getSource())) {
                                es.put(r.getSource(), entities.get(r.getSource()));
                            }
                            if (entities.containsKey(r.getTarget())) {
                                es.put(r.getTarget(), entities.get(r.getTarget()));
                            }

                        });

                        es.put(entity.getFullName(), entity);

                        return (ClassDiagramDocItem) new ClassDiagramDocItem()
                                .setContent(rs, es)
                                .setFullName(entity.getFullName());
                    } catch (IOException e) {
                        throw new TransformerException(e);
                    }
                })
                .collect(Collectors.toList());
    }

    private List<ClassDiagramDocItem.Relation> parsePlantUmlOutput(Path path) throws IOException {
        return Files.lines(path)
                .filter(ClassDiagramDocItem.Relation.Type::containsAnyRelation)
                .map(PlantUML::lineToRelation)
                .collect(Collectors.toList());
    }

    @Override
    public String getIdentifier() {
        return "uml-converter";
    }

}