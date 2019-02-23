package com.example.slabiak.appointmentscheduler.controller;

import com.example.slabiak.appointmentscheduler.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class HomeController {

    @Autowired
    UserService userService;

    @GetMapping("/")
    public String showHome(Model model) {
        return "home";
    }

    @GetMapping("/login")
    public String login(Model model, Principal principal) {
        if(principal !=null){
             return "redirect:/";
        }
        return "user/login";
    }

    @GetMapping("/access-denied")
    public String showAccessDenied() {

        return "user/access-denied";

    }

}
