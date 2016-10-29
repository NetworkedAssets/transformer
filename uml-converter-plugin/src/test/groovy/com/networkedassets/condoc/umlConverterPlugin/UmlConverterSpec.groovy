import com.networkedassets.condoc.transformer.common.Documentation
import com.networkedassets.condoc.transformer.common.exceptions.TransformerException
import com.networkedassets.condoc.transformer.dispatchRawData.core.boundary.DefaultRawData
import com.networkedassets.condoc.umlConverterPlugin.UmlConverterPlugin
import spock.lang.Specification

import java.nio.file.Path

class UmlConverterSpec extends Specification {

    static def umlConverter = new UmlConverterPlugin()

    def "should parse java project and store it as DocItem in Documentation"() {
        given:
        def rawData = new DefaultRawData()
        def projectFiles = generateTempProjectFiles()

        when:
        rawData.add(projectFiles)

        then:
        rawData.dataPaths.iterator().next() == projectFiles

        when:
        def documentation = (Documentation) umlConverter.convert(rawData)

        then:
        documentation.docItems.size() != 0
    }

    def "should throw TransformerException when rawData is empty"() {
        given:
        def rawData = new DefaultRawData()

        when:
        umlConverter.convert(rawData)

        then:
        rawData.getDataPaths().isEmpty()
        thrown TransformerException
    }

    Path generateTempProjectFiles(){
        def directory = File.createTempDir()
        def file = new File(directory, "temp.java")
        file.write("package com.networkedassets.condoc;\n" +
                "import javax.servlet.annotation.WebListener; \n \n" +
                "@WebListener\n" +
                "public class TestClass {\n" +
                "private int integerField;\n" +
                "public static String anotherField = \"Test\";\n" +
                "\n" +
                "public void functionTest(int field) throws Exception{\n" +
                "  integerField = field;\n" +
                "}\n" +
                "}")

        return directory.toPath()
    }
}