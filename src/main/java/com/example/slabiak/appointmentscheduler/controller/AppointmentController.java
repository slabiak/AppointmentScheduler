package com.example.slabiak.appointmentscheduler.controller;

import com.example.slabiak.appointmentscheduler.entity.Appointment;
import com.example.slabiak.appointmentscheduler.model.AppointmentRegisterForm;
import com.example.slabiak.appointmentscheduler.service.AppointmentService;
import com.example.slabiak.appointmentscheduler.service.UserService;
import com.example.slabiak.appointmentscheduler.service.WorkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/appointments")
public class AppointmentController {

    @Autowired
    WorkService workService;

    @Autowired
    UserService userService;

    @Autowired
    AppointmentService appointmentService;

    @GetMapping("")
    public String listAppointments(Model model, Authentication authentication) {
        model.addAttribute("user",userService.findByUserName(authentication.getName()));
        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_CUSTOMER"))) {
            return "appointments/customer-appointments";
        } else if(authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_PROVIDER")))
        return "appointments/provider-appointments";
        else{
            return "home";
        }
    }


    @GetMapping("/select_service")
  public String selectService(Model model) {
        model.addAttribute("works", workService.findAll());
        model.addAttribute("appointmentForm", new AppointmentRegisterForm());
        return "appointments/select-service";
    }

   @PostMapping("/select_provider")
    public String selectProvider(@ModelAttribute("appointmentForm") AppointmentRegisterForm appointmentForm, Model model) {
        model.addAttribute("providers", userService.findByWorks(workService.findById(appointmentForm.getWorkId())));
        model.addAttribute(appointmentForm);
        return "appointments/select-provider";
    }

    @PostMapping("/select_date")
    public String selectDate(@ModelAttribute("appointmentForm") AppointmentRegisterForm appointmentForm, Model model){
        model.addAttribute(appointmentForm);
        return "appointments/select-date";
    }

    @PostMapping("/save")
    public String saveAppointment(@ModelAttribute("appointmentForm") AppointmentRegisterForm appointmentForm, Authentication authentication){
        Appointment appointment = new Appointment();
        appointment.setCustomer(userService.findByUserName(authentication.getName()));
        appointment.setProvider(userService.findById(appointmentForm.getProviderId()));
        appointment.setWork(workService.findById(appointmentForm.getWorkId()));
        appointment.setStart(appointmentForm.getStart());
        appointment.setEnd(appointmentForm.getEnd());
        appointmentService.save(appointment);
        return "redirect:/customers/";
    }

}
