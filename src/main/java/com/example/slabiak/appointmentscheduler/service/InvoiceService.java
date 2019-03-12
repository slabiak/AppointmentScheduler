package com.example.slabiak.appointmentscheduler.service;

import com.example.slabiak.appointmentscheduler.entity.Invoice;

import java.io.File;
import java.util.List;

public interface InvoiceService {

    String generateInvoiceNumber();
    void save(Invoice invoice);
    Invoice findByAppointmentId(int appointmentId);
    Invoice findByInvoiceId(int invoiceId);
    List<Invoice> findAll();
    File generateInvoicePdf(int invoiceId);
    void changeStatusToPaid(int invoiceId);
}
