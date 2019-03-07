package com.example.slabiak.appointmentscheduler.entity;

import com.example.slabiak.appointmentscheduler.model.DayPlan;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;

@TypeDefs(@TypeDef(name = "json", typeClass = JsonStringType.class))
@Entity
@Table(name = "working_plans")
public class WorkingPlan{

    @Id
    @Column(name = "id_provider")
    private int id;

    @MapsId
    @OneToOne
    @JoinColumn(name = "id_provider")
    private User provider;

    @Type(type = "json")
    @Column(columnDefinition = "json", name="monday")
    private DayPlan monday;

    @Type(type = "json")
    @Column(columnDefinition = "json", name="tuesday")
    private DayPlan tuesday;

    @Type(type = "json")
    @Column(columnDefinition = "json", name="wednesday")
    private DayPlan wednesday;

    @Type(type = "json")
    @Column(columnDefinition = "json", name="thursday")
    private DayPlan thursday;

    @Type(type = "json")
    @Column(columnDefinition = "json", name="friday")
    private DayPlan friday;

    @Type(type = "json")
    @Column(columnDefinition = "json", name="saturday")
    private DayPlan saturday;

    @Type(type = "json")
    @Column(columnDefinition = "json", name="sunday")
    private DayPlan sunday;



    public WorkingPlan() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getProvider() {
        return provider;
    }

    public void setProvider(User provider) {
        this.provider = provider;
    }

    public DayPlan getDay(String day){
        switch(day){
            case "monday":
            return monday;

            case "tuesday":
            return tuesday;

            case "wednesday":
                return wednesday;

            case "thursday":
                return thursday;

            case "friday":
                return friday;

            case "saturday":
                return saturday;

            case "sunday":
                return sunday;

                default:
                    return null;
        }
    }

    public DayPlan getMonday() {
        return monday;
    }

    public void setMonday(DayPlan monday) {
        this.monday = monday;
    }

    public DayPlan getTuesday() {
        return tuesday;
    }

    public void setTuesday(DayPlan tuesday) {
        this.tuesday = tuesday;
    }

    public DayPlan getWednesday() {
        return wednesday;
    }

    public void setWednesday(DayPlan wednesday) {
        this.wednesday = wednesday;
    }

    public DayPlan getThursday() {
        return thursday;
    }

    public void setThursday(DayPlan thursday) {
        this.thursday = thursday;
    }

    public DayPlan getFriday() {
        return friday;
    }

    public void setFriday(DayPlan friday) {
        this.friday = friday;
    }

    public DayPlan getSaturday() {
        return saturday;
    }

    public void setSaturday(DayPlan saturday) {
        this.saturday = saturday;
    }

    public DayPlan getSunday() {
        return sunday;
    }

    public void setSunday(DayPlan sunday) {
        this.sunday = sunday;
    }
}
