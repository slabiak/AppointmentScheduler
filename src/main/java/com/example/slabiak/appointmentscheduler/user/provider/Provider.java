package com.example.slabiak.appointmentscheduler.user.provider;

import com.example.slabiak.appointmentscheduler.appointment.Appointment;
import com.example.slabiak.appointmentscheduler.model.User;
import com.example.slabiak.appointmentscheduler.work.Work;

import javax.persistence.*;
import java.util.List;


@Entity
@Table(name="users")
public class Provider extends User {

    @OneToMany(mappedBy = "provider")
    private List<Appointment> appointments;

    @ManyToMany
    @JoinTable(name="work_provider", joinColumns=@JoinColumn(name="provider_id"), inverseJoinColumns=@JoinColumn(name="work_id"))
    private List<Work> works;

}
