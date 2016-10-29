package com.networkedassets.condoc.transformer.common.exceptions;

public class SourceNotFoundException extends EntityNotFoundException {
    public SourceNotFoundException() {
    }

    public SourceNotFoundException(String message) {
        super(message);
    }
}
