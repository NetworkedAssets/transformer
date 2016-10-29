@file:JvmName("OmniDocAdders")
@file:Suppress("HasPlatformType")

package com.networkedassets.condoc.javadocConverterPlugin

import com.networkedassets.condoc.jsonDoclet.model.*
import com.networkedassets.condoc.jsonDoclet.model.Annotation
import com.networkedassets.condoc.jsonDoclet.model.Enum
import com.networkedassets.condoc.transformer.common.Documentation
import com.networkedassets.condoc.transformer.common.docitem.OmniDocItem

fun Documentation<OmniDocItem>.addPackageDocItem(p: Package) = addDocItem(omniDocItem {
    fullName = p.name ?: ""
    locationPath = makePath(fullName)
    isInlined = false
    displayName = fullName
    shortName = fullName.lastPartOfFullName
    itemType = "package"

    docText = makeDocText(p.comment, p.tag, fullName)
    excerpt = makeExcerpt(docText)

//     Category DSL explanation and example
//     ====================================
//
//     category("foo") {
//         +"bar"
//         +"baz"
//     }
//
//     The code above adds the category named 'foo' to the receiver OmniDocItem, and then adds items 'bar' and 'baz'
//     to the newly created category. Keep in mind that we are currently inside a code block with a receiver - that
//     means that the code block behaves as if it were a method of the receiver type. The above example would look like
//     this in a block without a receiver:
//
//     val docItem = OmniDocItem()
//     docItem.category("foo") {
//         +"bar"
//         +"baz"
//     }

    if (p.enum.isNotEmpty()) {
        category("enums") {
            p.enum.forEach {
                +it.qualified
                addEnumDocItem(it)
            }
        }
    }

    if (p.`interface`.isNotEmpty()) {
        category("interfaces") {
            p.`interface`.forEach {
                +it.qualified
                addInterfaceDocItem(it)
            }
        }
    }

    if (p.clazz.isNotEmpty()) {
        category("classes") {
            p.clazz.forEach {
                +it.qualified
                addClassDocItem(it)
            }
        }
    }

    if (p.annotation.isNotEmpty()) {
        category("annotations") {
            p.annotation.forEach {
                +it.qualified
                addAnnotationDocItem(it)
            }
        }
    }
})

fun Documentation<OmniDocItem>.addAnnotationDocItem(a: Annotation) = addDocItem(omniDocItem {
    fullName = a.qualified
    locationPath = makePath(fullName)
    isInlined = false
    displayName = a.name
    shortName = a.name

    metadata.addAll(a.annotation.asMetadata())

    if (!a.scope.isNullOrEmpty())
        modifiers.add(a.scope)
    modifiers.addAll(a.modifier)

    itemType = "annotation"

    docText = makeDocText(a.comment, a.tag, fullName)
    excerpt = makeExcerpt(docText)

    if (a.element.isNotEmpty()) {
        category("annotation parameters") {
            a.element.forEach {
                +(it.qualified.pathPartOfFullName + "#" + it.qualified.lastPartOfFullName)
                addAnnotationParameterDocItem(it)
            }
        }
    }
})

fun Documentation<OmniDocItem>.addAnnotationParameterDocItem(ap: AnnotationElement) = addDocItem(omniDocItem {
    fullName = ap.qualified
    locationPath = makePath(fullName)
    isInlined = true
    if (ap.default != null)
        displayName = "${ap.type.asStringWithRefs()} ${ap.name} default ${ap.default}"
    else
        displayName = "${ap.type.asStringWithRefs()} ${ap.name}"
    shortName = ap.name

    itemType = "annotation parameter"

    docText = makeDocText(ap.comment, ap.tag, fullName)
    excerpt = makeExcerpt(docText)
})

