package com.networkedassets.condoc.githubSourcePlugin.core.entity;


public class Branch {

    private String displayId;
    private String id;
    private String latestCommit;


    public Branch() {

    }

    public Branch(String displayId, String id, String latestCommit) {
        this();
        this.setDisplayId(displayId);
        this.setId(id);
        this.setLatestCommit(latestCommit);
    }

    public String getLatestCommit() {
        return latestCommit;
    }

    public void setLatestCommit(String latestCommit) {
        this.latestCommit = latestCommit;
    }


    public String getDisplayId() {
        return displayId;
    }

    public void setDisplayId(String displayId) {
        this.displayId = displayId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Branch{" +
                "displayId='" + displayId + '\'' +
                ", id='" + id + '\'' +
                '}';
    }


}

