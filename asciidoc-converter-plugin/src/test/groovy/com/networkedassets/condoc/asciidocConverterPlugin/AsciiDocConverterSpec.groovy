package com.networkedassets.condoc.asciidocConverterPlugin

import com.networkedassets.condoc.transformer.dispatchRawData.core.boundary.DefaultRawData
import org.asciidoctor.Asciidoctor
import org.asciidoctor.Options
import spock.lang.Specification

class AsciiDocConverterSpec extends Specification{

    static def asciiDocConverter = new AsciiDocConverterPlugin()
    static def asciidoctor = Asciidoctor.Factory.create()
    static def options = new Options()

    @SuppressWarnings("GroovyAccessibility")
    def setupSpec(){
        options.setHeaderFooter(true)
        options.setToFile(false)

        asciiDocConverter.asciidoctor = asciidoctor;
        asciiDocConverter.options = options
    }

    def "should parse asciiDoc file into html and store it in DocItem"() {
        given:
        def rawData = new DefaultRawData()
        def file = File.createTempFile("temp",".adoc")
        def directory = file.getParentFile()
        file.write("= Something\n")

        when:
        rawData.add(directory.toPath())

        then:
        rawData.dataPaths.iterator().next() == directory.toPath()

        when:
        def documentation = asciiDocConverter.convert(rawData)

        then:
        documentation.getDocItems().size() != 0
    }

    def "should parse asciiDoc content into html"() {
        given:
        def asciiDoc = "= Hello, AsciiDoc!\n" +
                "Doc Writer <doc@example.com>"

        when:
        def html = asciidoctor.render(asciiDoc, options)

        then:
        html.contains("<h1>Hello, AsciiDoc!</h1>\n" +
                "<div class=\"details\">\n" +
                "<span id=\"author\" class=\"author\">Doc Writer</span><br>\n" +
                "<span id=\"email\" class=\"email\"><a href=\"mailto:doc@example.com\">doc@example.com</a></span><br>\n" +
                "</div>")
    }

    def "should parse asciiDoc content (code block in list) into correct html"() {
        given:
        def asciiDoc = "== First Section\n" +
                "* item 1\n" +
                "* item 2\n" +
                "[source,ruby]\n" +
                "puts \"Hello, World!\""

        when:
        def html = asciidoctor.render(asciiDoc, options)

        then:
        html.contains("<h2 id=\"_first_section\">First Section</h2>\n" +
                "<div class=\"sectionbody\">\n" +
                "<div class=\"ulist\">\n" +
                "<ul>\n" +
                "<li>\n" +
                "<p>item 1</p>\n" +
                "</li>\n" +
                "<li>\n" +
                "<p>item 2</p>\n" +
                "<div class=\"listingblock\">\n" +
                "<div class=\"content\">\n" +
                "<pre class=\"highlight\"><code class=\"language-ruby\" data-lang=\"ruby\">puts \"Hello, World!\"</code></pre>\n" +
                "</div>\n" +
                "</div>\n" +
                "</li>\n" +
                "</ul>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>")
    }
}
