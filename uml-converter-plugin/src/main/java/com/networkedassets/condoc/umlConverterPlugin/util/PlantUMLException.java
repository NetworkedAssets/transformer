package com.networkedassets.condoc.umlConverterPlugin.util;

import com.networkedassets.condoc.transformer.common.exceptions.TransformerException;

public class PlantUMLException extends TransformerException {

    public PlantUMLException(String message) {
        super(message);
    }

    public PlantUMLException(String message, Throwable cause) {
        super(message, cause);
    }
}
