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
import java.util.HashMap;
import java.util.Map;


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
    public void sendEmail(String to, String subject,String template, Map model,File attachment) {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = null;
        try {
            helper = new MimeMessageHelper(message,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());

            Context context = new Context();
            context.setVariables(model);
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
        Map<String,String> model = new HashMap<>();
        model.put("customer",appointment.getCustomer().getFirstName()+" "+appointment.getCustomer().getLastName());
        model.put("provider",appointment.getProvider().getFirstName()+" "+appointment.getProvider().getLastName());
        model.put("price",String.valueOf(appointment.getWork().getPrice()));
        model.put("start",appointment.getStart().toString());
        model.put("duration",String.valueOf(appointment.getWork().getDuration()));
        model.put("url", "http://localhost:8080/appointments/deny?token="+jwtTokenService.generateDenyTokenForAppointment(appointment));
        sendEmail(appointment.getCustomer().getEmail(),"Finished appointment summary","appointmentFinished",model,null);
    }

    @Override
    public void sendInvoice(Invoice invoice) {
        Map<String,String> model = new HashMap<>();
        model.put("customer",invoice.getAppointments().get(0).getCustomer().getFirstName()+" "+invoice.getAppointments().get(0).getCustomer().getLastName());
        try {
            File invoicePdf =pdfGenaratorUtil.generatePdfFromInvoice(invoice);
            sendEmail(invoice.getAppointments().get(0).getCustomer().getEmail(),"Appointment invoice","appointmentInvoice",model,invoicePdf);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
