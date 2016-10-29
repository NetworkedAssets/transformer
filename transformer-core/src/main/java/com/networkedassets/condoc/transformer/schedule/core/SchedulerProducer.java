package com.networkedassets.condoc.transformer.schedule.core;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;

import javax.ejb.Singleton;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

@Singleton
public class SchedulerProducer {

    @Inject
    CdiJobFactory cdiJobFactory;

    @Produces
    @Named("condocTransformerScheduler")
    public Scheduler getInstance(){
        SchedulerFactory factory = new StdSchedulerFactory();
        Scheduler scheduler = null;

        try {
            scheduler = factory.getScheduler();
            scheduler.setJobFactory(cdiJobFactory);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return scheduler;
    }
}
