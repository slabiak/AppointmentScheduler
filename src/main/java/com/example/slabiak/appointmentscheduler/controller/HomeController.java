package com.example.slabiak.appointmentscheduler.controller;

import com.example.slabiak.appointmentscheduler.dao.WorkingPlanRepository;
import com.example.slabiak.appointmentscheduler.entity.WorkingPlan;
import com.example.slabiak.appointmentscheduler.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class HomeController {

    @Autowired
    UserService userService;
    @Autowired
    WorkingPlanRepository workingPlanRepository;

    @GetMapping("/")
    public String showHome(Model model, Authentication authentication) {
        model.addAttribute("user",userService.findByUserName(authentication.getName()));

        return "home";
    }

    @GetMapping("/login")
    public String login(Model model, Principal principal) {
        WorkingPlan plan = workingPlanRepository.getOne(1);
        System.out.println(plan.getSunday().getWorkingHours().getStart());
        if(principal !=null){
             return "redirect:/";
        }
        return "login";
    }

    @GetMapping("/access-denied")
    public String showAccessDenied() {

        return "access-denied";

    }

}
