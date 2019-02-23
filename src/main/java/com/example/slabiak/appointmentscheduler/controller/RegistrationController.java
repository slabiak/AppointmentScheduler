package com.example.slabiak.appointmentscheduler.controller;

import com.example.slabiak.appointmentscheduler.entity.User;
import com.example.slabiak.appointmentscheduler.model.UserRegisterForm;
import com.example.slabiak.appointmentscheduler.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;

@Controller
public class RegistrationController {

    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String showRegistrationForm(Model theModel, Principal principal) {
        if(principal !=null){
            return "redirect:/";
        }
        theModel.addAttribute("user", new UserRegisterForm());
        return "user/register";
    }

    @PostMapping("/register")
    public String processRegistration(@ModelAttribute("user") UserRegisterForm userForm, Model model) {
        User existing = userService.findByUserName(userForm.getUserName());
        if (existing != null){
           model.addAttribute("user", new UserRegisterForm());
            model.addAttribute("registrationError", "User name already exists.");

            return "user/register";
        }

        userService.register(userForm);
        model.addAttribute("createdUserName",userForm.getUserName());
        return "user/login";
    }



}
