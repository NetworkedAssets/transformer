package com.networkedassets.condoc.umlConverterPlugin;

import com.networkedassets.condoc.transformer.common.docitem.ClassDiagramDocItem;
import com.networkedassets.condoc.transformer.dispatchRawData.core.boundary.require.RawData;
import com.thoughtworks.qdox.JavaProjectBuilder;
import com.thoughtworks.qdox.model.*;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.networkedassets.condoc.transformer.common.docitem.ClassDiagramDocItem.Entity.Scope.*;

abstract class EntityProcessor {

    static Map<String, ClassDiagramDocItem.Entity> process(RawData rawData) {
        final JavaProjectBuilder projectBuilder = new JavaProjectBuilder();
        rawData.getDataPaths().forEach(path -> projectBuilder.addSourceTree(path.toFile()));

        return projectBuilder.getClasses()
                .stream()
                .map(clazz -> {
                    final String type;
                    final List<ClassDiagramDocItem.Entity.Member> fields;
                    final List<ClassDiagramDocItem.Entity.Member> methods;

                    fields = clazz.getFields().
                            stream()
                            .map(EntityProcessor::processMember)
                            .collect(Collectors.toList());
                    methods = clazz.getMethods()
                            .stream()
                            .map(EntityProcessor::processMember)
                            .collect(Collectors.toList());
                    clazz.getConstructors()
                            .stream()
                            .map(EntityProcessor::processMember)
                            .collect(Collectors.toCollection(() -> methods));

                    if (clazz.isInterface()) {
                        type = "interface";
                    } else if (clazz.isEnum()) {
                        type = "enum";
                    } else {
                        type = "class";
                    }

                    return new ClassDiagramDocItem.Entity(clazz.getFullyQualifiedName(), clazz.getName(), fields,
                            methods, type, clazz.isAbstract(), clazz.isStatic());
                })
                .collect(Collectors.toMap(ClassDiagramDocItem.Entity::getFullName, Function.identity()));
    }

    private static ClassDiagramDocItem.Entity.Member processMember(JavaMember member) {
        return new ClassDiagramDocItem.Entity.Member(scopeFromModifiers(member.getModifiers()),
                generateString(member), member.isStatic(), member.isAbstract());
    }

    private static String generateString(JavaMember member) {
        String string = scopeFromModifiers(member.getModifiers()).getSymbol()
                + " " + member.getName();
        if (member instanceof JavaMethod) {
            string += parametersToString(((JavaMethod) member).getParameters())
                    + ": " + ((JavaMethod) member).getReturnType().getGenericValue();
        } else if (member instanceof JavaConstructor) {
            string += parametersToString(((JavaConstructor) member).getParameters());
        } else if (member instanceof JavaField) {
            string += ": " + ((JavaField) member).getType().getGenericValue();
        }
        return string;
    }

    private static String parametersToString(List<JavaParameter> parameters) {
        return "(" + parameters.stream()
                .map(p -> p.getName() + ": " + p.getType().getGenericValue())
                .collect(Collectors.joining(", ")) + ")";
    }

    private static ClassDiagramDocItem.Entity.Scope scopeFromModifiers(List<String> modifiers) {
        if (modifiers.contains("private")) {
            return PRIVATE;
        } else if (modifiers.contains("public")) {
            return PUBLIC;
        } else if (modifiers.contains("protected")) {
            return PROTECTED;
        } else {
            return PACKAGE_PRIVATE;
        }
    }

}
