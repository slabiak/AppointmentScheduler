package com.example.slabiak.appointmentscheduler.controller;

import com.example.slabiak.appointmentscheduler.entity.Appointment;
import com.example.slabiak.appointmentscheduler.security.CustomUserDetails;
import com.example.slabiak.appointmentscheduler.service.AppointmentService;
import com.example.slabiak.appointmentscheduler.service.ExchangeService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/exchange")
public class ExchangeController {

    private final ExchangeService exchangeService;
    private final AppointmentService appointmentService;

    public ExchangeController(ExchangeService exchangeService, AppointmentService appointmentService) {
        this.exchangeService = exchangeService;
        this.appointmentService = appointmentService;
    }

    @GetMapping("/{oldAppointmentId}")
    public String showEligibleAppointmentsToExchange(@PathVariable("oldAppointmentId") int oldAppointmentId, Model model) {
        List<Appointment> eligibleAppointments = exchangeService.getEligibleAppointmentsForExchange(oldAppointmentId);
        model.addAttribute("appointmentId", oldAppointmentId);
        model.addAttribute("numberOfEligibleAppointments", eligibleAppointments.size());
        model.addAttribute("eligibleAppointments", eligibleAppointments);
        return "exchange/listProposals";
    }

    @GetMapping("/{oldAppointmentId}/{newAppointmentId}")
    public String showExchangeSummaryScreen(@PathVariable("oldAppointmentId") int oldAppointmentId, @PathVariable("newAppointmentId") int newAppointmentId, Model model, @AuthenticationPrincipal CustomUserDetails currentUser) {
        if (exchangeService.checkIfExchangeIsPossible(oldAppointmentId, newAppointmentId, currentUser.getId())) {
            model.addAttribute("oldAppointment", appointmentService.getAppointmentByIdWithAuthorization(oldAppointmentId));
            model.addAttribute("newAppointment", appointmentService.getAppointmentById(newAppointmentId));
        } else {
            return "redirect:/appointments/all";
        }

        return "exchange/exchangeSummary";
    }

    @PostMapping()
    public String processExchangeRequest(@RequestParam("oldAppointmentId") int oldAppointmentId, @RequestParam("newAppointmentId") int newAppointmentId, Model model, @AuthenticationPrincipal CustomUserDetails currentUser) {
        boolean result = exchangeService.requestExchange(oldAppointmentId, newAppointmentId, currentUser.getId());
        if (result) {
            model.addAttribute("message", "Exchange request sucsessfully sent!");
        } else {
            model.addAttribute("message", "Error! Exchange not sent!");
        }
        return "exchange/requestConfirmation";
    }

    @PostMapping("/accept")
    public String processExchangeAcceptation(@RequestParam("exchangeId") int exchangeId, Model model, @AuthenticationPrincipal CustomUserDetails currentUser) {
        exchangeService.acceptExchange(exchangeId, currentUser.getId());
        return "redirect:/appointments/all";
    }

    @PostMapping("/reject")
    public String processExchangeRejection(@RequestParam("exchangeId") int exchangeId, Model model, @AuthenticationPrincipal CustomUserDetails currentUser) {
        exchangeService.rejectExchange(exchangeId, currentUser.getId());
        return "redirect:/appointments/all";
    }
}
