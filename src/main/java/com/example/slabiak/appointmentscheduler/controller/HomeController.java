package com.example.slabiak.appointmentscheduler.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String showHome(Model model) {
        return "home";
    }

    @GetMapping("/login")
    public String login(Model model) {
        return "users/login";
    }

}
