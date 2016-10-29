package com.networkedassets.condoc.transformer.manageBundles.infrastructure.db.jpa;

import com.networkedassets.condoc.transformer.common.PersistenceManager;
import com.networkedassets.condoc.transformer.common.SourceUnit;
import com.networkedassets.condoc.transformer.common.exceptions.BadRequestException;
import com.networkedassets.condoc.transformer.common.exceptions.BundleNotFoundException;
import com.networkedassets.condoc.transformer.common.exceptions.SourceUnitNotFoundException;
import com.networkedassets.condoc.transformer.manageBundles.core.boundary.BundleManager;
import com.networkedassets.condoc.transformer.manageBundles.core.entity.Bundle;
import com.networkedassets.condoc.transformer.manageSourceUnits.core.boundary.SourceUnitManager;
import com.networkedassets.condoc.transformer.manageUsers.core.entity.UserGroup;
import com.networkedassets.condoc.transformer.schedule.core.boundary.ScheduleInfoManager;
import com.networkedassets.condoc.transformer.schedule.util.ScheduleUtils;
import org.quartz.JobKey;
import org.quartz.Scheduler;

import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.networkedassets.condoc.transformer.util.functional.Throwing.consumerRethrowAsTransformerException;

@Singleton
public class JpaBundleManager extends PersistenceManager<Bundle> implements BundleManager {

    @Inject
    SourceUnitManager sourceUnitManager;

    @Inject
    ScheduleInfoManager scheduleInfoManager;

    @Inject
    @Named("condocTransformerScheduler")
    Scheduler scheduler;

    @Override
    protected Class<Bundle> getEntityClass() {
        return Bundle.class;
    }

    @Override
    public void persist(Bundle bundle) {
        Set<SourceUnit> mappedSourceUnits = mergeBelongingSourceUnits(bundle);
        bundle.setSourceUnits(mappedSourceUnits);
        super.persist(bundle);
    }

    @Override
    @Transactional
    public Bundle save(int id, Bundle bundle) {
        if (bundle.getId() == null || !bundle.getId().equals(id)) {
            throw new BadRequestException("Bundle ID doesn't match REST path one");
        }
        Bundle originalBundle = getById(id).orElseThrow(BundleNotFoundException::new);
        originalBundle.setName(bundle.getName());
        originalBundle.setSourceUnits(mergeBelongingSourceUnits(bundle));

        manageNewSchedules(bundle, originalBundle);

        originalBundle.getUserGroups().clear();
        originalBundle.getUserGroups().addAll(bundle.getUserGroups());

        originalBundle.setListened(bundle.isListened());
        return merge(originalBundle);
    }

    private void manageNewSchedules(Bundle bundle, Bundle originalBundle) {
        originalBundle.getScheduleInfos().forEach(consumerRethrowAsTransformerException(scheduleInfo -> {
            scheduler.deleteJob(JobKey.jobKey(scheduleInfo.getScheduledJobName()));
            scheduleInfoManager.remove(scheduleInfo);
        }));
        originalBundle.getScheduleInfos().clear();
        originalBundle.getScheduleInfos().addAll(bundle.getScheduleInfos());
        originalBundle.getScheduleInfos().forEach(consumerRethrowAsTransformerException(scheduleInfo -> {
            scheduleInfo.setBundle(originalBundle);
            ScheduleUtils.schedule(scheduleInfo, scheduler);
        }));
    }

    private Set<SourceUnit> mergeBelongingSourceUnits(Bundle bundle) {
        if (bundle.getSourceUnits().stream().filter(su -> su.getSourceNodeIdentifier() == null).findAny().isPresent()) {
            throw new SourceUnitNotFoundException();
        }
        return bundle.getSourceUnits().stream()
                .map(su -> sourceUnitManager.get(su.getSourceNodeIdentifier()).orElse(su))
                .collect(Collectors.toSet());
    }

    @Override
    @Transactional
    public Optional<Bundle> getById(Integer id) {
        return find(id);
    }


    @Override
    @Transactional
    public List<Bundle> getAllBundles() {
        return em.createQuery("select b from Bundle b", Bundle.class).getResultList();
    }

    @Override
    @Transactional
    public void removeByIdForUserGroups(Integer id, Set<UserGroup> userGroups) {
        getByIdIfAnyGroupHasAccess(id, userGroups).ifPresent(this::remove);
    }

    @Override
    @Transactional
    public Bundle saveForUserGroups(int id, Bundle bundle, Set<UserGroup> userGroups) {
        getByIdIfAnyGroupHasAccess(id, userGroups).orElseThrow(BundleNotFoundException::new);
        return save(id, bundle);
    }

    @Override
    public Optional<Bundle> getByIdIfAnyGroupHasAccess(Integer id, Set<UserGroup> userGroups) {
        Optional<Bundle> bundle = find(id);
        return bundle.filter(b -> b.anyOfUserGroupsHasAccess(userGroups));
    }

    @Override
    public List<Bundle> getAllBundlesForUserGroups(Set<UserGroup> userGroups) {
        // todo optimize using Criteria API or JPQL (if possible)
        return em.createQuery("select b from Bundle b", Bundle.class)
                .getResultList()
                .stream()
                .filter(bundle -> bundle.anyOfUserGroupsHasAccess(userGroups))
                .collect(Collectors.toList());
    }

}
