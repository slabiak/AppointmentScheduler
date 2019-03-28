package com.example.slabiak.appointmentscheduler.controller;

import com.example.slabiak.appointmentscheduler.security.CustomUserDetails;
import com.example.slabiak.appointmentscheduler.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

@Controller
@RequestMapping("/invoices")
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    @GetMapping("/all")
    public String showAllInvoices(Model model) {
        model.addAttribute("invoices",invoiceService.getAllInvoices());
        return "invoices/listInvoices";
    }

    @RequestMapping("/paid/{invoiceId}")
    public String changeStatusToPaid(@PathVariable("invoiceId") int invoiceId){
        invoiceService.changeInvoiceStatusToPaid(invoiceId);
        return "redirect:/invoices/all";
    }

    @RequestMapping("/download/{invoiceId}")
    public ResponseEntity<InputStreamResource> downloadInvoice(@PathVariable("invoiceId") int invoiceId,@AuthenticationPrincipal CustomUserDetails currentUser) {
            try {
                File invoicePdf = invoiceService.generatePdfForInvoice(invoiceId);
                HttpHeaders respHeaders = new HttpHeaders();
                MediaType mediaType = MediaType.parseMediaType("application/pdf");
                respHeaders.setContentType(mediaType);
                respHeaders.setContentLength(invoicePdf.length());
                respHeaders.setContentDispositionFormData("attachment", invoicePdf.getName());
                InputStreamResource isr = new InputStreamResource(new FileInputStream(invoicePdf));
                return new ResponseEntity<InputStreamResource>(isr, respHeaders, HttpStatus.OK);
            }
            catch (FileNotFoundException e) {
                return new ResponseEntity<InputStreamResource>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
}
