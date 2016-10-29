package com.networkedassets.condoc.bitbucketSourcePlugin.core;

import com.networkedassets.condoc.transformer.common.exceptions.TransformerException;

public class InvalidJsonException extends TransformerException {
    InvalidJsonException(String message) {
        super(message);
    }
}

