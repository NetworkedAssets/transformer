package com.networkedassets.condoc.transformer.schedule.core.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.networkedassets.condoc.transformer.manageBundles.core.entity.Bundle;

import javax.persistence.*;
import java.util.Calendar;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
public class ScheduleInfo {

    public enum PeriodType {
        DAY, WEEK
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Embedded
    private ScheduledWeekdays weekdays;

    @ManyToOne
    @JsonIgnore
    private Bundle bundle;

    private boolean periodic;
    private PeriodType periodType;
    private int number;
    private String time;
    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Calendar oneTimeDate;

    private String scheduledJobName;

    public String getScheduledJobName() {
        return scheduledJobName;
    }

    public ScheduleInfo setScheduledJobName(String scheduledJobName) {
        this.scheduledJobName = scheduledJobName;
        return this;
    }

    public ScheduledWeekdays getWeekdays() {
        return weekdays;
    }

    public ScheduleInfo setWeekdays(ScheduledWeekdays weekdays) {
        this.weekdays = weekdays;
        return this;
    }

    public boolean isPeriodic() {
        return periodic;
    }

    public ScheduleInfo setPeriodic(boolean periodic) {
        this.periodic = periodic;
        return this;
    }

    public PeriodType getPeriodType() {
        return periodType;
    }

    public ScheduleInfo setPeriodType(PeriodType periodType) {
        this.periodType = periodType;
        return this;
    }

    public int getNumber() {
        return number;
    }

    public ScheduleInfo setNumber(int number) {
        this.number = number;
        return this;
    }

    public String getTime() {
        return time;
    }

    public ScheduleInfo setTime(String time) {
        this.time = time;
        return this;
    }

    public Calendar getOneTimeDate() {
        return oneTimeDate;
    }

    public ScheduleInfo setOneTimeDate(Calendar oneTimeDate) {
        this.oneTimeDate = oneTimeDate;
        return this;
    }

    public Integer getId() {
        return id;
    }

    public ScheduleInfo setId(Integer id) {
        this.id = id;
        return this;
    }

    public Bundle getBundle() {
        return bundle;
    }

    public ScheduleInfo setBundle(Bundle bundle) {
        this.bundle = bundle;
        return this;
    }

    @JsonIgnore
    public int getDay() {
        return oneTimeDate.get(Calendar.DAY_OF_MONTH);
    }

    //Calendar returns month counted from 0
    @JsonIgnore
    public int getMonth() {
        return oneTimeDate.get(Calendar.MONTH) + 1;
    }

    @JsonIgnore
    public int getYear() {
        return oneTimeDate.get(Calendar.YEAR);
    }

    @Override
    public String toString() {
        return "ScheduleInfo{" +
                "periodic=" + periodic +
                ", periodType=" + periodType +
                ", number=" + number +
                ", weekdays=" + weekdays +
                ", oneTimeDate=" + oneTimeDate +
                ", time='" + time + '\'' +
                '}';
    }
}
