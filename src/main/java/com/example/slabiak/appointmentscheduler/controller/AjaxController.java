package com.example.slabiak.appointmentscheduler.controller;

import com.example.slabiak.appointmentscheduler.entity.Appointment;
import com.example.slabiak.appointmentscheduler.entity.User;
import com.example.slabiak.appointmentscheduler.entity.Work;
import com.example.slabiak.appointmentscheduler.model.ApiResponse;
import com.example.slabiak.appointmentscheduler.service.AppointmentService;
import com.example.slabiak.appointmentscheduler.service.UserService;
import com.example.slabiak.appointmentscheduler.service.WorkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api")
@RestController
public class AjaxController {

    @Autowired
    WorkService workService;

    @Autowired
    UserService userService;

    @Autowired
    AppointmentService appointmentService;

    @GetMapping("/works")
    List<Work> getWorks(){
        return workService.findAll();
    }

    @GetMapping("/users/works/{workId}")
    List<User> getUsersByWork(@PathVariable("workId") int workId){
        return userService.findByWorks(workService.findById(workId));
    }

    @GetMapping("/user/{userId}/appointments")
    List<Appointment> getAppointmentsForUser(@PathVariable("userId") int userId, Authentication authentication) {
        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_CUSTOMER"))) {
            return userService.findById(userId).getAppointmentsByCustomer();
        } else if(authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_PROVIDER")))
            return userService.findById(userId).getAppointmentsByProvider();
        else return null;

    }

}
