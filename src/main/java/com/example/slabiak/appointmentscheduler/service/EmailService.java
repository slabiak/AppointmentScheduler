package com.example.slabiak.appointmentscheduler.service;

import com.example.slabiak.appointmentscheduler.entity.Appointment;

import javax.mail.MessagingException;
import java.util.Map;

public interface EmailService {

    public void sendEmail(String to, String subject,String template, Map model);
    public void sendFinishedAppointmentNotification(Appointment appointment);
}
