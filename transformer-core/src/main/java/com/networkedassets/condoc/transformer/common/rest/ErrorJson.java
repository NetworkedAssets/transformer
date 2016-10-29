package com.networkedassets.condoc.transformer.common.rest;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"name", "message"})
public class ErrorJson {
    private String message;
    private String name;

    public ErrorJson(Exception exception) {
        message = exception.getMessage();
        name = exception.getClass().getSimpleName();
    }

    public ErrorJson(String name, String message) {
        this.name = name;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public ErrorJson setName(String name) {
        this.name = name;
        return this;
    }
}
