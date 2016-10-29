package com.networkedassets.condoc.javadocConverterPlugin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networkedassets.condoc.javadocConverterPlugin.util.Javadoc;
import com.networkedassets.condoc.javadocConverterPlugin.util.JavadocException;
import com.networkedassets.condoc.jsonDoclet.model.Package;
import com.networkedassets.condoc.jsonDoclet.model.Root;
import com.networkedassets.condoc.transformer.common.Documentation;
import com.networkedassets.condoc.transformer.common.docitem.OmniDocItem;
import com.networkedassets.condoc.transformer.dispatchRawData.core.boundary.require.RawData;

@SuppressWarnings("unused")
public class JavadocConverterPlugin {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public Documentation<OmniDocItem> convert(RawData rawData) {
        Documentation<OmniDocItem> javadocDocumentation = buildEmptyJavaDocumentation();
        try {
            Root docRoot = Javadoc.structureFromRawData(rawData);
            for (Package p : docRoot.getPackage()) {
                OmniDocAdders.addPackageDocItem(javadocDocumentation, p);
            }
            return javadocDocumentation;
        } catch(JavadocException e){
            throw new RuntimeException("Couldn't generate Javadoc", e);
        }
    }

    private Documentation<OmniDocItem> buildEmptyJavaDocumentation() {
        Documentation<OmniDocItem> javadocDocumentation = new Documentation<>();
        javadocDocumentation.setType(Documentation.DocumentationType.OMNIDOC);
        return javadocDocumentation;
    }

    public String getIdentifier() {
        return "javadoc-converter";
    }

    private static String convertStructureToJson(Object val) throws JsonProcessingException {
        return OBJECT_MAPPER.writeValueAsString(val);
    }
}
