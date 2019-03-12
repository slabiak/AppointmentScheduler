package com.example.slabiak.appointmentscheduler.controller;

import com.example.slabiak.appointmentscheduler.dao.InvoiceRepository;
import com.example.slabiak.appointmentscheduler.dao.WorkingPlanRepository;
import com.example.slabiak.appointmentscheduler.entity.Appointment;
import com.example.slabiak.appointmentscheduler.entity.User;
import com.example.slabiak.appointmentscheduler.entity.Work;
import com.example.slabiak.appointmentscheduler.entity.WorkingPlan;
import com.example.slabiak.appointmentscheduler.model.AppointmentRegisterForm;
import com.example.slabiak.appointmentscheduler.model.TimePeroid;
import com.example.slabiak.appointmentscheduler.security.CustomUserDetails;
import com.example.slabiak.appointmentscheduler.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/api")
@RestController
public class AjaxController {

    @Autowired
    UserService userService;

    @Autowired
    AppointmentService appointmentService;

    @Autowired
    EmailService emailService;

    @Autowired
    InvoiceRepository invoiceRepository;

    @Autowired
    JwtTokenService jwtTokenService;

    @GetMapping("/user/{userId}/appointments")
    List<Appointment> getAppointmentsForUser(@PathVariable("userId") int userId,@AuthenticationPrincipal CustomUserDetails currentUser) {
        if (currentUser.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_CUSTOMER"))) {
            return userService.findById(userId).getAppointmentsByCustomer();
        } else if(currentUser.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_PROVIDER")))
            return userService.findById(userId).getAppointmentsByProvider();
        else return null;
    }

    @GetMapping("/hours/{workId}/{userId}/{date}")
    List<AppointmentRegisterForm> getAvailableHours(@PathVariable("userId") int userId, @PathVariable("workId") int workId, @PathVariable("date") String date) {
        LocalDate d = LocalDate.parse(date);
        List<TimePeroid> peroids = appointmentService.getProviderAvailableTimePeroids(userId,workId,d);
        List<AppointmentRegisterForm> appointments = new ArrayList<>();
        for(TimePeroid peroid:peroids){
            appointments.add(new AppointmentRegisterForm(workId,userId,peroid.getStart().atDate(d),peroid.getEnd().atDate(d)));
        }
        return appointments;
    }


    @GetMapping("/invoice")
    String invoice(){
        emailService.sendInvoice(invoiceRepository.getOne(6));
        return "success";
    }


    @GetMapping("/date")
    String date(){
        jwtTokenService.convertLocalDateTimeToDate(LocalDateTime.now());
        return "success";
    }

    @GetMapping("/token")
    String token(){

        return jwtTokenService.generateDenyTokenForAppointment(appointmentService.findById(2));
    }




}