fun Documentation<OmniDocItem>.addClassDocItem(c: Class) = addDocItem(omniDocItem {
    fullName = c.qualified
    locationPath = makePath(fullName)
    isInlined = false

    displayName = c.name +
            c.generic?.map {
                "&lt;" + it.name + it.bound.map { it.omniDocRefIfPossible() }
                        .joinToString(separator = " &amp; ", prefix = " extends ") + "&gt;"
            }?.joinToString(separator = ", ")
    shortName = c.name

    metadata.addAll(c.annotation.asMetadata())

    modifiers.addAll(c.modifier)

    itemType = "class"

    if (c.clazz != null) {
        relation("extends") {
            +c.clazz.asStringWithRefs()
        }
    }

    if (c.`interface`.isNotEmpty()) {
        relation("implements") {
            c.`interface`.forEach {
                +it.asStringWithRefs()
            }
        }
    }

    docText = makeDocText(c.comment, c.tag, fullName)
    excerpt = makeExcerpt(docText)

    if (c.innerClasses.isNotEmpty()) {
        category("nested classes") {
            c.innerClasses.forEach { +it }
        }
    }

    if (c.field.isNotEmpty()) {
        category("fields") {
            c.field.forEach {
                +(it.qualified.pathPartOfFullName + "#" + it.qualified.lastPartOfFullName)
                addFieldDocItem(it)
            }
        }
    }

    if (c.constructor.isNotEmpty()) {
        category("constructors") {
            c.constructor.forEach {
                +(it.qualified + "#${it.name}" + it.signature)
                addConstructorDocItem(it)
            }
        }
    }

    if (c.method.isNotEmpty()) {
        category("methods") {
            c.method.forEach {
                +(it.qualified.pathPartOfFullName + "#" + it.qualified.lastPartOfFullName + it.signature)
                addMethodDocItem(it)
            }
        }
    }
})

fun Documentation<OmniDocItem>.addConstructorDocItem(c: Constructor) = addDocItem(omniDocItem {
    fullName = c.qualified + "#${c.name}" + c.signature
    locationPath = makePath(fullName)
    isInlined = true
    displayName = c.name // + buildSignatureWithRefs(c.parameter)
    shortName = c.name
    declarationInCode = c.name + buildSignatureWithRefs(c.parameter)

    functionParameters.addAll(c.parameter.map(::methodParameterToParameterString))

    metadata.addAll(c.annotation.asMetadata())

    modifiers.addAll(c.modifier)

    itemType = "constructor"

    if (c.exception.isNotEmpty()) {
        relation("throws") {
            c.exception.forEach {
                +it.asStringWithRefs()
            }
        }
    }

    docText = makeDocText(c.comment, c.tag, fullName)
    excerpt = makeExcerpt(docText)
})

fun Documentation<OmniDocItem>.addFieldDocItem(f: Field) = addDocItem(omniDocItem {
    fullName = f.qualified.pathPartOfFullName + "#" + f.qualified.lastPartOfFullName
    locationPath = makePath(fullName)
    isInlined = true
    displayName = /* f.type.asStringWithRefs() + " " + */ f.name
    shortName = f.name
    declarationInCode = f.type.asStringWithRefs() + " " + f.name

    functionReturnType = f.type.asStringWithRefs()

    metadata.addAll(f.annotation.asMetadata())

    modifiers.addAll(f.modifier)

    itemType = "field"

    docText = makeDocText(f.comment, f.tag, fullName)
    excerpt = makeExcerpt(docText)
})

fun Documentation<OmniDocItem>.addMethodDocItem(m: Method) = addDocItem(omniDocItem {
    fullName = m.qualified.pathPartOfFullName + "#" + m.qualified.lastPartOfFullName + m.signature
    locationPath = makePath(fullName)
    isInlined = true
    displayName = /* m.`return`.asStringWithRefs() + " " + */ m.name /* + buildSignatureWithRefs(m.parameter) */
    shortName = m.name
    declarationInCode = m.`return`.asStringWithRefs() + " " + m.name + buildSignatureWithRefs(m.parameter)

    functionReturnType = m.`return`.asStringWithRefs()
    functionParameters.addAll(m.parameter.map(::methodParameterToParameterString))

    metadata.addAll(m.annotation.asMetadata())

    modifiers.addAll(m.modifier)

    itemType = "method"

    if (m.exception.isNotEmpty()) {
        relation("throws") {
            m.exception.forEach {
                +it.asStringWithRefs()
            }
        }
    }

    docText = makeDocText(m.comment, m.tag, fullName)
    excerpt = makeExcerpt(docText)
})

