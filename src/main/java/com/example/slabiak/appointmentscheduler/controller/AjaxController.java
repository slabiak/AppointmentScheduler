package com.example.slabiak.appointmentscheduler.controller;

import com.example.slabiak.appointmentscheduler.entity.Appointment;
import com.example.slabiak.appointmentscheduler.model.AppointmentRegisterForm;
import com.example.slabiak.appointmentscheduler.model.TimePeroid;
import com.example.slabiak.appointmentscheduler.security.CustomUserDetails;
import com.example.slabiak.appointmentscheduler.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RequestMapping("/api")
@RestController
public class AjaxController {

    @Autowired
    private AppointmentService appointmentService;


    @GetMapping("/user/{userId}/appointments")
    List<Appointment> getAppointmentsForUser(@PathVariable("userId") int userId,@AuthenticationPrincipal CustomUserDetails currentUser) {
        if (currentUser.hasRole("ROLE_CUSTOMER")) {
            return appointmentService.getAppointmentByCustomerId(userId);
        } else if(currentUser.hasRole("ROLE_PROVIDER"))
            return appointmentService.getAppointmentByProviderId(userId);
        else if(currentUser.hasRole("ROLE_ADMIN"))
            return appointmentService.getAllAppointments();
        else return null;
    }

    @GetMapping("/availableHours/{providerId}/{workId}/{date}")
    List<AppointmentRegisterForm> getAvailableHours(@PathVariable("providerId") int providerId, @PathVariable("workId") int workId, @PathVariable("date") String date,@AuthenticationPrincipal CustomUserDetails currentUser) {
        LocalDate localDate = LocalDate.parse(date);
        List<TimePeroid> peroids = appointmentService.getAvailableHours(providerId,currentUser.getId(),workId,localDate);
        List<AppointmentRegisterForm> appointments = new ArrayList<>();
        for(TimePeroid peroid:peroids){
            appointments.add(new AppointmentRegisterForm(workId,providerId,peroid.getStart().atDate(localDate),peroid.getEnd().atDate(localDate)));
        }
        return appointments;
    }

}
