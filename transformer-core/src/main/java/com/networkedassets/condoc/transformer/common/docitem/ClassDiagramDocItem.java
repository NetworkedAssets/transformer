package com.networkedassets.condoc.transformer.common.docitem;

import com.auth0.jwt.internal.com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.google.common.collect.Maps;
import com.networkedassets.condoc.transformer.common.Documentation;

import javax.annotation.Nullable;
import javax.persistence.Column;
import javax.persistence.Entity;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
public class ClassDiagramDocItem extends DocItem {
    private String originalPath = "";

    @JsonView(HtmlDocItem.Views.Content.class)
    @Column(columnDefinition = "TEXT")
    @JsonSerialize(using = RawStringSerializer.class)
    private String content = "";

    public String getContent() {
        return content;
    }

    public ClassDiagramDocItem setContent(String content) {
        this.content = content;
        return this;
    }

    public ClassDiagramDocItem setContent(List<ClassDiagramDocItem.Relation> relations,
                                          Map<String, ClassDiagramDocItem.Entity> entities) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> obj = new HashMap<>();
        obj.put("entities", entities);
        obj.put("relations", relations);
        content = mapper.writeValueAsString(obj);
        return this;
    }

    public String getOriginalPath() {

        return originalPath;
    }

    public ClassDiagramDocItem setOriginalPath(String originalPath) {
        this.originalPath = originalPath;
        return this;
    }

    @Override
    public Documentation.DocumentationType getDocItemType() {
        return Documentation.DocumentationType.of(ClassDiagramDocItem.class);
    }

    @Override
    public String getShortName() {
        return fullName;
    }

    public interface Views {
        interface Content {
        }
    }

    public static final class Relation {

        private final String source;
        private final Type type;
        private final String target;

        public Relation(String source, String target, Type type) {
            this.source = source;
            this.type = type;
            this.target = target;
        }

        public String getSource() {
            return source;
        }

        public Type getType() {
            return type;
        }

        public String getTarget() {
            return target;
        }

        @Override
        public String toString() {
            return "Relation{" +
                    "source='" + source + '\'' +
                    ", type='" + type + '\'' +
                    ", target='" + target + '\'' +
                    '}';
        }

        public enum Type {

            association("-->"),
            dependency("..>"),
            aggregation("o--"),
            composition("*--"),
            generalization("--|>"),
            realisation("..|>");

            private static final Map<String, Type> LOOKUP = Maps.uniqueIndex(Arrays.asList(Type.values()),
                    Type::getDescription);

            private final String value;

            Type(String value) {
                this.value = value;
            }

            public String getDescription() {

                return this.value;
            }

            @Nullable
            public static Type fromDescription(String value) {
                return LOOKUP.get(value);
            }

            public static boolean containsAnyRelation(String line) {
                return line.contains("..") || line.contains("--");
            }

        }

    }

    public static final class Entity {

        private final String fullName;
        private final String name;
        private final List<Member> fields;
        private final List<Member> methods;
        private final String entityType;
        private final boolean abstract_;
        private final boolean static_;

        public Entity(String fullName, String name, List<Member> fields, List<Member> methods,
                      String entityType, boolean abstract_, boolean static_) {
            this.fullName = fullName;
            this.name = name;
            this.fields = fields;
            this.methods = methods;
            this.entityType = entityType;
            this.abstract_ = abstract_;
            this.static_ = static_;
        }

        public String getFullName() {
            return fullName;
        }

        public String getName() {
            return name;
        }

        public List<Member> getFields() {
            return fields;
        }

        public List<Member> getMethods() {
            return methods;
        }

        public String getEntityType() {
            return entityType;
        }

        public boolean isAbstract() {
            return abstract_;
        }

        public boolean isStatic() {
            return static_;
        }

        public final static class Member {

            private String string;
            private final Scope scope;
            private final boolean abstract_;
            private final boolean static_;

            public Member(Scope scope, String string, boolean static_, boolean abstract_) {
                this.abstract_ = abstract_;
                this.static_ = static_;
                this.scope = scope;
                this.string = string;
            }

            public String getString() {
                return string;
            }

            public boolean isAbstract() {
                return abstract_;
            }

            public boolean isStatic() {
                return static_;
            }

            public Scope getScope() {
                return scope;
            }

        }

        public enum Scope {

            PRIVATE("-"),
            PROTECTED("#"),
            PACKAGE_PRIVATE("~"),
            PUBLIC("+");

            private String symbol;

            Scope(String symbol) {
                this.symbol = symbol;
            }

            public String getSymbol() {
                return symbol;
            }

        }

    }

    private static class RawStringSerializer extends StdSerializer<String> {

        @SuppressWarnings("unused") // used by @JsonSerialize
        public RawStringSerializer() {
            this(null);
        }

        public RawStringSerializer(Class<String> t) {
            super(t);
        }

        @Override
        public void serialize(String s, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            jsonGenerator.writeRawValue(s);
        }

    }
}
