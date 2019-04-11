package com.example.slabiak.appointmentscheduler.service.impl;

import com.example.slabiak.appointmentscheduler.entity.Appointment;
import com.example.slabiak.appointmentscheduler.entity.Invoice;
import com.example.slabiak.appointmentscheduler.service.EmailService;
import com.example.slabiak.appointmentscheduler.util.PdfGeneratorUtil;
import org.springframework.beans.factory.annotation.Autowired;
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


@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private SpringTemplateEngine templateEngine;

    @Autowired
    private JwtTokenServiceImpl jwtTokenService;

    @Autowired
    private PdfGeneratorUtil pdfGenaratorUtil;

    @Async
    @Override
    public void sendEmail(String to, String subject,String templateName, Context templateContext,File attachment) {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = null;
        try {
            helper = new MimeMessageHelper(message,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());

            String html = templateEngine.process("email/"+templateName, templateContext);

            helper.setTo(to);
            helper.setFrom("AppointmentScheduler");
            helper.setSubject(subject);
            helper.setText(html, true);

            if(attachment!=null){
                helper.addAttachment("invoice",attachment);
            }

            javaMailSender.send(message);

        } catch (MessagingException e) {
            e.printStackTrace();
        }

    }
    @Async
    @Override
    public void sendAppointmentFinishedNotification(Appointment appointment) {
        Context context = new Context();
        context.setVariable("appointment",appointment);
        context.setVariable("url", "http://localhost:8080/appointments/reject?token="+jwtTokenService.generateAppointmentRejectionToken(appointment));
        sendEmail(appointment.getCustomer().getEmail(),"Finished appointment summary","appointmentFinished",context,null);
    }
    @Async
    @Override
    public void sendAppointmentRejectionRequestedNotification(Appointment appointment) {
        Context context = new Context();
        context.setVariable("appointment",appointment);
        context.setVariable("url", "http://localhost:8080/appointments/acceptRejection?token="+jwtTokenService.generateAcceptRejectionToken(appointment));
        sendEmail(appointment.getProvider().getEmail(),"Rejection requested","appointmentRejectionRequested",context,null);
    }
    @Async
    @Override
    public void sendNewAppointmentScheduledNotification(Appointment appointment) {
        Context context = new Context();
        context.setVariable("appointment",appointment);
        sendEmail(appointment.getProvider().getEmail(),"New appointment booked","newAppointmentScheduled",context,null);
    }
    @Async
    @Override
    public void sendAppointmentCanceledByCustomerNotification(Appointment appointment) {
        Context context = new Context();
        context.setVariable("appointment",appointment);
        context.setVariable("canceler","customer");
        sendEmail(appointment.getProvider().getEmail(),"Appointment canceled by Customer","appointmentCanceled",context,null);
    }
    @Async
    @Override
    public void sendAppointmentCanceledByProviderNotification(Appointment appointment) {
        Context context = new Context();
        context.setVariable("appointment",appointment);
        context.setVariable("canceler","provider");
        sendEmail(appointment.getCustomer().getEmail(),"Appointment canceled by Provider","appointmentCanceled",context,null);
    }

    @Async
    @Override
    public void sendInvoice(Invoice invoice) {
        Context context = new Context();
        context.setVariable("customer",invoice.getAppointments().get(0).getCustomer().getFirstName()+" "+invoice.getAppointments().get(0).getCustomer().getLastName());
        try {
            File invoicePdf =pdfGenaratorUtil.generatePdfFromInvoice(invoice);
            sendEmail(invoice.getAppointments().get(0).getCustomer().getEmail(),"Appointment invoice","appointmentInvoice",context,invoicePdf);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    @Async
    @Override
    public void sendAppointmentRejectionAcceptedNotification(Appointment appointment) {
            Context context = new Context();
            context.setVariable("appointment",appointment);
            sendEmail(appointment.getCustomer().getEmail(),"Rejection request accepted","appointmentRejectionAccepted",context,null);
    }
}
