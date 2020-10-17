package com.example.slabiak.appointmentscheduler.service.impl;

import com.example.slabiak.appointmentscheduler.dao.InvoiceRepository;
import com.example.slabiak.appointmentscheduler.entity.Appointment;
import com.example.slabiak.appointmentscheduler.entity.AppointmentStatus;
import com.example.slabiak.appointmentscheduler.entity.Invoice;
import com.example.slabiak.appointmentscheduler.entity.user.customer.Customer;
import com.example.slabiak.appointmentscheduler.security.CustomUserDetails;
import com.example.slabiak.appointmentscheduler.service.AppointmentService;
import com.example.slabiak.appointmentscheduler.service.InvoiceService;
import com.example.slabiak.appointmentscheduler.service.NotificationService;
import com.example.slabiak.appointmentscheduler.service.UserService;
import com.example.slabiak.appointmentscheduler.util.PdfGeneratorUtil;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final PdfGeneratorUtil pdfGeneratorUtil;
    private final UserService userService;
    private final AppointmentService appointmentService;
    private final NotificationService notificationService;

    public InvoiceServiceImpl(InvoiceRepository invoiceRepository, PdfGeneratorUtil pdfGeneratorUtil, UserService userService, AppointmentService appointmentService, NotificationService notificationService) {
        this.invoiceRepository = invoiceRepository;
        this.pdfGeneratorUtil = pdfGeneratorUtil;
        this.userService = userService;
        this.appointmentService = appointmentService;
        this.notificationService = notificationService;
    }

    @Override
    public String generateInvoiceNumber() {
        List<Invoice> invoices = invoiceRepository.findAllIssuedInCurrentMonth(LocalDate.now().with(TemporalAdjusters.firstDayOfMonth()).atStartOfDay());
        int nextInvoiceNumber = invoices.size() + 1;
        LocalDateTime today = LocalDateTime.now();
        return "FV/" + today.getYear() + "/" + today.getMonthValue() + "/" + nextInvoiceNumber;
    }

    @Override
    public void createNewInvoice(Invoice invoice) {
        invoiceRepository.save(invoice);
    }

    @Override
    public Invoice getInvoiceByAppointmentId(int appointmentId) {
        return invoiceRepository.findByAppointmentId(appointmentId);
    }

    @Override
    public Invoice getInvoiceById(int invoiceId) {
        return invoiceRepository.findById(invoiceId)
                .orElseThrow(RuntimeException::new);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public List<Invoice> getAllInvoices() {
        return invoiceRepository.findAll();
    }

    @Override
    public File generatePdfForInvoice(int invoiceId) {
        CustomUserDetails currentUser = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Invoice invoice = invoiceRepository.getOne(invoiceId);
        if (!isUserAllowedToDownloadInvoice(currentUser, invoice)) {
            throw new org.springframework.security.access.AccessDeniedException("Unauthorized");
        }
        return pdfGeneratorUtil.generatePdfFromInvoice(invoice);
    }

    @Override
    public boolean isUserAllowedToDownloadInvoice(CustomUserDetails user, Invoice invoice) {
        int userId = user.getId();
        if (user.hasRole("ROLE_ADMIN")) {
            return true;
        }
        for (Appointment a : invoice.getAppointments()) {
            if (a.getProvider().getId() == userId || a.getCustomer().getId() == userId) {
                return true;
            }
        }
        return false;
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void changeInvoiceStatusToPaid(int invoiceId) {
        Invoice invoice = invoiceRepository.getOne(invoiceId);
        invoice.setStatus("paid");
        invoiceRepository.save(invoice);
    }

    @Transactional
    @Override
    public void issueInvoicesForConfirmedAppointments() {
        List<Customer> customers = userService.getAllCustomers();
        for (Customer customer : customers) {
            List<Appointment> appointmentsToIssueInvoice = appointmentService.getConfirmedAppointmentsByCustomerId(customer.getId());
            if (!appointmentsToIssueInvoice.isEmpty()) {
                for (Appointment a : appointmentsToIssueInvoice) {
                    a.setStatus(AppointmentStatus.INVOICED);
                    appointmentService.updateAppointment(a);
                }
                Invoice invoice = new Invoice(generateInvoiceNumber(), "issued", LocalDateTime.now(), appointmentsToIssueInvoice);
                invoiceRepository.save(invoice);
                notificationService.newInvoice(invoice, true);
            }

        }
    }
}
