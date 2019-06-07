package com.example.slabiak.appointmentscheduler.service;

import com.example.slabiak.appointmentscheduler.entity.Notification;
import com.example.slabiak.appointmentscheduler.entity.user.User;

import java.util.List;

public interface NotificationService {

    void notify(String title, String message, String url, User user);
    void markAsRead(int notificationId);
    void markAllAsRead(int userId);
    Notification getNotificationById(int notificationId);
    List<Notification> getAll(int userId);
    List<Notification> getUnreadNotifications(int userId);
}
