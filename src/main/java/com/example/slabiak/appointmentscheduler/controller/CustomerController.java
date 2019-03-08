package com.example.slabiak.appointmentscheduler.controller;

import com.example.slabiak.appointmentscheduler.entity.User;
import com.example.slabiak.appointmentscheduler.model.UserRegisterForm;
import com.example.slabiak.appointmentscheduler.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    UserService userService;

    @GetMapping("")
    public String showAllCustomers(Model model) {
        model.addAttribute("customers", userService.findByRoleName("ROLE_CUSTOMER"));
        return "customers/list";
    }

    @GetMapping("/{id}")
    public String showCustomer(@PathVariable int id, Model model) {
        model.addAttribute("user", userService.findById(id));
        return "customers/customerDetails";
    }

    @GetMapping("/account")
    public String showCustomer(Model model, Authentication authentication) {
         model.addAttribute("user", userService.findByUserName(authentication.getName()));

        return "customers/customerDetails";
    }


    @GetMapping("/new")
    public String showCustomerRegistrationForm(Model model, Principal principal) {
        if(principal !=null){
            return "redirect:/";
        }
        model.addAttribute("user", new UserRegisterForm());
        return "customers/createCustomerForm";
    }

    @PostMapping("/new")
    public String processCustomerRegistration(@ModelAttribute("user") UserRegisterForm userForm, Model model) {
        User existing = userService.findByUserName(userForm.getUserName());
        if (existing != null){
            model.addAttribute("user", userForm);
            model.addAttribute("registrationError", "User name already exists.");
            return "customers/createCustomerForm";
        }
        userService.registerCustomer(userForm);
        model.addAttribute("createdUserName",userForm.getUserName());
        return "login";
    }

    @PostMapping("/delete")
    public String processCustomerRegistration(@RequestParam("customerId") int id) {
        userService.deleteById(id);
        return "redirect:/customers";
    }



}
