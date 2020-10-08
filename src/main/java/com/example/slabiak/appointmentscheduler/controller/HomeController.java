package com.example.slabiak.appointmentscheduler.controller;

import com.example.slabiak.appointmentscheduler.security.CustomUserDetails;
import com.example.slabiak.appointmentscheduler.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final UserService userService;

    public HomeController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String showHome(Model model, @AuthenticationPrincipal CustomUserDetails currentUser) {
        model.addAttribute("user", userService.getUserById(currentUser.getId()));
        return "home";
    }

    @GetMapping("/login")
    public String login(Model model, @AuthenticationPrincipal CustomUserDetails currentUser) {
        if (currentUser != null) {
            return "redirect:/";
        }
        return "users/login";
    }

    @GetMapping("/access-denied")
    public String showAccessDeniedPage() {
        return "access-denied";
    }


}
