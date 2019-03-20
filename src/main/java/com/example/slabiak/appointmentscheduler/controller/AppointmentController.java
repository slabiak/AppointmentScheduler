package com.example.slabiak.appointmentscheduler.controller;

import com.example.slabiak.appointmentscheduler.entity.Appointment;
import com.example.slabiak.appointmentscheduler.entity.ChatMessage;
import com.example.slabiak.appointmentscheduler.security.CustomUserDetails;
import com.example.slabiak.appointmentscheduler.service.AppointmentService;
import com.example.slabiak.appointmentscheduler.service.EmailService;
import com.example.slabiak.appointmentscheduler.service.UserService;
import com.example.slabiak.appointmentscheduler.service.WorkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/appointments")
public class AppointmentController {

    @Autowired
    WorkService workService;

    @Autowired
    UserService userService;

    @Autowired
    AppointmentService appointmentService;

    @Autowired
    EmailService emailService;



    @GetMapping("")
    public String showAllAppointments(Model model, @AuthenticationPrincipal CustomUserDetails currentUser) {
        if (currentUser.hasRole("ROLE_CUSTOMER_RETAIL") || currentUser.hasRole("ROLE_CUSTOMER_CORPORATE")) {
            model.addAttribute("appointments",appointmentService.findByCustomerId(currentUser.getId()));
        } else if(currentUser.hasRole("ROLE_PROVIDER")) {
            model.addAttribute("appointments",appointmentService.findByProviderId(currentUser.getId()));
        } else if(currentUser.hasRole("ROLE_ADMIN")) {
            model.addAttribute("appointments",appointmentService.findAll());
        }
        return "appointments/listAppointments";
    }

    @GetMapping("/{id}")
    public String showAppointmentDetail(@PathVariable("id") int appointmentId, Model model,@AuthenticationPrincipal CustomUserDetails currentUser) {
        Appointment appointment = appointmentService.findById(appointmentId);
        model.addAttribute("appointment", appointment);
        model.addAttribute("chatMessage", new ChatMessage());
        boolean allowDeny = appointmentService.isUserAllowedToDenyThatAppointmentTookPlace(currentUser.getId(),appointmentId);
        boolean allowAcceptDeny = appointmentService.isUserAllowedToAcceptDeny(currentUser.getId(),appointmentId);
        model.addAttribute("allowAcceptDeny",allowAcceptDeny);
        model.addAttribute("allowDeny",allowDeny);
        if(allowDeny){
            model.addAttribute("remainingTime", formatDuration(Duration.between(LocalDateTime.now(),appointment.getEnd().plusHours(24))));
        }
        String cancelNotAllowedReason = appointmentService.getCancelNotAllowedReason(currentUser.getId(), appointmentId);
        model.addAttribute("allowCancel", cancelNotAllowedReason == null);
        model.addAttribute("cancelNotAllowedReason", cancelNotAllowedReason);
        return "appointments/appointmentDetail";
        }


    @PostMapping("/deny")
    public String denyAppointment(@RequestParam("appointmentId") int appointmentId, @AuthenticationPrincipal CustomUserDetails currentUser, Model model) {
        int customerId = currentUser.getId();
        appointmentService.denyAppointment(appointmentId,customerId);
        return "redirect:/appointments/"+appointmentId;
    }

    @GetMapping("/deny")
    public String denyAppointment(@RequestParam("token") String token, Model model) {
        boolean result = appointmentService.denyAppointment(token);
        model.addAttribute("result",result);
        return "appointments/denyConfirmation";
    }

    @PostMapping("/acceptDeny")
    public String acceptDenyAppointment(@RequestParam("appointmentId") int appointmentId, @AuthenticationPrincipal CustomUserDetails currentUser, Model model) {
        int customerId = currentUser.getId();
        appointmentService.acceptDeny(appointmentId,customerId);
        return "redirect:/appointments/"+appointmentId;
    }

    @GetMapping("/acceptDeny")
    public String acceptDenyAppointment(@RequestParam("token") String token, Model model) {
        System.out.println(token);
        boolean result = appointmentService.acceptDeny(token);
        model.addAttribute("result",result);
        return "appointments/denyConfirmation";
    }

    @PostMapping("/messages/new")
    public String addNewChatMessage(@ModelAttribute("chatMessage") ChatMessage chatMessage, @RequestParam("appointmentId") int appointmentId, @AuthenticationPrincipal CustomUserDetails currentUser) {
        int authorId = currentUser.getId();
        appointmentService.addMessageToAppointmentChat(appointmentId,authorId, chatMessage);
        return "redirect:/appointments/"+appointmentId;
    }

    @GetMapping("/new")
    public String selectProvider(Model model) {
        model.addAttribute("providers", userService.getAllProviders());
        return "appointments/select-provider";
    }

    @GetMapping("/new/{providerId}")
  public String selectService(@PathVariable("providerId") int providerId, Model model) {
        model.addAttribute("works", workService.findByProviderId(providerId));
        model.addAttribute("providerId",providerId);
        return "appointments/select-service";
    }

    @GetMapping("/new/{providerId}/{workId}")
    public String selectDate(@PathVariable("workId") int workId,@PathVariable("providerId") int providerId, Model model){
        model.addAttribute("providerId",providerId);
        model.addAttribute("workId",workId);
        return "appointments/select-date";
    }

    @GetMapping("/new/{providerId}/{workId}/{dateTime}")
    public String confirm(@PathVariable("workId") int workId,@PathVariable("providerId") int providerId,@PathVariable("dateTime") String start,Model model){
        model.addAttribute("work",workService.findById(workId));
        model.addAttribute("provider",userService.findById(providerId).getFirstName()+" " +userService.findById(providerId).getLastName());
        model.addAttribute("providerId",providerId);
        model.addAttribute("start",LocalDateTime.parse(start));
        model.addAttribute("end",LocalDateTime.parse(start).plusMinutes(workService.findById(workId).getDuration()));
        return "appointments/confirm";
    }

    @PostMapping("/new")
    public String saveAppointment(@RequestParam("workId") int workId,@RequestParam("providerId") int providerId,@RequestParam("start") String start, @AuthenticationPrincipal CustomUserDetails currentUser){
        int customerId= currentUser.getId();
        appointmentService.save(workId,providerId,customerId,LocalDateTime.parse(start));
        return "redirect:/appointments/";
    }

    @PostMapping("/cancel")
    public String cancelAppointment(@RequestParam("appointmentId") int appointmentId, @AuthenticationPrincipal CustomUserDetails currentUser){
        appointmentService.cancelById(appointmentId,currentUser.getId());
        return "redirect:/appointments";
    }


    public static String formatDuration(Duration duration) {
        long s = duration.getSeconds();
        return   String.format("%dh%02dm", s / 3600, (s % 3600) / 60);
    }

}
