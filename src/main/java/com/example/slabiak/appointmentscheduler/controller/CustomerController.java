package com.example.slabiak.appointmentscheduler.controller;

import com.example.slabiak.appointmentscheduler.dto.UserFormDTO;
import com.example.slabiak.appointmentscheduler.entity.User;
import com.example.slabiak.appointmentscheduler.model.UserRegisterForm;
import com.example.slabiak.appointmentscheduler.security.CustomUserDetails;
import com.example.slabiak.appointmentscheduler.service.AppointmentService;
import com.example.slabiak.appointmentscheduler.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.jws.soap.SOAPBinding;

@Controller
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    UserService userService;

    @Autowired
    AppointmentService appointmentService;

    @GetMapping("")
    public String showAllCustomers(Model model) {
        model.addAttribute("customers", userService.findByRoleName("ROLE_CUSTOMER"));
        return "customers/list";
    }

    @GetMapping("/{id}")
    public String showCustomerDetails(@PathVariable int id, Model model) {
        User user = userService.findById(id);
        model.addAttribute("user", new UserFormDTO(user));
        model.addAttribute("numberOfScheduledAppointments",appointmentService.getNumberOfScheduledAppointmentsForUser(id));
        model.addAttribute("numberOfCanceledAppointments",appointmentService.getNumberOfCanceledAppointmentsForUser(id));
        return "customers/customerDetails";
    }


    @GetMapping("/new")
    public String showCustomerRegistrationForm(Model model,@AuthenticationPrincipal CustomUserDetails currentUser) {
        if(currentUser !=null){
            return "redirect:/";
        }
        model.addAttribute("user", new UserFormDTO());
        return "customers/createCustomerForm";
    }

    @PostMapping("/new")
    public String processCustomerRegistration(@ModelAttribute("user") UserFormDTO userForm, Model model) {
        User user = userService.findByUserName(userForm.getUserName());
        if (user != null){
            model.addAttribute("user", userForm);
            model.addAttribute("registrationError", "User name already exists.");
            return "customers/createCustomerForm";
        }
        userService.saveNewUser(userForm);
        model.addAttribute("createdUserName",userForm.getUserName());
        return "login";
    }

    @PostMapping("/update/profile")
    public String processCustomerProfileUpdate(@ModelAttribute("user") UserFormDTO user, Model model) {
        userService.updateUserProfile(user);
        return "redirect:/customers/"+user.getId();
    }

    @PostMapping("/update/password")
    public String processCustomerPasswordUpate(@ModelAttribute("user") UserFormDTO userFormDTO, @AuthenticationPrincipal CustomUserDetails currentUser, Model model) {
        User user = userService.findById(currentUser.getId());
        boolean passwordChanged = userService.updateUserPassword(user.getId(),userFormDTO.getCurrentPassword(),userFormDTO.getNewPassword(),userFormDTO.getMatchingPassword());
        model.addAttribute(passwordChanged);
        model.addAttribute("user", new UserFormDTO(user));
        return "customers/customerDetails";
    }

    @PostMapping("/delete")
    public String processCustomerDeletion(@RequestParam("customerId") int customerId) {
        userService.deleteById(customerId);
        return "redirect:/customers";
    }

}