fun Documentation<OmniDocItem>.addInterfaceDocItem(i: Interface) = addDocItem(omniDocItem {
    fullName = i.qualified
    locationPath = makePath(fullName)
    isInlined = false
    displayName = i.name +
            i.generic?.map {
                "<" + it.name + it.bound.map { it.omniDocRefIfPossible() }
                        .joinToString(separator = " ", prefix = " ") + ">"
            }?.joinToString(separator = " ", prefix = "")
    shortName = i.name

    metadata.addAll(i.annotation.asMetadata())

    modifiers.addAll(i.modifier)

    itemType = "interface"

    if (i.`interface`.isNotEmpty()) {
        relation("extends") {
            i.`interface`.forEach {
                +it.asStringWithRefs()
            }
        }
    }

    docText = makeDocText(i.comment, i.tag, fullName)
    excerpt = makeExcerpt(docText)

    if (i.field.isNotEmpty()) {
        category("fields") {
            i.field.forEach {
                +(it.qualified.pathPartOfFullName + "#" + it.qualified.lastPartOfFullName)
                addFieldDocItem(it)
            }
        }
    }

    if (i.method.isNotEmpty()) {
        category("methods") {
            i.method.forEach {
                +(it.qualified.pathPartOfFullName + "#" + it.qualified.lastPartOfFullName + it.signature)
                addMethodDocItem(it)
            }
        }
    }
})

fun Documentation<OmniDocItem>.addEnumDocItem(e: Enum) = addDocItem(omniDocItem {
    fullName = e.qualified
    locationPath = makePath(fullName)
    isInlined = false
    displayName = e.name
    shortName = e.name

    metadata.addAll(e.annotation.asMetadata())

    modifiers.addAll(e.modifier)

    itemType = "enum"

    if (e.clazz != null) {
        relation("extends") {
            +e.clazz.asStringWithRefs()
        }
    }

    if (e.`interface`.isNotEmpty()) {
        relation("implements") {
            e.`interface`.forEach {
                +it.asStringWithRefs()
            }
        }
    }

    docText = makeDocText(e.comment, e.tag, fullName)
    excerpt = makeExcerpt(docText)

    if (e.constant.isNotEmpty()) {
        category("enum constants") {
            e.constant.forEach {
                +"$fullName#${it.name}"
                addEnumConstantDocItem(it, fullName)
            }
        }
    }
})

fun Documentation<OmniDocItem>.addEnumConstantDocItem(ec: EnumConstant, enumName: String) = addDocItem(omniDocItem {
    fullName = "$enumName#${ec.name}"
    locationPath = makePath(fullName)
    isInlined = true
    displayName = ec.name
    shortName = ec.name

    metadata.addAll(ec.annotation.asMetadata())

    itemType = "enum constant"

    docText = makeDocText(ec.comment, ec.tag, fullName)
    excerpt = makeExcerpt(docText)

})

fun makeExcerpt(docText: String) = docText.lineSequence().first().split("<(p|P)>".toRegex())[0]

fun makeDocText(comment: String?, tags: List<TagInfo>?, contextFullName: String): String {
    @Suppress("NAME_SHADOWING")
    val tags = tags ?: listOf()
    return processLinks(comment ?: "", contextFullName) + tags.joinToString(separator = "\n", prefix = "\n") {
        val name = it.name.removePrefix("@").capitalize()
        val text = if (name == "See" && isJavaItemReference(it.text)) it.text.omniDocRef else processLinks(it.text, contextFullName)
        "<p><b>$name:</b> $text<br></p>"
    }.trim()
}

fun isJavaItemReference(text: String): Boolean {
    return Regex("""([a-zA-Z]+[a-zA-Z0-9]*\.)*([a-zA-Z]+[a-zA-Z0-9]*)(<.*>)?(#.*)?""").matches(text)
}

fun processLinks(s: String, contextFullName: String) = Regex("""\{@link\s*([^\s#{}]*(#[^\s(){}]+(\([^(){}]*\))?)?)(\s*([^{}]*))?\}""").replace(s) {
    val contextClass = contextFullName.split('#')[0]
    val ref = it.groupValues[1]
    val resolvedRef = if (ref.startsWith('#')) contextClass + ref else ref
    val label = it.groupValues[5]
    val labelOrRef = if (label.isNullOrEmpty()) ref else label
    OmniDocItem.makeRef(resolvedRef, labelOrRef)
}

