package com.networkedassets.condoc.githubSourcePlugin.core.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.networkedassets.condoc.githubSourcePlugin.core.GithubEventDeserializer;

@JsonDeserialize(using = GithubEventDeserializer.class)
public class GithubEvent {

    public static final String OWNER_SEPARATOR = "/";
    public static final String REPOSITORY_SEPARATOR = "|";

    private String sourceUrl;
    private String repositoryName;
    private String branchName;
    private String ownerName;

    public GithubEvent() {
    }

    public GithubEvent(String sourceUrl, String repositoryName, String branchName, String ownerName) {
        this.sourceUrl = sourceUrl;
        this.repositoryName = repositoryName;
        this.branchName = branchName;
        this.ownerName = ownerName;
    }

    @JsonIgnore
    public boolean isValid() {
        return notEmptyOrNull(sourceUrl) && notEmptyOrNull(repositoryName) && notEmptyOrNull(branchName) && notEmptyOrNull(ownerName);
    }

    private boolean notEmptyOrNull(String str) {
        return str != null && !str.isEmpty();
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public String getRepositoryName() {
        return repositoryName;
    }

    public void setRepositoryName(String repositoryName) {
        this.repositoryName = repositoryName;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }
}
