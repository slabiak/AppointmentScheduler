package com.example.slabiak.appointmentscheduler.service;


import com.example.slabiak.appointmentscheduler.entity.Appointment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ScheduledTasksService {

    @Autowired
    private AppointmentService appointmentService;

    @Scheduled(fixedDelay = 2000)
    public void findExpiredAppointmentsAndChangeTheirStatus(){
        List<Appointment> expiredAppointments = appointmentService.findExpired();
        System.out.println("expired"+expiredAppointments.size());
        for(Appointment a:expiredAppointments){
            a.setStatus("finished");
            appointmentService.update(a);
        }
    }


}
