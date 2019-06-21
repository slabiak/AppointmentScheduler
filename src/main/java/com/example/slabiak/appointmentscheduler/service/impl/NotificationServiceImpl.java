package com.example.slabiak.appointmentscheduler.service.impl;

import com.example.slabiak.appointmentscheduler.dao.NotificationRepository;
import com.example.slabiak.appointmentscheduler.entity.Appointment;
import com.example.slabiak.appointmentscheduler.entity.Invoice;
import com.example.slabiak.appointmentscheduler.entity.Notification;
import com.example.slabiak.appointmentscheduler.entity.user.User;
import com.example.slabiak.appointmentscheduler.service.EmailService;
import com.example.slabiak.appointmentscheduler.service.NotificationService;
import com.example.slabiak.appointmentscheduler.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private JwtTokenServiceImpl jwtTokenService;

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
        if(notification.getUser().getId()==userId) {
            notification.setRead(true);
            notificationRepository.save(notification);
        } else{
            throw new org.springframework.security.access.AccessDeniedException("Unauthorized");
        }
    }

    @Override
    public void markAllAsRead(int userId) {
        List<Notification> notifications = notificationRepository.getAllUnreadNotifications(userId);
        for(Notification notification : notifications){
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
        String message = "Appointment finished, you can reject that it took place until "+ appointment.getEnd().plusHours(24).toString();
        String url = "/appointments/" + appointment.getId();
        newNotification(title,message,url,appointment.getCustomer());
        if(sendEmail){
            emailService.sendAppointmentFinishedNotification(appointment);
        }

    }

    @Override
    public void newAppointmentRejectionRequestedNotification(Appointment appointment, boolean sendEmail) {
        String title = "Appointment Rejected";
        String message =  appointment.getCustomer().getFirstName()+ " " + appointment.getCustomer().getLastName()+ "rejected an appointment. Your approval is required";
        String url = "/appointments/" + appointment.getId();
        newNotification(title,message,url,appointment.getProvider());
        if(sendEmail){
            emailService.sendAppointmentRejectionRequestedNotification(appointment);
        }
    }

    @Override
    public void newNewAppointmentScheduledNotification(Appointment appointment, boolean sendEmail) {
        String title = "New appointment scheduled";
        String message = "New appointment scheduled with" + appointment.getCustomer().getFirstName() + " " + appointment.getProvider().getLastName() + " on " + appointment.getStart().toString();
        String url = "/appointments/"+appointment.getId();
        newNotification(title,message,url,appointment.getProvider());
        if(sendEmail){
            emailService.sendNewAppointmentScheduledNotification(appointment);
        }
    }

    @Override
    public void newAppointmentCanceledByCustomerNotification(Appointment appointment, boolean sendEmail) {
        String title = "Appointment Canceled";
        String message = appointment.getCustomer().getFirstName() + " " + appointment.getCustomer().getLastName() +" cancelled appointment scheduled at "+ appointment.getStart().toString();
        String url = "/appointments/" + appointment.getId();
        newNotification(title,message,url,appointment.getProvider());
        if(sendEmail){
            emailService.sendAppointmentCanceledByCustomerNotification(appointment);
        }
    }

    @Override
    public void newAppointmentCanceledByProviderNotification(Appointment appointment, boolean sendEmail) {
        String title = "Appointment Canceled";
        String message = appointment.getProvider().getFirstName() + " " + appointment.getProvider().getLastName() +" cancelled appointment scheduled at "+ appointment.getStart().toString();
        String url = "/appointments/" + appointment.getId();
        newNotification(title,message,url,appointment.getCustomer());
        if(sendEmail){
            emailService.sendAppointmentCanceledByProviderNotification(appointment);
        }
    }

    public void newInvoice(Invoice invoice, boolean sendEmail) {
        String title = "New invoice";
        String message = "New invoice has been issued for you";
        String url = "/invoices/"+invoice.getId();
        newNotification(title,message,url,invoice.getAppointments().get(0).getCustomer());
        if(sendEmail){
            emailService.sendInvoice(invoice);
        }
    }

    @Override
    public void newAppointmentRejectionAcceptedNotification(Appointment appointment, boolean sendEmail) {
        String title = "Rejection accepted";
        String message = "You provider accepted your rejection request";
        String url = "/appointments/"+appointment.getId();
        newNotification(title,message,url,appointment.getCustomer());
        if(sendEmail){
            emailService.sendAppointmentRejectionAcceptedNotification(appointment);
        }
    }

}
