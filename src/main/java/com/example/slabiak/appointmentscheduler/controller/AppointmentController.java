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
import org.springframework.web.bind.annotation.*;

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
    public String showAllAppointments(Model model, Authentication authentication) {
        model.addAttribute("user",userService.findByUserName(authentication.getName()));
        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_CUSTOMER"))) {
            return "appointments/customer-appointments";
        } else if(authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_PROVIDER")))
        return "appointments/provider-appointments";
        else{
            return "home";
        }
    }

    @GetMapping("/{id}")
    public String showAppointmentDetail(@PathVariable("id") int id, Model model, Authentication authentication) {
        model.addAttribute("appointment", appointmentService.findById(id));
            return "appointments/appointmentDetail";
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
        appointmentForm.setCustomerId(userService.findByUserName(authentication.getName()).getId());
        appointmentService.save(appointmentForm);
        return "redirect:/customers/";
    }

    @PostMapping("/cancel")
    public String cancelAppointment(@RequestParam("id") int id, Authentication authentication){
        appointmentService.deleteById(id);
        return "redirect:/appointments";
    }

}
