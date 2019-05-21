package com.example.slabiak.appointmentscheduler.service;

import com.example.slabiak.appointmentscheduler.entity.user.User;

public interface NotificationService {

    void notify(String message, User user);
    void markAsRead(int notificationId);
}
