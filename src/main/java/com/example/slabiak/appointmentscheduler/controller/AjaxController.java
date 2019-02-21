package com.example.slabiak.appointmentscheduler.controller;

import com.example.slabiak.appointmentscheduler.entity.User;
import com.example.slabiak.appointmentscheduler.entity.Work;
import com.example.slabiak.appointmentscheduler.service.UserService;
import com.example.slabiak.appointmentscheduler.service.WorkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AjaxController {

    @Autowired
    WorkService workService;

    @Autowired
    UserService userService;


    @GetMapping("/api/works")
    List<Work> getWorks(){
        return workService.findAll();
    }

    @GetMapping("/api/users/works/{workId}")
    List<User> getUsersByWork(@PathVariable("workId") int workId){
        return userService.findByWorks(workService.findById(workId));
    }

}
