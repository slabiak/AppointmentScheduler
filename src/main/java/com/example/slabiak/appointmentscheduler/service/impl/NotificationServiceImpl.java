package com.example.slabiak.appointmentscheduler.service.impl;

import com.example.slabiak.appointmentscheduler.dao.NotificationRepository;
import com.example.slabiak.appointmentscheduler.entity.*;
import com.example.slabiak.appointmentscheduler.entity.user.User;
import com.example.slabiak.appointmentscheduler.service.EmailService;
import com.example.slabiak.appointmentscheduler.service.NotificationService;
import com.example.slabiak.appointmentscheduler.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserService userService;
    private final EmailService emailService;
    private final boolean mailingEnabled;

    public NotificationServiceImpl(@Value("${mailing.enabled}") boolean mailingEnabled, NotificationRepository notificationRepository, UserService userService, EmailService emailService) {
        this.mailingEnabled = mailingEnabled;
        this.notificationRepository = notificationRepository;
        this.userService = userService;
        this.emailService = emailService;
    }

    @Override
    public void newNotification(String title, String message, String url, User user) {
        Notification notification = new Notification();
        notification.setTitle(title);
        notification.setUrl(url);
        notification.setCreatedAt(new Date());
        notification.setMessage(message);
        notification.setUser(user);
        notificationRepository.save(notification);
    }


    @Override
    public void markAsRead(int notificationId, int userId) {
        Notification notification = notificationRepository.getOne(notificationId);
        if (notification.getUser().getId() == userId) {
            notification.setRead(true);
            notificationRepository.save(notification);
        } else {
            throw new org.springframework.security.access.AccessDeniedException("Unauthorized");
        }
    }

    @Override
    public void markAllAsRead(int userId) {
        List<Notification> notifications = notificationRepository.getAllUnreadNotifications(userId);
        for (Notification notification : notifications) {
            notification.setRead(true);
            notificationRepository.save(notification);
        }
    }

    @Override
    public Notification getNotificationById(int notificationId) {
        return notificationRepository.getOne(notificationId);
    }

    @Override
    public List<Notification> getAll(int userId) {
        return userService.getUserById(userId).getNotifications();
    }

    @Override
    public List<Notification> getUnreadNotifications(int userId) {
        return notificationRepository.getAllUnreadNotifications(userId);
    }

    @Override
    public void newAppointmentFinishedNotification(Appointment appointment, boolean sendEmail) {
        String title = "Appointment Finished";
        String message = "Appointment finished, you can reject that it took place until " + appointment.getEnd().plusHours(24).toString();
        String url = "/appointments/" + appointment.getId();
        newNotification(title, message, url, appointment.getCustomer());
        if (sendEmail && mailingEnabled) {
            emailService.sendAppointmentFinishedNotification(appointment);
        }

    }

    @Override
    public void newAppointmentRejectionRequestedNotification(Appointment appointment, boolean sendEmail) {
        String title = "Appointment Rejected";
        String message = appointment.getCustomer().getFirstName() + " " + appointment.getCustomer().getLastName() + "rejected an appointment. Your approval is required";
        String url = "/appointments/" + appointment.getId();
        newNotification(title, message, url, appointment.getProvider());
        if (sendEmail && mailingEnabled) {
            emailService.sendAppointmentRejectionRequestedNotification(appointment);
        }
    }

    @Override
    public void newNewAppointmentScheduledNotification(Appointment appointment, boolean sendEmail) {
        String title = "New appointment scheduled";
        String message = "New appointment scheduled with" + appointment.getCustomer().getFirstName() + " " + appointment.getProvider().getLastName() + " on " + appointment.getStart().toString();
        String url = "/appointments/" + appointment.getId();
        newNotification(title, message, url, appointment.getProvider());
        if (sendEmail && mailingEnabled) {
            emailService.sendNewAppointmentScheduledNotification(appointment);
        }
    }

    @Override
    public void newAppointmentCanceledByCustomerNotification(Appointment appointment, boolean sendEmail) {
        String title = "Appointment Canceled";
        String message = appointment.getCustomer().getFirstName() + " " + appointment.getCustomer().getLastName() + " cancelled appointment scheduled at " + appointment.getStart().toString();
        String url = "/appointments/" + appointment.getId();
        newNotification(title, message, url, appointment.getProvider());
        if (sendEmail && mailingEnabled) {
            emailService.sendAppointmentCanceledByCustomerNotification(appointment);
        }
    }

    @Override
    public void newAppointmentCanceledByProviderNotification(Appointment appointment, boolean sendEmail) {
        String title = "Appointment Canceled";
        String message = appointment.getProvider().getFirstName() + " " + appointment.getProvider().getLastName() + " cancelled appointment scheduled at " + appointment.getStart().toString();
        String url = "/appointments/" + appointment.getId();
        newNotification(title, message, url, appointment.getCustomer());
        if (sendEmail && mailingEnabled) {
            emailService.sendAppointmentCanceledByProviderNotification(appointment);
        }
    }

    public void newInvoice(Invoice invoice, boolean sendEmail) {
        String title = "New invoice";
        String message = "New invoice has been issued for you";
        String url = "/invoices/" + invoice.getId();
        newNotification(title, message, url, invoice.getAppointments().get(0).getCustomer());
        if (sendEmail && mailingEnabled) {
            emailService.sendInvoice(invoice);
        }
    }

    @Override
    public void newExchangeRequestedNotification(Appointment oldAppointment, Appointment newAppointment, boolean sendEmail) {
        String title = "Request for exchange";
        String message = "One of the users sent you a request to exchange his appointment with your appointment";
        String url = "/appointments/" + newAppointment.getId();
        newNotification(title, message, url, newAppointment.getCustomer());
        if (sendEmail && mailingEnabled) {
            emailService.sendNewExchangeRequestedNotification(oldAppointment, newAppointment);
        }
    }

    @Override
    public void newExchangeAcceptedNotification(ExchangeRequest exchangeRequest, boolean sendEmail) {
        String title = "Exchange request accepted";
        String message = "Someone accepted your appointment exchange request from " + exchangeRequest.getRequested().getStart() + " to " + exchangeRequest.getRequestor().getStart();
        String url = "/appointments/" + exchangeRequest.getRequested();
        newNotification(title, message, url, exchangeRequest.getRequested().getCustomer());
        if (sendEmail && mailingEnabled) {
            emailService.sendExchangeRequestAcceptedNotification(exchangeRequest);
        }
    }

    @Override
    public void newExchangeRejectedNotification(ExchangeRequest exchangeRequest, boolean sendEmail) {
        String title = "Exchange request rejected";
        String message = "Someone rejected your appointment exchange request from " + exchangeRequest.getRequestor().getStart() + " to " + exchangeRequest.getRequested().getStart();
        String url = "/appointments/" + exchangeRequest.getRequestor();
        newNotification(title, message, url, exchangeRequest.getRequestor().getCustomer());
        if (sendEmail && mailingEnabled) {
            emailService.sendExchangeRequestRejectedNotification(exchangeRequest);
        }
    }

    @Override
    public void newAppointmentRejectionAcceptedNotification(Appointment appointment, boolean sendEmail) {
        String title = "Rejection accepted";
        String message = "You provider accepted your rejection request";
        String url = "/appointments/" + appointment.getId();
        newNotification(title, message, url, appointment.getCustomer());
        if (sendEmail && mailingEnabled) {
            emailService.sendAppointmentRejectionAcceptedNotification(appointment);
        }
    }

    @Override
    public void newChatMessageNotification(ChatMessage chatMessage, boolean sendEmail) {
        String title = "New chat message";
        String message = "You have new chat message from " + chatMessage.getAuthor().getFirstName() + " regarding appointment scheduled at " + chatMessage.getAppointment().getStart();
        String url = "/appointments/" + chatMessage.getAppointment().getId();
        newNotification(title, message, url, chatMessage.getAuthor() == chatMessage.getAppointment().getProvider() ? chatMessage.getAppointment().getCustomer() : chatMessage.getAppointment().getProvider());
        if (sendEmail && mailingEnabled) {
            emailService.sendNewChatMessageNotification(chatMessage);
        }
    }

}
