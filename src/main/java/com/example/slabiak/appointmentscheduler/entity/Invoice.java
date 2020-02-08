package com.example.slabiak.appointmentscheduler.entity;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// statuses: issued,paid

@Entity
@Table(name = "invoices")
public class Invoice extends BaseEntity {

    @Column(name = "number")
    private String number;

    @Column(name = "status")
    private String status;

    @Column(name = "total_amount")
    private double totalAmount;

    @DateTimeFormat(pattern = "MM/dd/yyyy")
    @Column(name = "issued")
    private LocalDateTime issued;

    @OneToMany(mappedBy = "invoice")
    private List<Appointment> appointments;

    public Invoice() {
    }

    public Invoice(String number, String status, LocalDateTime issued, List<Appointment> appointments2) {
        this.number = number;
        this.status = status;
        this.issued = issued;
        this.appointments = new ArrayList<>();
        for (Appointment a : appointments2) {
            this.appointments.add(a);
            a.setInvoice(this);
            totalAmount += a.getWork().getPrice();
        }
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getIssued() {
        return issued;
    }

    public void setIssued(LocalDateTime issued) {
        this.issued = issued;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }
}
