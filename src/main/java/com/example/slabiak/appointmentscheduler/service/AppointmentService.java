package com.example.slabiak.appointmentscheduler.service;

import com.example.slabiak.appointmentscheduler.entity.Appointment;
import com.example.slabiak.appointmentscheduler.entity.User;
import com.example.slabiak.appointmentscheduler.model.AppointmentRegisterForm;

import java.util.List;

public interface AppointmentService {
    void save(AppointmentRegisterForm appointment);
    Appointment findById(int id);
    List<Appointment> findAll();
    void deleteById(int id);
    List<Appointment> findByCustomer(User user);
    List<Appointment> findByProvider(User user);
}
