package com.example.slabiak.appointmentscheduler.service.impl;

import com.example.slabiak.appointmentscheduler.entity.Appointment;
import com.example.slabiak.appointmentscheduler.entity.ChatMessage;
import com.example.slabiak.appointmentscheduler.entity.ExchangeRequest;
import com.example.slabiak.appointmentscheduler.entity.Invoice;
import com.example.slabiak.appointmentscheduler.entity.user.User;
import com.example.slabiak.appointmentscheduler.service.EmailService;
import com.example.slabiak.appointmentscheduler.util.PdfGeneratorUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.nio.charset.StandardCharsets;

@Slf4j
@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;
    private final JwtTokenServiceImpl jwtTokenService;
    private final PdfGeneratorUtil pdfGenaratorUtil;
    private final String baseUrl;

    public EmailServiceImpl(JavaMailSender javaMailSender, SpringTemplateEngine templateEngine, JwtTokenServiceImpl jwtTokenService, PdfGeneratorUtil pdfGenaratorUtil, @Value("${base.url}") String baseUrl) {
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
        this.jwtTokenService = jwtTokenService;
        this.pdfGenaratorUtil = pdfGenaratorUtil;
        this.baseUrl = baseUrl;
    }

    @Async
    @Override
    public void sendEmail(String to, String subject, String templateName, Context templateContext, File attachment) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());

            String html = templateEngine.process("email/" + templateName, templateContext);

            helper.setTo(to);
            helper.setFrom("appointmentscheduler@gmail.com");
            helper.setSubject(subject);
            helper.setText(html, true);

            if (attachment != null) {
                helper.addAttachment("invoice", attachment);
            }

            javaMailSender.send(message);

        } catch (MessagingException e) {
            log.error("Error while adding attachment to email, error is {}", e.getLocalizedMessage());
        }

    }

    @Async
    @Override
    public void sendAppointmentFinishedNotification(Appointment appointment) {
        Context context = new Context();
        context.setVariable("appointment", appointment);
        context.setVariable("url", baseUrl + "/appointments/reject?token=" + jwtTokenService.generateAppointmentRejectionToken(appointment));
        sendEmail(appointment.getCustomer().getEmail(), "Finished appointment summary", "appointmentFinished", context, null);
    }

    @Async
    @Override
    public void sendAppointmentRejectionRequestedNotification(Appointment appointment) {
        Context context = new Context();
        context.setVariable("appointment", appointment);
        context.setVariable("url", baseUrl + "/appointments/acceptRejection?token=" + jwtTokenService.generateAcceptRejectionToken(appointment));
        sendEmail(appointment.getProvider().getEmail(), "Rejection requested", "appointmentRejectionRequested", context, null);
    }

    @Async
    @Override
    public void sendNewAppointmentScheduledNotification(Appointment appointment) {
        Context context = new Context();
        context.setVariable("appointment", appointment);
        sendEmail(appointment.getProvider().getEmail(), "New appointment booked", "newAppointmentScheduled", context, null);
    }

    @Async
    @Override
    public void sendAppointmentCanceledByCustomerNotification(Appointment appointment) {
        Context context = new Context();
        context.setVariable("appointment", appointment);
        context.setVariable("canceler", "customer");
        sendEmail(appointment.getProvider().getEmail(), "Appointment canceled by Customer", "appointmentCanceled", context, null);
    }

    @Async
    @Override
    public void sendAppointmentCanceledByProviderNotification(Appointment appointment) {
        Context context = new Context();
        context.setVariable("appointment", appointment);
        context.setVariable("canceler", "provider");
        sendEmail(appointment.getCustomer().getEmail(), "Appointment canceled by Provider", "appointmentCanceled", context, null);
    }

    @Async
    @Override
    public void sendInvoice(Invoice invoice) {
        Context context = new Context();
        context.setVariable("customer", invoice.getAppointments().get(0).getCustomer().getFirstName() + " " + invoice.getAppointments().get(0).getCustomer().getLastName());
        try {
            File invoicePdf = pdfGenaratorUtil.generatePdfFromInvoice(invoice);
            sendEmail(invoice.getAppointments().get(0).getCustomer().getEmail(), "Appointment invoice", "appointmentInvoice", context, invoicePdf);
        } catch (Exception e) {
            log.error("Error while generating pdf, error is {}", e.getLocalizedMessage());
        }

    }

    @Async
    @Override
    public void sendAppointmentRejectionAcceptedNotification(Appointment appointment) {
        Context context = new Context();
        context.setVariable("appointment", appointment);
        sendEmail(appointment.getCustomer().getEmail(), "Rejection request accepted", "appointmentRejectionAccepted", context, null);
    }

    @Async
    @Override
    public void sendNewChatMessageNotification(ChatMessage chatMessage) {
        Context context = new Context();
        User recipent = chatMessage.getAuthor() == chatMessage.getAppointment().getProvider() ? chatMessage.getAppointment().getCustomer() : chatMessage.getAppointment().getProvider();
        context.setVariable("recipent", recipent);
        context.setVariable("appointment", chatMessage.getAppointment());
        context.setVariable("url", baseUrl + "/appointments/" + chatMessage.getAppointment().getId());
        sendEmail(recipent.getEmail(), "New chat message", "newChatMessage", context, null);
    }

    @Async
    @Override
    public void sendNewExchangeRequestedNotification(Appointment oldAppointment, Appointment newAppointment) {
        Context context = new Context();
        context.setVariable("oldAppointment", oldAppointment);
        context.setVariable("newAppointment", newAppointment);
        context.setVariable("url", baseUrl + "/appointments/" + newAppointment.getId());
        sendEmail(newAppointment.getCustomer().getEmail(), "New Appointment Exchange Request", "newExchangeRequest", context, null);
    }

    @Override
    public void sendExchangeRequestAcceptedNotification(ExchangeRequest exchangeRequest) {
        Context context = new Context();
        context.setVariable("exchangeRequest", exchangeRequest);
        context.setVariable("url", baseUrl + "/appointments/" + exchangeRequest.getRequested().getId());
        sendEmail(exchangeRequest.getRequested().getCustomer().getEmail(), "Exchange request accepted", "exchangeRequestAccepted", context, null);
    }

    @Override
    public void sendExchangeRequestRejectedNotification(ExchangeRequest exchangeRequest) {
        Context context = new Context();
        context.setVariable("exchangeRequest", exchangeRequest);
        context.setVariable("url", baseUrl + "/appointments/" + exchangeRequest.getRequestor().getId());
        sendEmail(exchangeRequest.getRequestor().getCustomer().getEmail(), "Exchange request rejected", "exchangeRequestRejected", context, null);
    }
}
