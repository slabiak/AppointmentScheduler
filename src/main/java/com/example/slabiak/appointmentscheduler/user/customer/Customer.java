package com.example.slabiak.appointmentscheduler.user.customer;

import com.example.slabiak.appointmentscheduler.appointment.Appointment;
import com.example.slabiak.appointmentscheduler.model.User;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name="users")
public class Customer extends User {

    @OneToMany(mappedBy = "customer")
    private List<Appointment> appointments;
}
