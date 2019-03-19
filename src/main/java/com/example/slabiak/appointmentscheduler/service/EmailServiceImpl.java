package com.example.slabiak.appointmentscheduler.service;

import com.example.slabiak.appointmentscheduler.entity.Appointment;
import com.example.slabiak.appointmentscheduler.entity.Invoice;
import com.example.slabiak.appointmentscheduler.util.PdfGeneratorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
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
    private JwtTokenService jwtTokenService;

    @Autowired
    PdfGeneratorUtil pdfGenaratorUtil;

    @Override
    public void sendEmail(String to, String subject,String template, Context context,File attachment) {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = null;
        try {
            helper = new MimeMessageHelper(message,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());

            String html = templateEngine.process("email/"+template, context);

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

    @Override
    public void sendFinishedAppointmentNotification(Appointment appointment) {
        Context context = new Context();
        context.setVariable("appointment",appointment);
        context.setVariable("url", "http://localhost:8080/appointments/deny?token="+jwtTokenService.generateDenyAppointmentToken(appointment));
        sendEmail(appointment.getCustomer().getEmail(),"Finished appointment summary","appointmentFinished",context,null);
    }

    @Override
    public void sendDeniedAppointmentNotification(Appointment appointment) {
        Context context = new Context();
        context.setVariable("appointment",appointment);
        context.setVariable("url", "http://localhost:8080/appointments/acceptDeny?token="+jwtTokenService.generateAcceptDenyToken(appointment));
        sendEmail(appointment.getProvider().getEmail(),"Denied apppointment","appointmentDenied",context,null);
    }


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
}
