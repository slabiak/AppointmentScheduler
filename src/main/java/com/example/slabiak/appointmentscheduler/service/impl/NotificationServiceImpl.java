package com.example.slabiak.appointmentscheduler.service.impl;

import com.example.slabiak.appointmentscheduler.dao.NotificationRepository;
import com.example.slabiak.appointmentscheduler.entity.Notification;
import com.example.slabiak.appointmentscheduler.entity.user.User;
import com.example.slabiak.appointmentscheduler.service.NotificationService;

import java.util.Date;

public class NotificationServiceImpl implements NotificationService {

    private NotificationRepository notificationRepository;

    @Override
    public void notify(String message, User user) {
        Notification notification = new Notification();
        notification.setCreatedAt(new Date());
        notification.setMessage(message);
        notification.setUser(user);
    }

    @Override
    public void markAsRead(int notificationId) {
        Notification notification = notificationRepository.getOne(notificationId);
        notification.setRead(true);
        notificationRepository.save(notification);
    }

}
