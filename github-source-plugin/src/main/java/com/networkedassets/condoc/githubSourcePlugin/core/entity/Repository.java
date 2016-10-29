package com.networkedassets.condoc.githubSourcePlugin.core.entity;

import java.util.HashMap;
import java.util.Map;


public class Repository {

    private String name;
    private String slug;
    private Map<String, Branch> branches;

    public Repository() {
        branches = new HashMap<>();
    }


    public Repository(String name, String slug) {
        this();
        this.name = name;
        this.slug = slug;
    }

    public void addBranch(Branch branch) {
        branches.put(branch.getId(), branch);
    }

    public String getSlug() {
        return slug;
    }

    public String getName() {
        return name;
    }
}
