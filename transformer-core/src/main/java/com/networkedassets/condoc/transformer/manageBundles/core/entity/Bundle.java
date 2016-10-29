package com.networkedassets.condoc.transformer.manageBundles.core.entity;

import com.fasterxml.jackson.annotation.JsonView;
import com.networkedassets.condoc.transformer.common.SourceUnit;
import com.networkedassets.condoc.transformer.manageUsers.core.entity.UserGroup;
import com.networkedassets.condoc.transformer.schedule.core.entity.ScheduleInfo;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;


@Entity
public class Bundle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true)
    private String name;

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.EAGER)
    @JsonView(Views.FullButDocItems.class)
    private Set<SourceUnit> sourceUnits = new HashSet<>();

    @OneToMany(mappedBy = "bundle", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<ScheduleInfo> scheduleInfos = new HashSet<>();
    private boolean listened;

    @ManyToMany
    @JsonView(Views.UserGroups.class)
    private Set<UserGroup> userGroups = new HashSet<>();

    public Integer getId() {
        return id;
    }

    public Bundle setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Bundle setName(String name) {
        this.name = name;
        return this;
    }

    public Set<ScheduleInfo> getScheduleInfos() {
        return scheduleInfos;
    }

    public Bundle setScheduleInfos(Set<ScheduleInfo> scheduleInfos) {
        this.scheduleInfos = scheduleInfos;
        return this;
    }

    public Bundle addScheduleInfos(Set<ScheduleInfo> schedules) {
        scheduleInfos.addAll(schedules);
        return this;
    }

    public Collection<SourceUnit> getSourceUnits() {
        return sourceUnits;
    }

    public Bundle setSourceUnits(Collection<SourceUnit> sourceUnits) {
        this.sourceUnits.clear();
        this.sourceUnits.addAll(sourceUnits);
        return this;
    }

    public Bundle addSourceUnit(SourceUnit sourceUnit) {
        sourceUnits.add(sourceUnit);
        return this;
    }

    public Bundle removeSourceUnit(SourceUnit sourceUnit) {
        sourceUnits.remove(sourceUnit);
        return null;
    }

    public boolean isListened() {
        return listened;
    }

    public Bundle setListened(boolean listened) {
        this.listened = listened;
        return this;
    }

    public Set<UserGroup> getUserGroups() {
        return userGroups;
    }

    public Bundle setUserGroups(Set<UserGroup> groups) {
        this.userGroups = groups;
        return this;
    }

    public boolean anyOfUserGroupsHasAccess(Set<UserGroup> userGroups) {
        Set<UserGroup> bundleGroups = getUserGroups();
        return userGroups.stream()
                .filter(bundleGroups::contains)
                .findAny()
                .isPresent();
    }

    public interface Views {

        interface Persisted extends SourceUnit.Views.Persisted {
        }

        interface DocItems extends SourceUnit.Views.DocItems {
        }

        interface UserGroups extends Persisted {
        }

        interface Full extends FullButDocItems, DocItems {
        }

        interface FullButDocItems extends Persisted, UserGroups {
        }

    }

}