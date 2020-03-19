package com.example.slabiak.appointmentscheduler.service;

import com.example.slabiak.appointmentscheduler.entity.Appointment;

import java.util.List;

public interface ExchangeService {

    boolean checkIfEligibleForExchange(int userId, int appointmentId);

    List<Appointment> getEligibleAppointmentsForExchange(int appointmentId);

    boolean checkIfExchangeIsPossible(int oldAppointmentId, int newAppointmentId, int userId);

    boolean acceptExchange(int exchangeId, int userId);

    boolean rejectExchange(int exchangeId, int userId);

    boolean requestExchange(int oldAppointmentId, int newAppointmentId, int userId);
}
