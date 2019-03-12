package com.example.slabiak.appointmentscheduler.entity;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

// statuses: issued,paid

@Entity
@Table(name="invoices")
public class Invoice extends BaseEntity {

    @Column(name="number")
    private String number;

    @Column(name="status")
    private String status;

    @DateTimeFormat(pattern = "MM/dd/yyyy")
    @Column(name="issued")
    private LocalDateTime issued;

    @OneToOne
    @JoinColumn(name="id_appointment")
    private Appointment appointment;

    public Invoice(){
    }

    public Invoice(String number, String status, LocalDateTime issued, Appointment appointment) {
        this.number = number;
        this.status = status;
        this.issued = issued;
        this.appointment = appointment;
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

    public Appointment getAppointment() {
        return appointment;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

}
