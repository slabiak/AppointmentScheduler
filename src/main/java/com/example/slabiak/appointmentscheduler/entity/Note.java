package com.example.slabiak.appointmentscheduler.entity;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name="notes")
public class Note extends BaseEntity{

    @Column(name="created_at")
    private LocalDateTime createdAt;

    @Column(name="subject")
    private String subject;

    @Column(name="message")
    private String message;

    @ManyToOne
    @JoinColumn(name="id_author")
    private User author;

    @ManyToOne
    @JoinColumn(name="id_appointment")
    private Appointment appointment;

    public Note(){

    }

    public Note(String subject, String message){
        this.subject=subject;
        this.message = message;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }
}
