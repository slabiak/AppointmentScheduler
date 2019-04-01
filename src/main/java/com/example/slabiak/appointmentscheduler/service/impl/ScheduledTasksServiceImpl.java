package com.example.slabiak.appointmentscheduler.service.impl;


import com.example.slabiak.appointmentscheduler.service.AppointmentService;
import com.example.slabiak.appointmentscheduler.service.InvoiceService;
import com.example.slabiak.appointmentscheduler.service.ScheduledTasksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasksServiceImpl implements ScheduledTasksService {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private InvoiceService invoiceService;

    // runs every 1 second
    @Scheduled(fixedDelay = 1000)
    @Override
    public void updateAllAppointmentsStatuses(){
     appointmentService.updateAllAppointmentsStatuses();
    }

    // runs on the first day of each month
    @Scheduled(cron = "0 0 0 1 * ?")
    @Override
    public void issueInvoicesForCurrnetMonth(){
       invoiceService.issueInvoicesForConfirmedAppointments();
    }


}
