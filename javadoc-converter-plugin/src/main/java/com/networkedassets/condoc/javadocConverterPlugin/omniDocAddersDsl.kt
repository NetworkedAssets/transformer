package com.networkedassets.condoc.javadocConverterPlugin

import com.networkedassets.condoc.transformer.common.docitem.OmniDocItem

inline fun OmniDocItem.category(name: String, block: CategoryHelper.() -> Unit) {
    val cat = addCategory(name)
    CategoryHelper(cat).block()
}

class CategoryHelper(val cat: OmniDocItem.MemberCategory) {
    operator fun String.unaryPlus() {
        cat.addMember(this)
    }
}

inline fun OmniDocItem.relation(name: String, block: RelationshipHelper.() -> Unit) {
    val rel = addRelationship(name)
    RelationshipHelper(rel).block()
}

class RelationshipHelper(val rel: OmniDocItem.Relation) {
    operator fun String.unaryPlus() {
        rel.addRelItem(this)
    }
}

inline fun omniDocItem(block: OmniDocItem.() -> Unit) = OmniDocItem().apply(block)