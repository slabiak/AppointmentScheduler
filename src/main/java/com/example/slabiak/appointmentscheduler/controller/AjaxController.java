package com.example.slabiak.appointmentscheduler.controller;

import com.example.slabiak.appointmentscheduler.dao.InvoiceRepository;
import com.example.slabiak.appointmentscheduler.entity.Appointment;
import com.example.slabiak.appointmentscheduler.model.AppointmentRegisterForm;
import com.example.slabiak.appointmentscheduler.model.TimePeroid;
import com.example.slabiak.appointmentscheduler.security.CustomUserDetails;
import com.example.slabiak.appointmentscheduler.service.AppointmentService;
import com.example.slabiak.appointmentscheduler.service.EmailService;
import com.example.slabiak.appointmentscheduler.service.JwtTokenService;
import com.example.slabiak.appointmentscheduler.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
        if (currentUser.hasRole("ROLE_CUSTOMER")) {
            return userService.findById(userId).getAppointmentsByCustomer();
        } else if(currentUser.hasRole("ROLE_PROVIDER"))
            return userService.findById(userId).getAppointmentsByProvider();
        else if(currentUser.hasRole("ROLE_ADMIN"))
            return appointmentService.findAll();
        else return null;
    }

    @GetMapping("/hours/{userId}/{workId}/{date}")
    List<AppointmentRegisterForm> getAvailableHours(@PathVariable("userId") int userId, @PathVariable("workId") int workId, @PathVariable("date") String date) {
        LocalDate d = LocalDate.parse(date);
        List<TimePeroid> peroids = appointmentService.getProviderAvailableTimePeroids(userId,workId,d);
        List<AppointmentRegisterForm> appointments = new ArrayList<>();
        for(TimePeroid peroid:peroids){
            appointments.add(new AppointmentRegisterForm(workId,userId,peroid.getStart().atDate(d),peroid.getEnd().atDate(d)));
        }
        return appointments;
    }





}
