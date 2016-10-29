package com.networkedassets.condoc.javadocConverterPlugin

import com.networkedassets.condoc.transformer.dispatchRawData.core.boundary.DefaultRawData
import org.junit.Test
import java.nio.file.Paths

class JavadocTest {
    @Test
    fun javadocWorks() {
        val simpledataPath = this.javaClass.getResource("/simpledata")
        val plugin = createPlugin()
        val rawData = DefaultRawData()
        rawData.add(Paths.get(simpledataPath.toURI()))

        val doc = plugin.convert(rawData)

        assert(!doc.docItems.isEmpty())
        // TODO: add more assertions
    }

    private fun createPlugin(): JavadocConverterPluginToolsJarWrapper {
        val plugin = JavadocConverterPluginToolsJarWrapper()
        try {
            plugin.contextInitialized(null)
        } catch (e: Exception) {
        }
        return plugin
    }
}
