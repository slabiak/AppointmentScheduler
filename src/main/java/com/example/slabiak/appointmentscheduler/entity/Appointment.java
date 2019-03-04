package com.example.slabiak.appointmentscheduler.entity;
import com.example.slabiak.appointmentscheduler.model.AppointmentSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="appointments")
@JsonSerialize(using = AppointmentSerializer.class)
public class Appointment extends BaseEntity implements Comparable<Appointment> {

    @Column(name="start")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime start;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @Column(name="end")
    private LocalDateTime end;

    @ManyToOne
    @JoinColumn(name="id_customer")
    private User customer;

    @ManyToOne
    @JoinColumn(name="id_provider")
    private User provider;

    @ManyToOne
    @JoinColumn(name="id_work")
    private Work work;

    public Appointment(){

    }

    public Appointment(LocalDateTime start, LocalDateTime end, User customer, User provider, Work work) {
        this.start = start;
        this.end = end;
        this.customer = customer;
        this.provider = provider;
        this.work = work;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public User getCustomer() {
        return customer;
    }

    public void setCustomer(User customer) {
        this.customer = customer;
    }

    public User getProvider() {
        return provider;
    }

    public void setProvider(User provider) {
        this.provider = provider;
    }

    public Work getWork() {
        return work;
    }

    public void setWork(Work work) {
        this.work = work;
    }

    @Override
    public int compareTo(Appointment o) {
        return this.getStart().compareTo(o.getStart());
    }
}
