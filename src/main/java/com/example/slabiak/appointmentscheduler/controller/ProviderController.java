package com.example.slabiak.appointmentscheduler.controller;

import com.example.slabiak.appointmentscheduler.dto.UserFormDTO;
import com.example.slabiak.appointmentscheduler.entity.User;
import com.example.slabiak.appointmentscheduler.entity.WorkingPlan;
import com.example.slabiak.appointmentscheduler.model.TimePeroid;
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
        model.addAttribute("providers", userService.findByRoleName("ROLE_PROVIDER"));
        return "providers/list";
    }


    @GetMapping("/new")
    public String showProviderRegistrationForm(Model model) {
        model.addAttribute("user", new UserFormDTO());
        model.addAttribute("allWorks", workService.findAll());
        return "providers/createProviderForm";
    }

    @PostMapping("/new")
    public String processProviderRegistration(@ModelAttribute("user") UserFormDTO userForm, Model model) {
        User existing = userService.findByUserName(userForm.getUserName());
        if (existing != null){
            model.addAttribute("user", userForm);
            model.addAttribute("registrationError", "User name already exists.");
            return "providers/createProviderForm";
        }
        userService.saveNewUser(userForm);
        return "redirect:/providers";
    }

    @GetMapping("/{id}")
    public String showProviderDetails(@PathVariable("id") int id, Model model) {
        User provider = userService.findById(id);
        model.addAttribute("provider", new UserFormDTO(provider));
        model.addAttribute("allWorks", workService.findAll());
        model.addAttribute("numberOfScheduledAppointments",appointmentService.getNumberOfScheduledAppointmentsForUser(id));
        model.addAttribute("numberOfCanceledAppointments",appointmentService.getNumberOfCanceledAppointmentsForUser(id));
        return "providers/providerDetails";
    }

    @PostMapping("/update/profile")
    public String processProviderUpdate(@ModelAttribute("user") UserFormDTO userUpdateData, Model model) {
        userService.updateUserProfile(userUpdateData);
        return "redirect:/providers/"+userUpdateData.getId();
    }

    @PostMapping("/delete")
    public String processDeleteProviderRequest(@RequestParam("providerId") int providerId){
        userService.deleteById(providerId);
        return "redirect:/providers";
    }

    @GetMapping("/availability")
    public String showProviderAvailability(Model model,@AuthenticationPrincipal CustomUserDetails currentUser){
        model.addAttribute("plan",userService.findById(currentUser.getId()).getWorkingPlan());
        model.addAttribute("breakModel", new TimePeroid());
        return "providers/showOrUpdateProviderAvailability";
    }

    @PostMapping("/availability")
    public String processProviderWorkingPlanUpdate(@ModelAttribute("plan") WorkingPlan plan){
        workingPlanService.update(plan);
        return "redirect:/providers/availability";
    }

    @PostMapping("/availability/breakes/add")
    public String processProviderAddBreak(@ModelAttribute("breakModel") TimePeroid breakToAdd,@RequestParam("planId") int planId,@RequestParam("dayOfWeek") String dayOfWeek ){
        workingPlanService.addBreak(breakToAdd,planId,dayOfWeek);
        return "redirect:/providers/availability";
    }

    @PostMapping("/availability/breakes/delete")
    public String processProviderDeleteBreak(@ModelAttribute("breakModel") TimePeroid breakToDelete,@RequestParam("planId") int planId,@RequestParam("dayOfWeek") String dayOfWeek ){
        workingPlanService.deleteBreak(breakToDelete,planId,dayOfWeek);
        return "redirect:/providers/availability";
    }

    @PostMapping("/update/password")
    public String processProviderPasswordUpate(@ModelAttribute("provider") UserFormDTO userFormDTO, @AuthenticationPrincipal CustomUserDetails currentUser, Model model) {
        User user = userService.findById(currentUser.getId());
        boolean passwordChanged = userService.updateUserPassword(user.getId(),userFormDTO.getCurrentPassword(),userFormDTO.getNewPassword(),userFormDTO.getMatchingPassword());
        model.addAttribute(passwordChanged);
        model.addAttribute("user", new UserFormDTO(user));
        return "providers/providerDetails";
    }




}
