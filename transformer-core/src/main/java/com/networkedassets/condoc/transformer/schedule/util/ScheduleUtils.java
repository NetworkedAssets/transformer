package com.networkedassets.condoc.transformer.schedule.util;

import com.networkedassets.condoc.transformer.schedule.ScheduledJob;
import com.networkedassets.condoc.transformer.schedule.core.entity.ScheduleInfo;
import org.quartz.*;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * Utils producing CRON (time-based job) used during scheduling
 */
public class ScheduleUtils {

    /**
     * Creates CRON for given event.
     * Syntax: sec min hour day_of_month month day_of_week year(optional)
     * Example: "0 0/5 * * * ?" - fire every 5 minutes
     *
     * @param info full firing-time description
     * @return CRON built from event
     */
    private static CronScheduleBuilder getCronSchedule(ScheduleInfo info) {
        String[] splitTime = info.getTime().split(":");
        String h = splitTime[0];
        String min = splitTime[1];

        return CronScheduleBuilder.cronSchedule("00 " + min + " " + h + " " + getCronDays(info));
    }

    private static String getCronDays(ScheduleInfo info) {
        String date;
        if (info.isPeriodic()) {
            //Every week
            if (info.getPeriodType() == ScheduleInfo.PeriodType.WEEK) {
                String days = info.getWeekdays().toString();
                date = "? * " + days;

            } else //Every x days
                date = "*/" + info.getNumber() + " * ?";
        } else
            date = info.getDay() + " "
                    + info.getMonth() + " ? "
                    + info.getYear();

        return date;
    }

    public static void schedule(ScheduleInfo info, Scheduler scheduler) throws SchedulerException {
        JobDetail job = newJob(ScheduledJob.class)
                .usingJobData("bundleId", info.getBundle().getId())
                .build();

        Trigger trigger = newTrigger().startNow().withSchedule(ScheduleUtils.getCronSchedule(info))
                .build();

        System.out.println(
                "Scheduled event with cron: " + ((CronTrigger) trigger).getCronExpression());

        info.setScheduledJobName(job.getKey().getName());

        scheduler.scheduleJob(job, trigger);

        if (!scheduler.isStarted())
            scheduler.start();
    }
}
