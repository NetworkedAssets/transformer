package com.networkedassets.condoc.transformer.retrieveDocumentation.core;

import com.networkedassets.condoc.transformer.common.DocItemTreeNode;
import com.networkedassets.condoc.transformer.common.docitem.OmniDocItem;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class OmniDocTreeifier {
    private Collection<OmniDocItem> docItems;
    private String commonParent;

    public OmniDocTreeifier(Collection<OmniDocItem> docItems) {
        this.docItems = filterInlined(docItems);
        this.commonParent = findCommonParent(docItems);
    }

    private Set<OmniDocItem> filterInlined(Collection<OmniDocItem> docItems) {
        return docItems.parallelStream().filter(di -> !di.isInlined()).collect(Collectors.toSet());
    }

    public List<DocItemTreeNode> treeify() {
        return treeifyWithSingleRoot().getChildren();
    }

    public DocItemTreeNode treeifyWithSingleRoot() {
        DocItemTreeNode packageTree = buildPackageTree();
        addClasses(packageTree);
        flattenPackages(packageTree);
        return packageTree;
    }

    private DocItemTreeNode buildPackageTree() {
        DocItemTreeNode root = new DocItemTreeNode(commonParent, commonParent, "package");
        Set<String> packages = extractPackages().stream()
                .map(OmniDocItem::getFullName)
                .map(s -> StringUtils.substringAfter(s, commonParent + "."))
                .collect(Collectors.toSet());
        for (String aPackage : packages) {
            DocItemTreeNode lastNode = root;
            for (String node : aPackage.split("\\.")) {
                DocItemTreeNode finalLastNode = lastNode;
                lastNode = lastNode
                        .getChildByName(node)
                        .orElseGet(() -> finalLastNode.addChild(finalLastNode.getFullName() + "." + node, node, "package"));
                connectDocItem(lastNode);
            }
        }
        return root;
    }


    private void connectDocItem(DocItemTreeNode lastNode) {
        docItems.stream().filter(di -> di.getFullName().equals(lastNode.getFullName())).findAny()
                .ifPresent(lastNode::setOriginalDocItem);
    }

    private void flattenPackages(DocItemTreeNode packageTree) {
        if (!packageTree.getType().equals("package")) {
            return;
        }
        if (packageTree.getChildren().size() == 1 && packageTree.getChildren().get(0).getType().equals("package")) {
            DocItemTreeNode child = packageTree.getChildren().get(0);
            packageTree.setChildren(child.getChildren());
            packageTree.setFullName(child.getFullName());
            if (child.getOriginalDocItem() != null) {
                packageTree.setOriginalDocItem(child.getOriginalDocItem());
            }
            packageTree.setName(packageTree.getName() + "." + child.getName());
            flattenPackages(packageTree);
        } else {
            packageTree.getChildren().forEach(this::flattenPackages);
        }
    }

    private void addClasses(DocItemTreeNode packageTree) {
        List<DocItemTreeNode> children = docItems.stream()
                .filter(di -> !di.getItemType().equals("package"))
                .filter(di -> isDirectChild(packageTree.getFullName(), di.getFullName()))
                .map(DocItemTreeNode::new)
                .collect(Collectors.toList());
        packageTree.getChildren().addAll(children);
        packageTree.getChildren().forEach(this::addClasses);
    }

    private Set<OmniDocItem> extractPackages() {
        return docItems.stream()
                .filter(docItem -> docItem.getItemType().equals("package"))
                .collect(Collectors.toSet());
    }

    private String findCommonParent(Collection<OmniDocItem> docItems) {
        String[] fullNames = docItems.stream().map(OmniDocItem::getFullName).toArray(String[]::new);
        String longestCommonPrefix = StringUtils.getCommonPrefix(fullNames);
        return StringUtils.substringBeforeLast(longestCommonPrefix, ".");
    }

    private boolean isDirectChild(String parent, String childCandidate) {
        return !Objects.equals(parent, childCandidate)
                && childCandidate.startsWith(parent)
                && !StringUtils.substringAfter(childCandidate, parent + ".").contains(".");
    }
}
