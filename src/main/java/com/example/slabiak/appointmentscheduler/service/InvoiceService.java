package com.example.slabiak.appointmentscheduler.service;

import com.example.slabiak.appointmentscheduler.entity.Invoice;

import java.io.File;
import java.util.List;

public interface InvoiceService {
    void createNewInvoice(Invoice invoice);
    Invoice getInvoiceByAppointmentId(int appointmentId);
    Invoice getInvoiceById(int invoiceId);
    List<Invoice> getAllInvoices();
    void changeInvoiceStatusToPaid(int invoiceId);
    void issueInvoicesForConfirmedAppointments();
    String generateInvoiceNumber();
    File generatePdfForInvoice(int invoiceId);
}
