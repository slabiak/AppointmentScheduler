package com.example.slabiak.appointmentscheduler.service;

import com.example.slabiak.appointmentscheduler.dao.InvoiceRepository;
import com.example.slabiak.appointmentscheduler.entity.Appointment;
import com.example.slabiak.appointmentscheduler.entity.Invoice;
import com.example.slabiak.appointmentscheduler.entity.user.User;
import com.example.slabiak.appointmentscheduler.util.PdfGeneratorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Optional;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    @Autowired
    InvoiceRepository invoiceRepository;

    @Autowired
    PdfGeneratorUtil pdfGeneratorUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private EmailService emailService;

    @Override
    public String generateInvoiceNumber() {
        List<Invoice> invoices = invoiceRepository.findAllIssuedInCurrentMonth(LocalDate.now().with(TemporalAdjusters.firstDayOfMonth()).atStartOfDay());
        int nextInvoiceNumber = invoices.size()+1;
        LocalDateTime today = LocalDateTime.now();
        String invoiceNumber = "FV/"+today.getYear() +"/"+today.getMonthValue()+"/"+nextInvoiceNumber;
        return invoiceNumber;
    }

    @Override
    public void save(Invoice invoice) {
        invoiceRepository.save(invoice);
    }

    @Override
    public Invoice findByAppointmentId(int appointmentId) {
        return invoiceRepository.findByAppointmentId(appointmentId);
    }

    @Override
    public Invoice findByInvoiceId(int invoiceId) {
        Optional<Invoice> result = invoiceRepository.findById(invoiceId);
       Invoice invoice = null;

        if (result.isPresent()) {
            invoice = result.get();
        }
        else {
            // todo throw new excep
        }

        return invoice;
    }

    @Override
    public List<Invoice> findAll() {
        return invoiceRepository.findAll();
    }

    @Override
    public File generateInvoicePdf(int invoiceId) {
        Invoice invoice = invoiceRepository.getOne(invoiceId);
        System.out.println("total apps in invoice:" +invoice.getAppointments().size());
        File invoicePdf = pdfGeneratorUtil.generatePdfFromInvoice(invoice);
        return invoicePdf;
    }

    @Override
    public void changeStatusToPaid(int invoiceId) {
        Invoice invoice = invoiceRepository.getOne(invoiceId);
        invoice.setStatus("paid");
        invoiceRepository.save(invoice);
    }

    @Override
    public void issueInvoicesForConfirmedAppointments() {
        List<User> customers = userService.findByRoleName("ROLE_CUSTOMER");
        for(User customer:customers){
            List<Appointment> appointmentsToIssueInvoice = appointmentService.getConfirmedAppointmentsForUser(customer.getId());
            if(appointmentsToIssueInvoice.size()>0){
                for(Appointment a: appointmentsToIssueInvoice){
                    a.setStatus("invoiced");
                }
                Invoice invoice = new Invoice(generateInvoiceNumber(),"issued",LocalDateTime.now(),appointmentsToIssueInvoice);
                save(invoice);
                emailService.sendInvoice(invoice);
            }

        }
    }
}
