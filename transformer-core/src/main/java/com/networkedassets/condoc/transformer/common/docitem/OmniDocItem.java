package com.networkedassets.condoc.transformer.common.docitem;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.networkedassets.condoc.transformer.common.Documentation;
import org.apache.commons.lang3.StringEscapeUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SuppressWarnings("WeakerAccess")
@Entity
public class OmniDocItem extends DocItem {

    private boolean inlined = false;
    // region displayName
    @Column(columnDefinition = "TEXT")
    private String beforeDisplayName = "";
    @Column(columnDefinition = "TEXT")
    private String displayName = "";
    @Column(columnDefinition = "TEXT")
    private String afterDisplayName = "";
    // endregion
    @Column(columnDefinition = "TEXT")
    private String shortName = ""; // optional

    @Column(columnDefinition = "TEXT")
    private String functionReturnType = "";
    @ElementCollection(fetch = FetchType.EAGER)
    @Column(columnDefinition = "TEXT")
    private List<String> functionParameters = new ArrayList<>();
    @Column(columnDefinition = "TEXT")
    private String declarationInCode = "";

    // region metadata
    @Column(columnDefinition = "TEXT")
    private String beforeMetadata = "";
    @ElementCollection(fetch = FetchType.EAGER)
    @Column(columnDefinition = "TEXT")
    private Set<String> metadata = new HashSet<>();
    @Column(columnDefinition = "TEXT")
    private String afterMetadata = "";
    // endregion

    // region modifiers
    @Column(columnDefinition = "TEXT")
    private String beforeModifiers = "";
    @ElementCollection(fetch = FetchType.EAGER)
    @Column(columnDefinition = "TEXT")
    private Set<String> modifiers = new HashSet<>();
    @Column(columnDefinition = "TEXT")
    private String afterModifiers = "";
    // endregion

    // region item type
    @Column(columnDefinition = "TEXT")
    private String beforeItemType = "";
    @Column(columnDefinition = "TEXT")
    private String itemType = "";
    @Column(columnDefinition = "TEXT")
    private String afterItemType = "";
    // endregion

