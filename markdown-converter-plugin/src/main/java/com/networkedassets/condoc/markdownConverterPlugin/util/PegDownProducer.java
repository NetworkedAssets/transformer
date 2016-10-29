package com.networkedassets.condoc.markdownConverterPlugin.util;

import org.pegdown.Extensions;
import org.pegdown.PegDownProcessor;
import org.pegdown.plugins.PegDownPlugins;

import javax.ejb.Singleton;
import javax.enterprise.inject.Produces;
import javax.inject.Named;

@Singleton
public class PegDownProducer {
    @Produces
    @Named("markdown-converter")
    public PegDownProcessor getInstance(){
        return new PegDownProcessor(
                Extensions.ALL, PegDownPlugins.builder().withPlugin(MarkdownCodeBlockParser.class).build());
    }
}
