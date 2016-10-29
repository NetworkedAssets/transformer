package com.networkedassets.condoc.jsonDoclet;

import com.networkedassets.condoc.jsonDoclet.model.*;
import com.networkedassets.condoc.jsonDoclet.model.Class;
import com.networkedassets.condoc.jsonDoclet.model.Enum;
import com.networkedassets.condoc.jsonDoclet.model.Package;
import com.sun.javadoc.*;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javadoc.DocEnv;
import com.sun.tools.javadoc.DocImpl;
import com.sun.tools.javadoc.ProgramElementDocImpl;
import com.sun.tools.javadoc.TypeMaker;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

public class Parser {

    private final static Logger log = Logger.getLogger(Parser.class.getName());
    private final java.lang.reflect.Field treeField;
    private final java.lang.reflect.Field envField;

    protected Map<String, Package> packages = new TreeMap<>();
    protected Map<String, Index.IndexPackage> indexPackages = new TreeMap<>();

    protected ObjectFactory objectFactory = new ObjectFactory();

    public Parser() {
        try {
            treeField = ProgramElementDocImpl.class.getDeclaredField("tree");
            envField = DocImpl.class.getDeclaredField("env");

            treeField.setAccessible(true);
            envField.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public Root parseRootDoc(RootDoc rootDoc) {
        Root rootNode = objectFactory.createRoot();
        Index index = objectFactory.createIndex();
        rootNode.setIndex(index);

        for (ClassDoc classDoc : rootDoc.classes()) {
            PackageDoc packageDoc = classDoc.containingPackage();

            Package packageNode = packages.get(packageDoc.name());
            Index.IndexPackage indexPackage = indexPackages.get(packageDoc.name());
            if (packageNode == null) {
                packageNode = parsePackage(packageDoc);
                packages.put(packageDoc.name(), packageNode);
                rootNode.getPackage().add(packageNode);

                indexPackage = objectFactory.createIndexIndexPackage();
                indexPackage.setName(packageNode.getName());
                indexPackages.put(packageNode.getName(), indexPackage);
                index.getIndexPackage().add(indexPackage);
            }

            if (classDoc instanceof AnnotationTypeDoc) {
                Annotation a = parseAnnotationTypeDoc((AnnotationTypeDoc) classDoc);
                packageNode.getAnnotation().add(a);

                Index.IndexPackage.IndexClass clazz = objectFactory.createIndexIndexPackageIndexClass();
                clazz.setName(a.getName());
                clazz.setQualified(a.getQualified());
                clazz.setType("annotation");
                indexPackage.getIndexClass().add(clazz);
            } else if (classDoc.isEnum()) {
                Enum e = parseEnum(classDoc);
                packageNode.getEnum().add(e);

                Index.IndexPackage.IndexClass clazz = objectFactory.createIndexIndexPackageIndexClass();
                clazz.setName(e.getName());
                clazz.setQualified(e.getQualified());
                clazz.setType("enum");
                indexPackage.getIndexClass().add(clazz);
            } else if (classDoc.isInterface()) {
                Interface i = parseInterface(classDoc);
                packageNode.getInterface().add(i);

                Index.IndexPackage.IndexClass clazz = objectFactory.createIndexIndexPackageIndexClass();
                clazz.setName(i.getName());
                clazz.setQualified(i.getQualified());
                clazz.setType("interface");
                indexPackage.getIndexClass().add(clazz);
            } else {
                Class c = parseClass(classDoc);
                packageNode.getClazz().add(c);

                Index.IndexPackage.IndexClass clazz = objectFactory.createIndexIndexPackageIndexClass();
                clazz.setName(c.getName());
                clazz.setQualified(c.getQualified());
                clazz.setType(c.isException() || c.isError() ? "exception" : "class");
                indexPackage.getIndexClass().add(clazz);
            }
        }

        return rootNode;
    }

    protected Package parsePackage(PackageDoc packageDoc) {
        Package packageNode = objectFactory.createPackage();
        packageNode.setName(packageDoc.name());
        String comment = packageDoc.commentText();
        if (comment.length() > 0) {
            packageNode.setComment(comment);
        }

        for (Tag tag : packageDoc.tags()) {
            packageNode.getTag().add(parseTag(tag));
        }

        return packageNode;
    }

    protected Annotation parseAnnotationTypeDoc(AnnotationTypeDoc annotationTypeDoc) {
        Annotation annotationNode = objectFactory.createAnnotation();
        annotationNode.setName(annotationTypeDoc.name());
        annotationNode.setQualified(annotationTypeDoc.qualifiedName());
        String comment = annotationTypeDoc.commentText();
        if (comment.length() > 0) {
            annotationNode.setComment(comment);
        }
        annotationNode.setIncluded(annotationTypeDoc.isIncluded());
        annotationNode.setScope(parseScope(annotationTypeDoc));

        for (AnnotationTypeElementDoc annotationTypeElementDoc : annotationTypeDoc.elements()) {
            annotationNode.getElement().add(parseAnnotationTypeElementDoc(annotationTypeElementDoc));
        }

        for (AnnotationDesc annotationDesc : annotationTypeDoc.annotations()) {
            annotationNode.getAnnotation().add(parseAnnotationDesc(annotationDesc, annotationTypeDoc.qualifiedName()));
        }

        for (Tag tag : annotationTypeDoc.tags()) {
            annotationNode.getTag().add(parseTag(tag));
        }

        return annotationNode;
    }

    protected AnnotationElement parseAnnotationTypeElementDoc(AnnotationTypeElementDoc annotationTypeElementDoc) {
        AnnotationElement annotationElementNode = objectFactory.createAnnotationElement();
        annotationElementNode.setName(annotationTypeElementDoc.name());
        annotationElementNode.setQualified(annotationTypeElementDoc.qualifiedName());
        annotationElementNode.setType(parseTypeInfo(annotationTypeElementDoc.returnType()));
        annotationElementNode.setComment(annotationTypeElementDoc.commentText());

        AnnotationValue value = annotationTypeElementDoc.defaultValue();
        if (value != null) {
            annotationElementNode.setDefault(value.toString());
        }

        for (Tag tag : annotationTypeElementDoc.tags()) {
            annotationElementNode.getTag().add(parseTag(tag));
        }

        return annotationElementNode;
    }

    protected AnnotationInstance parseAnnotationDesc(AnnotationDesc annotationDesc, String programElement) {
        AnnotationInstance annotationInstanceNode = objectFactory.createAnnotationInstance();

        try {
            AnnotationTypeDoc annotTypeInfo = annotationDesc.annotationType();
            annotationInstanceNode.setName(annotTypeInfo.name());
            annotationInstanceNode.setQualified(annotTypeInfo.qualifiedTypeName());
        } catch (ClassCastException castException) {
            log.severe("Unable to obtain type data about an annotation found on: " + programElement);
            log.severe("Add to the classpath the class/jar that defines this annotation.");
        }

        for (AnnotationDesc.ElementValuePair elementValuesPair : annotationDesc.elementValues()) {
            AnnotationArgument annotationArgumentNode = objectFactory.createAnnotationArgument();
            annotationArgumentNode.setName(elementValuesPair.element().name());

            Type annotationArgumentType = elementValuesPair.element().returnType();
            annotationArgumentNode.setType(parseTypeInfo(annotationArgumentType));
            annotationArgumentNode.setPrimitive(annotationArgumentType.isPrimitive());
            annotationArgumentNode.setArray(annotationArgumentType.dimension().length() > 0);

            Object objValue = elementValuesPair.value().value();
            if (objValue instanceof AnnotationValue[]) {
                for (AnnotationValue annotationValue : (AnnotationValue[]) objValue) {
                    annotationArgumentNode.getValue().add(annotationValue.value().toString());
                }
            } else if (objValue instanceof FieldDoc) {
                annotationArgumentNode.getValue().add(((FieldDoc) objValue).name());
            } else if (objValue instanceof ClassDoc) {
                annotationArgumentNode.getValue().add(((ClassDoc) objValue).qualifiedTypeName());
            } else {
                annotationArgumentNode.getValue().add(objValue.toString());
            }
            annotationInstanceNode.getArgument().add(annotationArgumentNode);
        }

        return annotationInstanceNode;
    }

    protected Enum parseEnum(ClassDoc classDoc) {
        Enum enumNode = objectFactory.createEnum();
        enumNode.setName(classDoc.name());
        enumNode.setQualified(classDoc.qualifiedName());
        String comment = classDoc.commentText();
        if (comment.length() > 0) {
            enumNode.setComment(comment);
        }
        enumNode.setIncluded(classDoc.isIncluded());
        String scope = parseScope(classDoc);
        enumNode.setScope(scope);
        enumNode.getModifier().add(scope);
        enumNode.setType("enum");

        Type superClassType = classDoc.superclassType();
        if (superClassType != null) {
            enumNode.setClazz(parseTypeInfo(superClassType));
        }

        for (Type interfaceType : classDoc.interfaceTypes()) {
            enumNode.getInterface().add(parseTypeInfo(interfaceType));
        }

        for (FieldDoc field : classDoc.enumConstants()) {
            enumNode.getConstant().add(parseEnumConstant(field));
        }

        for (AnnotationDesc annotationDesc : classDoc.annotations()) {
            enumNode.getAnnotation().add(parseAnnotationDesc(annotationDesc, classDoc.qualifiedName()));
        }

        for (Tag tag : classDoc.tags()) {
            enumNode.getTag().add(parseTag(tag));
        }

        return enumNode;
    }

    protected EnumConstant parseEnumConstant(FieldDoc fieldDoc) {
        EnumConstant enumConstant = objectFactory.createEnumConstant();
        enumConstant.setName(fieldDoc.name());
        String comment = fieldDoc.commentText();
        if (comment.length() > 0) {
            enumConstant.setComment(comment);
        }

        for (AnnotationDesc annotationDesc : fieldDoc.annotations()) {
            enumConstant.getAnnotation().add(parseAnnotationDesc(annotationDesc, fieldDoc.qualifiedName()));
        }

        for (Tag tag : fieldDoc.tags()) {
            enumConstant.getTag().add(parseTag(tag));
        }

        return enumConstant;
    }

    protected Interface parseInterface(ClassDoc classDoc) {

        Interface interfaceNode = objectFactory.createInterface();
        interfaceNode.setName(classDoc.name());
        interfaceNode.setQualified(classDoc.qualifiedName());
        String comment = classDoc.commentText();
        if (comment.length() > 0) {
            interfaceNode.setComment(comment);
        }
        interfaceNode.setIncluded(classDoc.isIncluded());
        String scope = parseScope(classDoc);
        interfaceNode.setScope(scope);
        interfaceNode.setType("interface");
        interfaceNode.getModifier().add(scope);

        for (TypeVariable typeVariable : classDoc.typeParameters()) {
            interfaceNode.getGeneric().add(parseTypeParameter(typeVariable));
        }

        for (Type interfaceType : classDoc.interfaceTypes()) {
            interfaceNode.getInterface().add(parseTypeInfo(interfaceType));
        }

        for (MethodDoc method : classDoc.methods()) {
            interfaceNode.getMethod().add(parseMethod(method));
        }

        for (AnnotationDesc annotationDesc : classDoc.annotations()) {
            interfaceNode.getAnnotation().add(parseAnnotationDesc(annotationDesc, classDoc.qualifiedName()));
        }

        for (Tag tag : classDoc.tags()) {
            interfaceNode.getTag().add(parseTag(tag));
        }

        for (FieldDoc field : classDoc.fields()) {
            interfaceNode.getField().add(parseField(field));
        }

        return interfaceNode;
    }

    protected Class parseClass(ClassDoc classDoc) {

        Class classNode = objectFactory.createClass();
        classNode.setName(classDoc.name());
        classNode.setQualified(classDoc.qualifiedName());
        String comment = classDoc.commentText();
        if (comment.length() > 0) {
            classNode.setComment(comment);
        }
        classNode.setAbstract(classDoc.isAbstract());
        classNode.setError(classDoc.isError());
        classNode.setException(classDoc.isException());
        classNode.setExternalizable(classDoc.isExternalizable());
        classNode.setIncluded(classDoc.isIncluded());
        classNode.setSerializable(classDoc.isSerializable());
        String scope = parseScope(classDoc);
        classNode.setScope(scope);
        classNode.setType(classDoc.isException() || classDoc.isError() ? "exception" : "class");
        List<String> modifiers = classNode.getModifier();
        modifiers.add(scope);
        if (classDoc.isAbstract()) modifiers.add("abstract");
        if (classDoc.isStatic()) modifiers.add("static");
        if (classDoc.isFinal()) modifiers.add("final");

        for (TypeVariable typeVariable : classDoc.typeParameters()) {
            classNode.getGeneric().add(parseTypeParameter(typeVariable));
        }

        Type superClassType = classDoc.superclassType();
        if (superClassType != null && superClassType.qualifiedTypeName().equals("<any>")) {
            try {
                JCTree.JCClassDecl tree = (JCTree.JCClassDecl) treeField.get(classDoc);
                classNode.setClazz(parseTypeInfo(tree.extending, (DocEnv) envField.get(classDoc)));
            } catch (IllegalAccessException | IllegalArgumentException e) {
                classNode.setClazz(parseTypeInfo(superClassType)); // welp
            }
        } else if (superClassType != null) {
            classNode.setClazz(parseTypeInfo(superClassType));
        }

        Type[] interfaceTypes = classDoc.interfaceTypes();
        for (int i = 0; i < interfaceTypes.length; i++) {
            Type interfaceType = interfaceTypes[i];
            if (interfaceType.qualifiedTypeName().equals("<any>")) {
                try {
                    JCTree.JCClassDecl tree = (JCTree.JCClassDecl) treeField.get(classDoc);
                    classNode.getInterface().add(parseTypeInfo(tree.implementing.get(i), (DocEnv) envField.get(classDoc)));
                } catch (IllegalAccessException | IllegalArgumentException e) {
                    classNode.getInterface().add(parseTypeInfo(interfaceType)); // welp
                }
            } else {
                classNode.getInterface().add(parseTypeInfo(interfaceType));
            }
        }

        for (MethodDoc method : classDoc.methods()) {
            classNode.getMethod().add(parseMethod(method));
        }

        for (AnnotationDesc annotationDesc : classDoc.annotations()) {
            classNode.getAnnotation().add(parseAnnotationDesc(annotationDesc, classDoc.qualifiedName()));
        }

        for (ConstructorDoc constructor : classDoc.constructors()) {
            classNode.getConstructor().add(parseConstructor(constructor));
        }

        for (FieldDoc field : classDoc.fields()) {
            classNode.getField().add(parseField(field));
        }

        for (Tag tag : classDoc.tags()) {
            classNode.getTag().add(parseTag(tag));
        }

        for (ClassDoc innerClass : classDoc.innerClasses()) {
            classNode.getInnerClasses().add(innerClass.qualifiedName());
        }

        return classNode;
    }

    protected Constructor parseConstructor(ConstructorDoc constructorDoc) {
        Constructor constructorNode = objectFactory.createConstructor();

        constructorNode.setName(constructorDoc.name());
        constructorNode.setQualified(constructorDoc.qualifiedName());
        String comment = constructorDoc.commentText();
        if (comment.length() > 0) {
            constructorNode.setComment(comment);
        }
        String scope = parseScope(constructorDoc);
        constructorNode.setScope(scope);
        constructorNode.setIncluded(constructorDoc.isIncluded());
        constructorNode.setFinal(constructorDoc.isFinal());
        constructorNode.setNative(constructorDoc.isNative());
        constructorNode.setStatic(constructorDoc.isStatic());
        constructorNode.setSynchronized(constructorDoc.isSynchronized());
        constructorNode.setVarArgs(constructorDoc.isVarArgs());
        constructorNode.setSignature(constructorDoc.signature());

        List<String> modifiers = constructorNode.getModifier();
        modifiers.add(scope);
        if (constructorNode.isFinal()) modifiers.add("final");
        if (constructorNode.isSynchronized()) modifiers.add("synchronized");
        if (constructorNode.isNative()) modifiers.add("native");

        for (Parameter parameter : constructorDoc.parameters()) {
            constructorNode.getParameter().add(parseMethodParameter(parameter));
        }

        for (Type exceptionType : constructorDoc.thrownExceptionTypes()) {
            constructorNode.getException().add(parseTypeInfo(exceptionType));
        }

        for (AnnotationDesc annotationDesc : constructorDoc.annotations()) {
            constructorNode.getAnnotation().add(parseAnnotationDesc(annotationDesc, constructorDoc.qualifiedName()));
        }

        for (Tag tag : constructorDoc.tags()) {
            constructorNode.getTag().add(parseTag(tag));
        }

        return constructorNode;
    }

    protected Method parseMethod(MethodDoc methodDoc) {
        Method methodNode = objectFactory.createMethod();

        methodNode.setName(methodDoc.name());
        methodNode.setQualified(methodDoc.qualifiedName());
        String comment = methodDoc.commentText();
        if (comment.length() > 0) {
            methodNode.setComment(comment);
        }
        String scope = parseScope(methodDoc);
        methodNode.setScope(scope);
        methodNode.setAbstract(methodDoc.isAbstract());
        methodNode.setIncluded(methodDoc.isIncluded());
        methodNode.setFinal(methodDoc.isFinal());
        methodNode.setNative(methodDoc.isNative());
        methodNode.setStatic(methodDoc.isStatic());
        methodNode.setSynchronized(methodDoc.isSynchronized());
        methodNode.setVarArgs(methodDoc.isVarArgs());
        methodNode.setSignature(methodDoc.signature());
        Type retType = methodDoc.returnType();
        if (retType.qualifiedTypeName().equals("<any>")) {
            try {
                JCTree.JCMethodDecl tree = (JCTree.JCMethodDecl) treeField.get(methodDoc);
                methodNode.setReturn(parseTypeInfo(tree.restype, (DocEnv) envField.get(methodDoc)));
            } catch (IllegalAccessException | IllegalArgumentException e) {
                methodNode.setReturn(parseTypeInfo(retType)); // welp
            }
        } else {
            methodNode.setReturn(parseTypeInfo(retType));
        }


        List<String> modifiers = methodNode.getModifier();
        modifiers.add(scope);
        if (methodNode.isFinal()) modifiers.add("final");
        if (methodNode.isStatic()) modifiers.add("static");
        if (methodNode.isNative()) modifiers.add("native");
        if (methodNode.isSynchronized()) modifiers.add("synchronized");
        if (methodNode.isAbstract()) modifiers.add("abstract");

        for (Parameter parameter : methodDoc.parameters()) {
            methodNode.getParameter().add(parseMethodParameter(parameter));
        }

        for (Type exceptionType : methodDoc.thrownExceptionTypes()) {
            methodNode.getException().add(parseTypeInfo(exceptionType));
        }

        for (AnnotationDesc annotationDesc : methodDoc.annotations()) {
            methodNode.getAnnotation().add(parseAnnotationDesc(annotationDesc, methodDoc.qualifiedName()));
        }

        for (Tag tag : methodDoc.tags()) {
            methodNode.getTag().add(parseTag(tag));
        }

        return methodNode;
    }

    protected MethodParameter parseMethodParameter(Parameter parameter) {
        MethodParameter parameterMethodNode = objectFactory.createMethodParameter();
        parameterMethodNode.setName(parameter.name());
        parameterMethodNode.setType(parseTypeInfo(parameter.type()));

        for (AnnotationDesc annotationDesc : parameter.annotations()) {
            parameterMethodNode.getAnnotation().add(parseAnnotationDesc(annotationDesc, parameter.typeName()));
        }

        return parameterMethodNode;
    }

    protected Field parseField(FieldDoc fieldDoc) {
        Field fieldNode = objectFactory.createField();
        Type fieldType = fieldDoc.type();
        if (fieldType.qualifiedTypeName().equals("<any>")) {
            try {
                JCTree.JCVariableDecl tree = (JCTree.JCVariableDecl) treeField.get(fieldDoc);
                fieldNode.setType(parseTypeInfo(tree.vartype, (DocEnv) envField.get(fieldDoc)));
            } catch (IllegalAccessException | IllegalArgumentException e) {
                fieldNode.setType(parseTypeInfo(fieldType)); // welp
            }
        } else {
            fieldNode.setType(parseTypeInfo(fieldType));
        }
        fieldNode.setName(fieldDoc.name());
        fieldNode.setQualified(fieldDoc.qualifiedName());
        String comment = fieldDoc.commentText();
        if (comment.length() > 0) {
            fieldNode.setComment(comment);
        }
        String scope = parseScope(fieldDoc);
        fieldNode.setScope(scope);
        fieldNode.setFinal(fieldDoc.isFinal());
        fieldNode.setStatic(fieldDoc.isStatic());
        fieldNode.setVolatile(fieldDoc.isVolatile());
        fieldNode.setTransient(fieldDoc.isTransient());
        fieldNode.setConstant(fieldDoc.constantValueExpression());

        List<String> modifiers = fieldNode.getModifier();
        modifiers.add(scope);
        if (fieldNode.isFinal()) modifiers.add("final");
        if (fieldNode.isStatic()) modifiers.add("static");
        if (fieldNode.isTransient()) modifiers.add("transient");
        if (fieldDoc.isVolatile()) modifiers.add("volatile");

        for (AnnotationDesc annotationDesc : fieldDoc.annotations()) {
            fieldNode.getAnnotation().add(parseAnnotationDesc(annotationDesc, fieldDoc.qualifiedName()));
        }

        for (Tag tag : fieldDoc.tags()) {
            fieldNode.getTag().add(parseTag(tag));
        }

        return fieldNode;
    }

    private TypeInfo parseTypeInfo(JCTree.JCExpression restype, DocEnv env) {
        if (restype instanceof JCTree.JCTypeApply) {
            JCTree.JCTypeApply type = (JCTree.JCTypeApply) restype;
            TypeInfo typeInfo = objectFactory.createTypeInfo();
            typeInfo.setQualified(type.clazz.toString());
            for (JCTree.JCExpression arg : type.arguments) {
                typeInfo.getGeneric().add(parseTypeInfo(arg, env));
            }

            return typeInfo;
        } else if (restype instanceof JCTree.JCIdent) {
            JCTree.JCIdent type = (JCTree.JCIdent) restype;
            TypeInfo typeInfo = objectFactory.createTypeInfo();
            if (!type.type.isErroneous()) {
                return parseTypeInfo(TypeMaker.getType(env, type.type));
            } else {
                typeInfo.setQualified(type.toString());
            }

            return typeInfo;
        } else {
            throw new IllegalArgumentException("invalid restype type");
        }
    }

    protected TypeInfo parseTypeInfo(Type type) {
        TypeInfo typeInfoNode = objectFactory.createTypeInfo();
        typeInfoNode.setQualified(type.qualifiedTypeName());
        String dimension = type.dimension();
        if (dimension.length() > 0) {
            typeInfoNode.setDimension(dimension);
        }

        WildcardType wildcard = type.asWildcardType();
        if (wildcard != null) {
            typeInfoNode.setWildcard(parseWildcard(wildcard));
        }

        ParameterizedType parametrized = type.asParameterizedType();
        if (parametrized != null) {
            for (Type typeArgument : parametrized.typeArguments()) {
                typeInfoNode.getGeneric().add(parseTypeInfo(typeArgument));
            }
        }

        return typeInfoNode;
    }

    protected Wildcard parseWildcard(WildcardType wildcard) {
        Wildcard wildcardNode = objectFactory.createWildcard();

        for (Type extendType : wildcard.extendsBounds()) {
            wildcardNode.getExtendsBound().add(parseTypeInfo(extendType));
        }

        for (Type superType : wildcard.superBounds()) {
            wildcardNode.getSuperBound().add(parseTypeInfo(superType));
        }

        return wildcardNode;
    }

    protected TypeParameter parseTypeParameter(TypeVariable typeVariable) {
        TypeParameter typeParameter = objectFactory.createTypeParameter();
        typeParameter.setName(typeVariable.typeName());

        for (Type bound : typeVariable.bounds()) {
            typeParameter.getBound().add(bound.qualifiedTypeName());
        }

        return typeParameter;
    }

    protected TagInfo parseTag(Tag tagDoc) {
        TagInfo tagNode = objectFactory.createTagInfo();
        tagNode.setName(tagDoc.kind());
        tagNode.setText(tagDoc.text());
        return tagNode;
    }

    protected String parseScope(ProgramElementDoc doc) {
        if (doc.isPrivate()) {
            return "private";
        } else if (doc.isProtected()) {
            return "protected";
        } else if (doc.isPublic()) {
            return "public";
        }
        return "";
    }
}