    // region relationships
    @Column(columnDefinition = "TEXT")
    private String beforeRelations = "";
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "owner", cascade = CascadeType.ALL)
    private Set<Relation> relations = new HashSet<>();
    @Column(columnDefinition = "TEXT")
    private String afterRelations = "";
    // endregion

    // region doc text
    @Column(columnDefinition = "TEXT")
    private String beforeDocText = "";
    @Column(columnDefinition = "TEXT")
    private String excerpt = "";
    @Column(columnDefinition = "TEXT")
    private String docText = "";
    @Column(columnDefinition = "TEXT")
    private String afterDocText = "";
    // endregion

    // region members
    @Column(columnDefinition = "TEXT")
    private String beforeMemberCategories = "";
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "owner", cascade = CascadeType.ALL)
    private Set<MemberCategory> memberCategories = new HashSet<>();
    @Column(columnDefinition = "TEXT")
    private String afterMemberCategories = "";
    // endregion

    public boolean isInlined() {
        return inlined;
    }

    public OmniDocItem setInlined(boolean inlined) {
        this.inlined = inlined;
        return this;
    }

    public String getBeforeDisplayName() {
        return beforeDisplayName;
    }

    public OmniDocItem setBeforeDisplayName(String beforeDisplayName) {
        this.beforeDisplayName = beforeDisplayName;
        return this;
    }

    public String getDisplayName() {
        return displayName;
    }

    public OmniDocItem setDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    public String getAfterDisplayName() {
        return afterDisplayName;
    }

    public OmniDocItem setAfterDisplayName(String afterDisplayName) {
        this.afterDisplayName = afterDisplayName;
        return this;
    }

    public String getShortName() {
        return shortName;
    }

    public OmniDocItem setShortName(String shortName) {
        this.shortName = shortName;
        return this;
    }

    public String getBeforeMetadata() {
        return beforeMetadata;
    }

    public OmniDocItem setBeforeMetadata(String beforeMetadata) {
        this.beforeMetadata = beforeMetadata;
        return this;
    }

    public Set<String> getMetadata() {
        return metadata;
    }

    public OmniDocItem setMetadata(Set<String> metadata) {
        this.metadata = metadata;
        return this;
    }

    public String getAfterMetadata() {
        return afterMetadata;
    }

    public OmniDocItem setAfterMetadata(String afterMetadata) {
        this.afterMetadata = afterMetadata;
        return this;
    }

    public String getBeforeModifiers() {
        return beforeModifiers;
    }

    public OmniDocItem setBeforeModifiers(String beforeModifiers) {
        this.beforeModifiers = beforeModifiers;
        return this;
    }

    public Set<String> getModifiers() {
        return modifiers;
    }

    public OmniDocItem setModifiers(Set<String> modifiers) {
        this.modifiers = modifiers;
        return this;
    }

    public String getAfterModifiers() {
        return afterModifiers;
    }

    public OmniDocItem setAfterModifiers(String afterModifiers) {
        this.afterModifiers = afterModifiers;
        return this;
    }

    public String getBeforeItemType() {
        return beforeItemType;
    }

    public OmniDocItem setBeforeItemType(String beforeItemType) {
        this.beforeItemType = beforeItemType;
        return this;
    }

    public String getItemType() {
        return itemType;
    }

    public OmniDocItem setItemType(String itemType) {
        this.itemType = itemType;
        return this;
    }

    public String getAfterItemType() {
        return afterItemType;
    }

    public OmniDocItem setAfterItemType(String afterItemType) {
        this.afterItemType = afterItemType;
        return this;
    }

    public String getBeforeRelations() {
        return beforeRelations;
    }

    public OmniDocItem setBeforeRelations(String beforeRelationships) {
        this.beforeRelations = beforeRelationships;
        return this;
    }

    public Set<Relation> getRelations() {
        return relations;
    }

    public OmniDocItem setRelations(Set<Relation> relations) {
        this.relations = relations;
        return this;
    }

    public String getAfterRelations() {
        return afterRelations;
    }

    public OmniDocItem setAfterRelations(String afterRelationships) {
        this.afterRelations = afterRelationships;
        return this;
    }

    public String getBeforeDocText() {
        return beforeDocText;
    }

    public OmniDocItem setBeforeDocText(String beforeDocText) {
        this.beforeDocText = beforeDocText;
        return this;
    }

    public String getExcerpt() {
        return excerpt;
    }

    public OmniDocItem setExcerpt(String excerpt) {
        this.excerpt = excerpt;
        return this;
    }

    public String getDocText() {
        return docText;
    }

    public OmniDocItem setDocText(String docText) {
        this.docText = docText;
        return this;
    }

    public String getAfterDocText() {
        return afterDocText;
    }

    public OmniDocItem setAfterDocText(String afterDocText) {
        this.afterDocText = afterDocText;
        return this;
    }

    public String getBeforeMemberCategories() {
        return beforeMemberCategories;
    }

    public OmniDocItem setBeforeMemberCategories(String beforeMembers) {
        this.beforeMemberCategories = beforeMembers;
        return this;
    }

    public Set<MemberCategory> getMemberCategories() {
        return memberCategories;
    }

    public OmniDocItem setMemberCategories(Set<MemberCategory> categories) {
        this.memberCategories = categories;
        return this;
    }

    public String getAfterMemberCategories() {
        return afterMemberCategories;
    }

    public OmniDocItem setAfterMemberCategories(String afterMembers) {
        this.afterMemberCategories = afterMembers;
        return this;
    }
    // endregion

    public static final String REFERENCE_TEMPLATE = "<omni-doc ref=\"%s\">%s</omni-doc>";

    public static String makeRef(String fullName, String shortName) {
        return String.format(REFERENCE_TEMPLATE,
                StringEscapeUtils.escapeHtml4(fullName),
                StringEscapeUtils.escapeHtml4(shortName));
    }

    @JsonIgnore
    public String getRef() {
        return makeRef(fullName, shortName);
    }

    public MemberCategory addCategory(String name) {
        MemberCategory category = new MemberCategory();
        category.setName(name);
        category.setOwner(this);
        this.getMemberCategories().add(category);
        return category;
    }

    public Relation addRelationship(String name) {
        Relation relation = new Relation();
        relation.setName(name);
        relation.setOwner(this);
        this.getRelations().add(relation);
        return relation;
    }

    public String getFunctionReturnType() {
        return functionReturnType;
    }

    public OmniDocItem setFunctionReturnType(String functionReturnType) {
        this.functionReturnType = functionReturnType;
        return this;
    }

    public List<String> getFunctionParameters() {
        return functionParameters;
    }

    public OmniDocItem setFunctionParameters(List<String> functionArguments) {
        this.functionParameters = functionArguments;
        return this;
    }

    public String getDeclarationInCode() {
        return declarationInCode;
    }

    public OmniDocItem setDeclarationInCode(String inCodeNameTemplate) {
        this.declarationInCode = inCodeNameTemplate;
        return this;
    }

    @Override
    public Documentation.DocumentationType getDocItemType() {
        return Documentation.DocumentationType.of(OmniDocItem.class);
    }


    @SuppressWarnings("WeakerAccess")
    @Entity
    public static class Relation {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id;
        @Column(columnDefinition = "TEXT")
        private String name = "";
        @Column(columnDefinition = "TEXT")
        private String displayName = "";
        @ElementCollection(fetch = FetchType.EAGER)
        @Column(columnDefinition = "TEXT")
        private Set<String> items = new HashSet<>();
        @JsonIgnore
        @ManyToOne
        private OmniDocItem owner;

        // region java bullshit boilerplate (getters & setters)
        public Integer getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public Relation setName(String relationName) {
            this.name = relationName;
            return this;
        }

        public Set<String> getItems() {
            return items;
        }

        public Relation setItems(Set<String> relationItems) {
            this.items = relationItems;
            return this;
        }

        public OmniDocItem getOwner() {
            return owner;
        }

        public Relation setOwner(OmniDocItem owner) {
            this.owner = owner;
            return this;
        }

        public String getDisplayName() {
            return displayName;
        }

        public Relation setDisplayName(String displayName) {
            this.displayName = displayName;
            return this;
        }
        // endregion

        public void addRelItem(String nameWithRefs) {
            items.add(nameWithRefs);
        }


    }

    @SuppressWarnings("WeakerAccess")
    @Entity
    public static class MemberCategory {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id;
        @Column(columnDefinition = "TEXT")
        private String name = "";
        @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
        private Set<Member> items = new HashSet<>();
        @JsonIgnore
        @ManyToOne
        private OmniDocItem owner;

        // region java bullshit boilerplate (getters & setters)
        public Integer getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public MemberCategory setName(String name) {
            this.name = name;

            return this;
        }

        public Set<Member> getItems() {
            return items;
        }

        public MemberCategory setItems(Set<Member> memberItems) {
            this.items = memberItems;
            return this;
        }

        public OmniDocItem getOwner() {
            return owner;
        }

        public MemberCategory setOwner(OmniDocItem owner) {
            this.owner = owner;
            return this;
        }
        // endregion

        public void addMember(String fullName) {
            Member member = new Member(fullName);
            member.setCategory(this);
            this.getItems().add(member);
        }

        @SuppressWarnings("WeakerAccess")
        @Entity
        public static class Member {
            @Id
            @GeneratedValue(strategy = GenerationType.IDENTITY)
            private Integer id;

            @JsonIgnore
            @ManyToOne
            private MemberCategory category;

            @Column(columnDefinition = "TEXT")
            private String fullName = "";

            /*
            Inlined doc items should be manually injected here when providing the object
            to the delivery mechanisms
             */
            @Transient
            private OmniDocItem data;

            public Member() {

            }

            public Member(String fullName) {
                this.fullName = fullName;
            }

            // region java bullshit boilerplate (getters & setters)
            public Integer getId() {
                return id;
            }

            public Member setId(Integer id) {
                this.id = id;
                return this;
            }

            public String getFullName() {
                return fullName;
            }

            public Member setFullName(String fullName) {
                this.fullName = fullName;
                return this;
            }

            public OmniDocItem getData() {
                return data;
            }

            public Member setData(OmniDocItem data) {
                this.data = data;
                return this;
            }

            public MemberCategory getCategory() {
                return category;
            }

            public Member setCategory(MemberCategory category) {
                this.category = category;
                return this;
            }
            //endregion
        }
    }
}
