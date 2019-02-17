package com.example.slabiak.appointmentscheduler.appointment;


import com.example.slabiak.appointmentscheduler.user.customer.Customer;
import com.example.slabiak.appointmentscheduler.model.BaseEntity;
import com.example.slabiak.appointmentscheduler.user.provider.Provider;
import com.example.slabiak.appointmentscheduler.work.Work;
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
    private Customer customer;

    @ManyToOne
    @JoinColumn(name="work_id")
    private Work work;

    @ManyToOne
    @JoinColumn(name="provider_id")
    private Provider provider;
}
