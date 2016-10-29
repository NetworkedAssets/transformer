package com.networkedassets.condoc.asciidocConverterPlugin.util;

import org.asciidoctor.Asciidoctor;
import org.asciidoctor.Options;

import javax.ejb.Singleton;
import javax.enterprise.inject.Produces;
import java.util.Collections;

@Singleton
public class AsciidoctorProducer {

    @Produces
    public Asciidoctor getAsciidoctor() {
        return Asciidoctor.Factory.create(Collections.singletonList("gems/asciidoctor-1.5.4/lib"));
    }

    @Produces
    public Options getOptions() {
        Options options = new Options();
        options.setHeaderFooter(true);
        options.setToFile(false);
        return options;
    }

}
