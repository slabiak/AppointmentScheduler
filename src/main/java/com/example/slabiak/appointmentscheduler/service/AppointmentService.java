package com.example.slabiak.appointmentscheduler.service;

import com.example.slabiak.appointmentscheduler.entity.Appointment;

import java.util.List;

public interface AppointmentService {
    void save(Appointment appointment);
    Appointment findById(int id);
    List<Appointment> findAll();
    void deleteById(int id);
}
