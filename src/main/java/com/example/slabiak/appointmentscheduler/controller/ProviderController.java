package com.example.slabiak.appointmentscheduler.controller;

import com.example.slabiak.appointmentscheduler.entity.WorkingPlan;
import com.example.slabiak.appointmentscheduler.entity.user.User;
import com.example.slabiak.appointmentscheduler.entity.user.provider.Provider;
import com.example.slabiak.appointmentscheduler.model.TimePeroid;
import com.example.slabiak.appointmentscheduler.model.UserFormDTO;
import com.example.slabiak.appointmentscheduler.security.CustomUserDetails;
import com.example.slabiak.appointmentscheduler.service.AppointmentService;
import com.example.slabiak.appointmentscheduler.service.UserService;
import com.example.slabiak.appointmentscheduler.service.WorkService;
import com.example.slabiak.appointmentscheduler.service.WorkingPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/providers")
public class ProviderController {

    @Autowired
    private UserService userService;

    @Autowired
    private WorkService workService;

    @Autowired
    private WorkingPlanService workingPlanService;

    @Autowired
    private AppointmentService appointmentService;

    @GetMapping("")
    public String showAllProviders(Model model) {
        model.addAttribute("providers", userService.getAllProviders());
        return "users/listProviders";
    }

    @GetMapping("/new")
    public String showProviderRegistrationForm(Model model) {
        model.addAttribute("account_type","provider");
        model.addAttribute("action","/providers/new");
        model.addAttribute("user", new UserFormDTO());
        model.addAttribute("allWorks",workService.getAllWorks());
        return "users/createUserForm";
    }

    @PostMapping("/new")
    public String processProviderRegistrationForm(@ModelAttribute("user") UserFormDTO userForm, Model model) {
        User user = userService.getUserByUsername(userForm.getUserName());
        if (user != null){
            model.addAttribute("user", userForm);
            model.addAttribute("account_type","provider");
            model.addAttribute("action","/providers/new");
            model.addAttribute("registrationError", "User name already exists.");
            return "users/createUserForm";
        }
        userService.saveNewProvider(userForm);
        return "redirect:/providers";
    }

    @GetMapping("/{id}")
    public String showProviderDetails(@PathVariable("id") int id, Model model) {
        Provider provider = userService.getProviderById(id);
        model.addAttribute("user", new UserFormDTO(provider));
        model.addAttribute("account_type","provider");
        model.addAttribute("action1","/providers/update/profile");
        model.addAttribute("action2","/providers/update/password");
        model.addAttribute("allWorks", workService.getAllWorks());
        model.addAttribute("numberOfScheduledAppointments",appointmentService.getNumberOfScheduledAppointmentsForUser(id));
        model.addAttribute("numberOfCanceledAppointments",appointmentService.getNumberOfCanceledAppointmentsForUser(id));
        return "users/updateUserForm";
    }

    @PostMapping("/update/profile")
    public String processProviderUpdate(@ModelAttribute("user") UserFormDTO userUpdateData, Model model) {
        userService.updateProviderProfile(userUpdateData);
        return "redirect:/providers/"+userUpdateData.getId();
    }

    @PostMapping("/delete")
    public String processDeleteProviderRequest(@RequestParam("providerId") int providerId){
        userService.deleteUserById(providerId);
        return "redirect:/providers";
    }

    @GetMapping("/availability")
    public String showProviderAvailability(Model model,@AuthenticationPrincipal CustomUserDetails currentUser){
        model.addAttribute("plan",userService.getProviderById(currentUser.getId()).getWorkingPlan());
        model.addAttribute("breakModel", new TimePeroid());
        return "users/showOrUpdateProviderAvailability";
    }

    @PostMapping("/availability")
    public String processProviderWorkingPlanUpdate(@ModelAttribute("plan") WorkingPlan plan){
        workingPlanService.updateWorkingPlan(plan);
        return "redirect:/providers/availability";
    }

    @PostMapping("/availability/breakes/add")
    public String processProviderAddBreak(@ModelAttribute("breakModel") TimePeroid breakToAdd,@RequestParam("planId") int planId,@RequestParam("dayOfWeek") String dayOfWeek ){
        workingPlanService.addBreakToWorkingPlan(breakToAdd,planId,dayOfWeek);
        return "redirect:/providers/availability";
    }

    @PostMapping("/availability/breakes/delete")
    public String processProviderDeleteBreak(@ModelAttribute("breakModel") TimePeroid breakToDelete,@RequestParam("planId") int planId,@RequestParam("dayOfWeek") String dayOfWeek ){
        workingPlanService.deleteBreakFromWorkingPlan(breakToDelete,planId,dayOfWeek);
        return "redirect:/providers/availability";
    }

    @PostMapping("/update/password")
    public String processProviderPasswordUpate(@ModelAttribute("provider") UserFormDTO userForm, @AuthenticationPrincipal CustomUserDetails currentUser, Model model) {
        Provider provider = userService.getProviderById(currentUser.getId());
        boolean passwordChanged = userService.updateUserPassword(userForm);
        model.addAttribute(passwordChanged);
        model.addAttribute("user", new UserFormDTO(provider));
        return "users/updateUserForm";
    }




}
