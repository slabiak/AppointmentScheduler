package com.example.slabiak.appointmentscheduler.appointment;

import java.util.List;

public interface AppointmentService {

    void save(Appointment appointment);
    Appointment findById(int id);
    List<Appointment> findAll();
    void deleteById(int id);
}
