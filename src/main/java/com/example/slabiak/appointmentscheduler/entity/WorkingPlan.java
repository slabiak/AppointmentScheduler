package com.example.slabiak.appointmentscheduler.entity;

import com.example.slabiak.appointmentscheduler.entity.BaseEntity;
import com.example.slabiak.appointmentscheduler.model.Day;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity(name = "Plan")
@Table(name = "working_plans")
public class WorkingPlan extends BaseEntity {

    @Type(type = "json")
    @Column(columnDefinition = "json", name="monday")
    private Day monday;

    @Type(type = "json")
    @Column(columnDefinition = "json", name="tuesday")
    private Day tuesday;

    @Type(type = "json")
    @Column(columnDefinition = "json", name="wednesday")
    private Day wednesday;

    @Type(type = "json")
    @Column(columnDefinition = "json", name="thursday")
    private Day thursday;

    @Type(type = "json")
    @Column(columnDefinition = "json", name="friday")
    private Day friday;

    @Type(type = "json")
    @Column(columnDefinition = "json", name="saturday")
    private Day saturday;

    @Type(type = "json")
    @Column(columnDefinition = "json", name="sunday")
    private Day sunday;

    public WorkingPlan() {
    }

    public Day getMonday() {
        return monday;
    }

    public void setMonday(Day monday) {
        this.monday = monday;
    }

    public Day getTuesday() {
        return tuesday;
    }

    public void setTuesday(Day tuesday) {
        this.tuesday = tuesday;
    }

    public Day getWednesday() {
        return wednesday;
    }

    public void setWednesday(Day wednesday) {
        this.wednesday = wednesday;
    }

    public Day getThursday() {
        return thursday;
    }

    public void setThursday(Day thursday) {
        this.thursday = thursday;
    }

    public Day getFriday() {
        return friday;
    }

    public void setFriday(Day friday) {
        this.friday = friday;
    }

    public Day getSaturday() {
        return saturday;
    }

    public void setSaturday(Day saturday) {
        this.saturday = saturday;
    }

    public Day getSunday() {
        return sunday;
    }

    public void setSunday(Day sunday) {
        this.sunday = sunday;
    }
}
