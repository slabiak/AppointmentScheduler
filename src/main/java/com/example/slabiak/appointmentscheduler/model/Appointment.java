package com.example.slabiak.appointmentscheduler.model;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="appointments")
public class Appointment extends BaseEntity {

    @Column(name="start")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime start;

    @DateTimeFormat(pattern="yyyy-MM-dd'T'hh:mm")
    @Column(name="end")
    private LocalDateTime end;

    @ManyToOne
    @JoinColumn(name="customer_id")
    private User customer;

    @ManyToOne
    @JoinColumn(name="provider_id")
    private User provider;

    @ManyToOne
    @JoinColumn(name="work_id")
    private Work work;

}
