package com.networkedassets.condoc.javadocConverterPlugin.util;

/**
 * Exception used in Javadoc wrapper
 */
@SuppressWarnings("serial")
public class JavadocException extends Exception {
    public JavadocException(String message, Throwable cause) {
        super(message, cause);
    }
}