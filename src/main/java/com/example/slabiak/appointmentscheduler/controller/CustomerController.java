package com.example.slabiak.appointmentscheduler.controller;

import com.example.slabiak.appointmentscheduler.model.UserFormDTO;
import com.example.slabiak.appointmentscheduler.entity.user.customer.CorporateCustomer;
import com.example.slabiak.appointmentscheduler.entity.user.customer.Customer;
import com.example.slabiak.appointmentscheduler.entity.user.customer.RetailCustomer;
import com.example.slabiak.appointmentscheduler.entity.user.User;
import com.example.slabiak.appointmentscheduler.security.CustomUserDetails;
import com.example.slabiak.appointmentscheduler.service.AppointmentService;
import com.example.slabiak.appointmentscheduler.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    UserService userService;

    @Autowired
    AppointmentService appointmentService;

    @GetMapping("")
    public String showAllCustomers(Model model) {
        model.addAttribute("customers", userService.getAllCustomers());
        return "users/listCustomers";
    }

    @GetMapping("/{id}")
    public String showCustomerDetails(@PathVariable int id, Model model) {
        Customer customer = userService.getCustomerById(id);
        if(customer.hasRole("ROLE_CUSTOMER_CORPORATE")){
            CorporateCustomer corporateCustomer = userService.getCorporateCustomerById(id);
            model.addAttribute("user", new UserFormDTO(corporateCustomer));
            model.addAttribute("account_type","customer_corporate");
        } else if(customer.hasRole("ROLE_CUSTOMER_RETAIL")) {
            RetailCustomer retailCustomer = userService.getRetailCustomerById(id);
            model.addAttribute("user", new UserFormDTO(retailCustomer));
            model.addAttribute("account_type", "customer_retail");
        }
        model.addAttribute("action1","/customers/update/profile");
        model.addAttribute("action2","/customers/update/password");
        model.addAttribute("numberOfScheduledAppointments",appointmentService.getNumberOfScheduledAppointmentsForUser(id));
        model.addAttribute("numberOfCanceledAppointments",appointmentService.getNumberOfCanceledAppointmentsForUser(id));
        return "users/updateUserForm";
    }

    @PostMapping("/update/profile")
    public String processCustomerProfileUpdate(@ModelAttribute("user") UserFormDTO user, Model model) {
        Customer customer = userService.getCustomerById(user.getId());
        if(customer.hasRole("ROLE_CUSTOMER_CORPORATE")) {
            userService.updateCorporateCustomerProfile(user);
        } else if(customer.hasRole("ROLE_CUSTOMER_RETAIL")){
            userService.updateRetailCustomerProfile(user);
        }
        return "redirect:/customers/"+user.getId();
    }

    @GetMapping("/new/{customer_type}")
    public String showCustomerRegistrationForm(@PathVariable("customer_type")String customerType, Model model,@AuthenticationPrincipal CustomUserDetails currentUser) {
        if(currentUser !=null){
            return "redirect:/";
        }
        if(customerType.equals("corporate")){
            model.addAttribute("account_type","customer_corporate");
        } else if(customerType.equals("retail")){
            model.addAttribute("account_type","customer_retail");
        } else {
            throw new RuntimeException();
        }
        model.addAttribute("action","/customers/new/"+customerType);
        model.addAttribute("user", new UserFormDTO());
        return "users/createUserForm";
    }


    @PostMapping("/new/{customer_type}")
    public String processCustomerRegistration(@PathVariable("customer_type")String customerType, @ModelAttribute("user") UserFormDTO userForm, Model model) {
        User user = userService.findByUserName(userForm.getUserName());
        if (user != null){
            model.addAttribute("user", userForm);
            model.addAttribute("account_type","customer_" + customerType);
            model.addAttribute("action","/customers/new/"+customerType);
            model.addAttribute("registrationError", "User name already exists.");
            return "users/createUserForm";
        }
        if(customerType.equals("corporate")){
            userService.saveNewCorporateCustomer(userForm);
        } else if(customerType.equals("retail")){
            userService.saveNewRetailCustomer(userForm);
        }
        model.addAttribute("createdUserName",userForm.getUserName());
        return "users/login";
    }


    @PostMapping("/update/password")
    public String processCustomerPasswordUpate(@ModelAttribute("user") UserFormDTO userForm, @AuthenticationPrincipal CustomUserDetails currentUser, Model model) {
        Customer customer = userService.getCustomerById(currentUser.getId());
        boolean passwordChanged = userService.updateUserPassword(userForm);
        return "redirect:/customers/"+currentUser.getId();
    }

    @PostMapping("/delete")
    public String processCustomerDeletion(@RequestParam("customerId") int customerId) {
        userService.deleteById(customerId);
        return "redirect:/customers";
    }

}
