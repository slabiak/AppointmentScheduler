package com.example.slabiak.appointmentscheduler.controller;

import com.example.slabiak.appointmentscheduler.entity.Work;
import com.example.slabiak.appointmentscheduler.model.AppointmentRegisterForm;
import com.example.slabiak.appointmentscheduler.service.WorkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/works")
public class WorkController {

    @Autowired
    WorkService workService;

    @GetMapping("")
    public String listWorks(Model model) {
        model.addAttribute("works", workService.findAll());
        return "admin/works/list-works";
    }

    @GetMapping("/new")
    public String showFormForAdd(Model model) {
        model.addAttribute("work", new Work());
        return "admin/works/new-work";
    }

    @PostMapping("/new")
    public String saveWork(@ModelAttribute("work") Work work) {
        workService.save(work);
        return "redirect:/works";
    }

    @GetMapping("/update/{workId}")
    public String showFormForAdd(@PathVariable("workId") int workId, Model model) {
        model.addAttribute("work", workService.findById(workId));
        return "admin/works/new-work";
    }

    @PostMapping("/delete")
    public String deleteWork(@RequestParam("workId") int workId){
        workService.deleteById(workId);
        return "redirect:/works";
    }


}
