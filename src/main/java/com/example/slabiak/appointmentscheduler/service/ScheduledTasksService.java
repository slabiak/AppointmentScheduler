package com.example.slabiak.appointmentscheduler.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasksService {

    @Autowired
    private AppointmentService appointmentService;

    // check db every 1 hour
    @Scheduled(fixedDelay = 1000)
    public void updateAllAppointmentsStatuses(){
     appointmentService.updateAllAppointmentsStatuses();
    }


}
