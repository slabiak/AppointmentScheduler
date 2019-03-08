package com.example.slabiak.appointmentscheduler.service;

import javax.mail.MessagingException;
import java.util.Map;

public interface EmailService {

    public void sendEmail(String to, String subject, Map model);
}
