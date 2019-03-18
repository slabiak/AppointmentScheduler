package com.example.slabiak.appointmentscheduler.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasksService {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private InvoiceService invoiceService;

    // check db every 1 hour
    @Scheduled(fixedDelay = 1000)
    public void updateAllAppointmentsStatuses(){
     appointmentService.updateAllAppointmentsStatuses();
    }

    @Scheduled(cron = "0 0 0 1 * ?") // runs on the first day of each month
    public void issueInvoicesForCurrnetMonth(){
        invoiceService.issueInvoicesForConfirmedAppointments();
    }


}
