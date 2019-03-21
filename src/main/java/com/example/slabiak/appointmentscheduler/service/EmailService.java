package com.example.slabiak.appointmentscheduler.service;

import com.example.slabiak.appointmentscheduler.entity.Appointment;
import com.example.slabiak.appointmentscheduler.entity.Invoice;
import org.thymeleaf.context.Context;

import java.io.File;
import java.util.Map;

public interface EmailService {

    void sendEmail(String to, String subject, String template, Context context, File attachment);
    void sendAppointmentFinishedNotification(Appointment appointment);
    void sendAppointmentDeniedNotification(Appointment appointment);
    void sendNewAppointmentScheduledNotification(Appointment appointment);
    void sendAppointmentCanceledNotification(Appointment appointment);
    void sendInvoice(Invoice invoice);
}
