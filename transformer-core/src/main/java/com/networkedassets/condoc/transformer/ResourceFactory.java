package com.networkedassets.condoc.transformer;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.ejb.Singleton;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import java.util.logging.Logger;

@Singleton
public class ResourceFactory {
    @Produces
    public Logger produceLogger(InjectionPoint injectionPoint) {
        return Logger.getLogger(injectionPoint.getMember().getDeclaringClass().getName());
    }

    @Produces
    public ObjectMapper produceObjectMapper() {
        return new ObjectMapper();
    }
}