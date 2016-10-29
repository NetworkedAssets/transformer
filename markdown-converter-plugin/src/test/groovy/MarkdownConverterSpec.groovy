package groovy

import com.networkedassets.condoc.markdownConverterPlugin.MarkdownConverterPlugin
import com.networkedassets.condoc.markdownConverterPlugin.util.MarkdownCodeBlockParser
import com.networkedassets.condoc.transformer.common.Documentation
import com.networkedassets.condoc.transformer.dispatchRawData.core.boundary.DefaultRawData
import org.pegdown.Extensions
import org.pegdown.PegDownProcessor
import org.pegdown.plugins.PegDownPlugins
import spock.lang.Specification

class MarkdownConverterSpec extends Specification{

    static def markdownConverter = new MarkdownConverterPlugin()
    static def pegDownProcessor = new PegDownProcessor(
            Extensions.ALL,
            PegDownPlugins.builder()
                    .withPlugin(MarkdownCodeBlockParser.class)
                    .build()
    );

    @SuppressWarnings("GroovyAccessibility")
    def setupSpec(){
        markdownConverter.pegDownProcessor = pegDownProcessor;
    }

    def "should parse markdown file into html and store it as DocItem in Documentation"() {
        given:
        def rawData = new DefaultRawData()
        def file = File.createTempFile("temp",".md")
        file.write("#Something\n")

        when:
        rawData.add(file.toPath())

        then:
        rawData.dataPaths.iterator().next() == file.toPath()

        when:
        def documentation = (Documentation) markdownConverter.convert(rawData)

        then:
        documentation.docItems.size() != 0
    }

    def "should parse markdown content into html"() {
        given:
        def markdown = "nope\n" +
                "``` c\n" +
                "int main(){\n" +
                "   return 0;\n" +
                "}\n" +
                "```"

        when:
        def html = pegDownProcessor.markdownToHtml(markdown)

        then:
        html == "<p>nope</p>\n" +
                "<pre><code class=\"c\">int main(){\n" +
                "   return 0;\n" +
                "}\n"+
                "</code></pre>"
    }

    def "should parse markdown content (code block in list) into correct html"() {
        given:
        def markdown = "1. Nothing really interesting.\n" +
                "```bash\n" +
                "# javac -version\n" +
                "# echo JAVA_HOME\n" +
                "```\n" +
                "2. Hello world."

        when:
        def html = pegDownProcessor.markdownToHtml(markdown)

        then:
        html == "<ol>\n" +
                "  <li>\n" +
                "  <p>Nothing really interesting.</p>\n" +
                "  <pre><code class=\"bash\"># javac -version\n" +
                "# echo JAVA_HOME\n" +
                "</code></pre></li>\n" +
                "  <li>Hello world.</li>\n" +
                "</ol>"
    }

    def "should correctly parse links"(){
        given:
        def markdown = "[![Windows Build Status](https://img.shields.io/appveyor/ci/spockframework/spock/master.svg?label=Windows%20Build)](https://ci.appveyor.com/project/spockframework/spock/branch/master)"

        when:
        def html = pegDownProcessor.markdownToHtml(markdown)

        then:
        html == "<p><a href=\"https://ci.appveyor.com/project/spockframework/spock/branch/master\">" +
                "<img src=\"https://img.shields.io/appveyor/ci/spockframework/spock/master.svg?label=Windows%20Build\" " +
                "alt=\"Windows Build Status\" /></a></p>"
    }
}
