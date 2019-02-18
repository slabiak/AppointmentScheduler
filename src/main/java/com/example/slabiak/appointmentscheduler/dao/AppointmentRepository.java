package com.example.slabiak.appointmentscheduler.dao;

import com.example.slabiak.appointmentscheduler.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentRepository  extends JpaRepository<Appointment, Integer> {
}
