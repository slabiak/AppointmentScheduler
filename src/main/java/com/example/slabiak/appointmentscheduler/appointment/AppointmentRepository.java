package com.example.slabiak.appointmentscheduler.appointment;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentRepository  extends JpaRepository<Appointment, Integer> {
}
