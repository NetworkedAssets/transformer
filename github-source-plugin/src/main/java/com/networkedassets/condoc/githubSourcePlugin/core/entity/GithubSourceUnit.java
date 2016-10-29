package com.networkedassets.condoc.githubSourcePlugin.core.entity;


import java.util.HashMap;
import java.util.Map;

public class GithubSourceUnit {
    private Map<String, Repository> repos = new HashMap<>();

    public GithubSourceUnit() {

    }

    public void addRepository(Repository r) {
        repos.put(r.getSlug(), r);
    }

    public Repository getRepositoryByKey(String key) {
        return repos.get(key);
    }

    public Map<String, Repository> getRepositories() {
        return repos;
    }

    public void setReposiotries(Map<String, Repository> repos) {
        this.repos = repos;
    }
}