val String.omniDocRef: String
    get() {
        if (contains("<") && contains(">")) {
            val groupValues = """([^<>]*)<(.*)>$""".toRegex().matchEntire(this)?.groupValues
            if (groupValues == null) {
                println("uh-oh")
            }
            val namePart = groupValues!![1]
            val genericPart = groupValues[2]
            return namePart.omniDocRefIfPossible() + "&lt;${genericPart.omniDocRefIfPossible()}&gt;"
        } else {
            return OmniDocItem.makeRef(this, this.lastPartOfFullName)
        }
    }

val String.lastPartOfFullName: String
    get() = this.split(delimiters = ".").lastOrNull() ?: ""

val String.pathPartOfFullName: String
    get() = this.split(delimiters = ".").dropLast(1).joinToString(".")

fun buildSignatureWithRefs(params: List<MethodParameter>) = params.joinToString(
        prefix = "(", separator = ", ", postfix = ")",
        transform = ::methodParameterToParameterString
)

fun methodParameterToParameterString(mp: MethodParameter): String {
    var annotation = mp.annotation.asMetadata().joinToString(separator = " ")
    if (annotation.isNotEmpty()) annotation += " "
    val type = mp.type.asStringWithRefs()
    val name = mp.name
    return "$annotation$type $name"
}

fun String.requiresToInsertRefs() = contains(".")

fun String.omniDocRefIfPossible() = if (requiresToInsertRefs()) omniDocRef else this

// FIXME: - json doclet doesn't return parameters for eg. Path annotation
fun List<AnnotationInstance>.asMetadata(): Collection<String> = map { it.asMetadata() }

fun AnnotationInstance.asMetadata(): String {
    val args = argument.map { it.asMetadataArgument() }.joinToString(", ")

    if (args.isEmpty())
        return "@${qualified.omniDocRefIfPossible()}"
    else
        return "@${qualified.omniDocRefIfPossible()}($args)"
}

fun AnnotationArgument.asMetadataArgument(): String {
    val argValue = if (isArray) {
        if (value.isEmpty())
            "{}"
        else
            "{ " + value.mapIndexed { n, s -> processAnnotationArgumentNthValue(n) }
                    .joinToString(", ") + " }"
    } else {
        processAnnotationArgumentNthValue(0)
    }

    return "$name = $argValue"
}

private fun AnnotationArgument.processAnnotationArgumentNthValue(n: Int): String {
    val argValue = value[n]
    if (type.qualified == "java.lang.String") return """"$argValue""""
    if (type.qualified == "java.lang.Class") return """${argValue.omniDocRef}.class"""
    if (isPrimitive && type.qualified == "char") {
        val char = argValue.toInt().toChar()
        return if (char.isISOControl())
            "'\\u${java.lang.Integer.toHexString(char.toInt()).padStart(4, '0')}'"
        else
            "'$char'"
    }
    if (!isPrimitive) { // not primitive, not class, not String... enum?
        val valueRef = OmniDocItem.makeRef("${type.qualified}#$argValue", "$argValue")
        return """${type.qualified.omniDocRef}.$valueRef"""
    }
    return argValue // primitive, not char
}

fun TypeInfo.asStringWithRefs(): String {
    // TODO: test it for something with wildcards
    var result = qualified.omniDocRef
    if (dimension != null) result += dimension
    if (wildcard != null) {
        result += if (wildcard.extendsBound.isNotEmpty())
            wildcard.extendsBound.map { "extends " + it.asStringWithRefs() }.joinToString(", ", " ") else ""
        result += if (wildcard.superBound.isNotEmpty())
            wildcard.superBound.map { "super " + it.asStringWithRefs() }.joinToString(", ", " ") else ""
    }
    if (generic.isNotEmpty()) {
        result += generic.map { it.asStringWithRefs() }.joinToString(", ", "&lt;", "&gt;")
    }
    return result
}

fun makePath(fullName: String): List<String> {
    val pathAndMethod = fullName.split('#')

    val packageAndClass = pathAndMethod[0].split('.')
    val method = pathAndMethod.getOrNull(1)

    val pathAccumulator = StringBuilder()
    val res = mutableListOf<String>()

    packageAndClass.forEachIndexed { i, pathPart ->
        pathAccumulator.append(if (i == 0) pathPart else ".$pathPart")
        res += """<omni-doc ref="$pathAccumulator">$pathPart</omni-doc>"""
    }

    if (method != null) {
        pathAccumulator.append("#$method")
        res += """<omni-doc ref="$pathAccumulator">$method</omni-doc>"""
    }

    return res
}