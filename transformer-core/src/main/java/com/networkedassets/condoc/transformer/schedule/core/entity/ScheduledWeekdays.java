package com.networkedassets.condoc.transformer.schedule.core.entity;

import com.networkedassets.condoc.transformer.common.exceptions.TransformerException;

import javax.persistence.Embeddable;
import java.lang.reflect.Field;

@Embeddable
public class ScheduledWeekdays {

    private boolean mon, tue, wed, thu, fri, sat, sun;

    public boolean isMon() {
        return mon;
    }

    public void setMon(boolean mon) {
        this.mon = mon;
    }

    public boolean isTue() {
        return tue;
    }

    public void setTue(boolean tue) {
        this.tue = tue;
    }

    public boolean isWed() {
        return wed;
    }

    public void setWed(boolean wed) {
        this.wed = wed;
    }

    public boolean isThu() {
        return thu;
    }

    public void setThu(boolean thu) {
        this.thu = thu;
    }

    public boolean isFri() {
        return fri;
    }

    public void setFri(boolean fri) {
        this.fri = fri;
    }

    public boolean isSat() {
        return sat;
    }

    public void setSat(boolean sat) {
        this.sat = sat;
    }

    public boolean isSun() {
        return sun;
    }

    public void setSun(boolean sun) {
        this.sun = sun;
    }

    @Override
    public String toString() {
        String days = "";
        try {
            Field[] fields = getClass().getDeclaredFields();
            for(Field f : fields)
                if((Boolean) f.get(this))
                    days += f.getName().toUpperCase() + ",";

            if (days.isEmpty())
                days = "*";
            else //Remove comma after last day
                days = days.substring(0, days.length() - 1);

        } catch (IllegalAccessException e) {
            throw new TransformerException(e.getMessage());
        }
        return days;
    }
}