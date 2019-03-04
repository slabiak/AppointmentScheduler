package com.example.slabiak.appointmentscheduler.service;

import com.example.slabiak.appointmentscheduler.entity.Appointment;
import com.example.slabiak.appointmentscheduler.entity.User;
import com.example.slabiak.appointmentscheduler.model.AppointmentRegisterForm;
import com.example.slabiak.appointmentscheduler.model.TimePeroid;
import org.apache.tomcat.jni.Local;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentService {
    void save(AppointmentRegisterForm appointment);
    Appointment findById(int id);
    List<Appointment> findAll();
    void deleteById(int id);
    List<Appointment> findByCustomer(User user);
    List<Appointment> findByProvider(User user);
    List<Appointment> findByProviderAndDate(User user, LocalDate date);
    List<Appointment> getAvailableAppointments(int providerId, int workId, LocalDate date);
    public List<TimePeroid> getProviderAvailableTimePeroids(int providerId, int workId, LocalDate date);

    void save(int workId, int providerId, int customerId, LocalDateTime start);
}
