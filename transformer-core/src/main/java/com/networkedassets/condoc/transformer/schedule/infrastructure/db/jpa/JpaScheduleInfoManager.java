package com.networkedassets.condoc.transformer.schedule.infrastructure.db.jpa;

import com.networkedassets.condoc.transformer.common.PersistenceManager;
import com.networkedassets.condoc.transformer.schedule.core.boundary.ScheduleInfoManager;
import com.networkedassets.condoc.transformer.schedule.core.entity.ScheduleInfo;

import java.util.List;

public class JpaScheduleInfoManager extends PersistenceManager<ScheduleInfo> implements ScheduleInfoManager {


    @Override
    public List<ScheduleInfo> getList() {
        return em.createQuery("select si from ScheduleInfo si", ScheduleInfo.class).getResultList();
    }

    @Override
    protected Class<ScheduleInfo> getEntityClass() {
        return ScheduleInfo.class;
    }
}
