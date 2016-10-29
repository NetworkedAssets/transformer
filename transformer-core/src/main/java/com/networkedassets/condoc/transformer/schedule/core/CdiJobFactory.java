package com.networkedassets.condoc.transformer.schedule.core;

import org.quartz.Job;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.simpl.PropertySettingJobFactory;
import org.quartz.spi.TriggerFiredBundle;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.InjectionTarget;
import javax.inject.Inject;

/**
 * Used to properly inject fields into new ScheduledJob instance
 */
public class CdiJobFactory extends PropertySettingJobFactory {

    @Inject
    private BeanManager beanManager;

    @Override
    public Job newJob(TriggerFiredBundle triggerFiredBundle, Scheduler scheduler) throws SchedulerException {
        Job job = super.newJob(triggerFiredBundle, scheduler);
        CreationalContext<Job> ctx = beanManager.createCreationalContext(null);
        AnnotatedType<Job> type = (AnnotatedType<Job>) beanManager.createAnnotatedType(job.getClass());
        InjectionTarget<Job> it = beanManager.createInjectionTarget(type);
        it.inject(job, ctx);
        return job;
    }
}