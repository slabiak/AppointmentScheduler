package com.example.slabiak.appointmentscheduler.service.impl;

import com.example.slabiak.appointmentscheduler.dao.AppointmentRepository;
import com.example.slabiak.appointmentscheduler.dao.ExchangeRequestRepository;
import com.example.slabiak.appointmentscheduler.entity.Appointment;
import com.example.slabiak.appointmentscheduler.entity.ExchangeRequest;
import com.example.slabiak.appointmentscheduler.entity.ExchangeStatus;
import com.example.slabiak.appointmentscheduler.service.ExchangeService;
import com.example.slabiak.appointmentscheduler.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ExchangeServiceImpl implements ExchangeService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private ExchangeRequestRepository exchangeRequestRepository;

    @Override
    public boolean checkIfEligibleForExchange(int userId, int appointmentId) {
        Appointment appointment = appointmentRepository.getOne(appointmentId);
        if (appointment.getStart().minusHours(24).isAfter(LocalDateTime.now()) && appointment.getStatus().equals("scheduled") && appointment.getCustomer().getId() == userId) {
            return true;
        }
        return false;
    }

    @Override
    public List<Appointment> getEligibleAppointmentsForExchange(int appointmentId) {
        Appointment appointmentToExchange = appointmentRepository.getOne(appointmentId);
        return appointmentRepository.getEligibleAppointmentsForExchange(LocalDateTime.now().plusHours(24), appointmentToExchange.getCustomer().getId(), appointmentToExchange.getProvider().getId(), appointmentToExchange.getWork().getId());
    }

    @Override
    public boolean checkIfExchangeIsPossible(int oldAppointmentId, int newAppointmentId, int userId) {
        Appointment oldAppointment = appointmentRepository.getOne(oldAppointmentId);
        Appointment newAppointment = appointmentRepository.getOne(newAppointmentId);
        if (oldAppointment.getCustomer().getId() == userId) {
            return oldAppointment.getWork().getId() == newAppointment.getWork().getId()
                    && oldAppointment.getProvider().getId() == newAppointment.getProvider().getId()
                    && oldAppointment.getStart().minusHours(24).isAfter(LocalDateTime.now())
                    && newAppointment.getStart().minusHours(24).isAfter(LocalDateTime.now());
        } else{
           throw new org.springframework.security.access.AccessDeniedException("Unauthorized");
        }

    }

    @Override
    public boolean requestChange(int oldAppointmentId, int newAppointmentId, int userId) {
        if(checkIfExchangeIsPossible(oldAppointmentId,newAppointmentId,userId)){
            Appointment oldAppointment = appointmentRepository.getOne(oldAppointmentId);
            Appointment newAppointment = appointmentRepository.getOne(newAppointmentId);
            oldAppointment.setStatus("exchange requested");
            appointmentRepository.save(oldAppointment);
            ExchangeRequest exchangeRequest = new ExchangeRequest(oldAppointment,newAppointment,ExchangeStatus.PENDING);
            exchangeRequestRepository.save(exchangeRequest);
            notificationService.newExchangeRequestedNotification(oldAppointment,newAppointment,true);
            return true;
        }
        return false;
    }
}
