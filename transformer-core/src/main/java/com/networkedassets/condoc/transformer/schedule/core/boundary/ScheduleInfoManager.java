package com.networkedassets.condoc.transformer.schedule.core.boundary;

import com.networkedassets.condoc.transformer.schedule.core.entity.ScheduleInfo;

import java.util.List;

public interface ScheduleInfoManager {

    List<ScheduleInfo> getList();

    void remove(ScheduleInfo scheduleInfo);

}
