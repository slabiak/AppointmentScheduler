package com.example.slabiak.appointmentscheduler.entity;
import com.example.slabiak.appointmentscheduler.model.AppointmentSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/*
statuses:
scheduled   =>  finished    =>  confirmed   =>  invoiced.
            | => canceled.  |=> denied.
*/


@Entity
@Table(name="appointments")
@JsonSerialize(using = AppointmentSerializer.class)
public class Appointment extends BaseEntity implements Comparable<Appointment> {

    @Column(name="start")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime start;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @Column(name="end")
    private LocalDateTime end;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @Column(name="canceled_at")
    private LocalDateTime canceledAt;

    @OneToOne
    @JoinColumn(name="id_canceler")
    private User canceler;

    @Column(name="status")
    private String status;

    @ManyToOne
    @JoinColumn(name="id_customer")
    private User customer;

    @ManyToOne
    @JoinColumn(name="id_provider")
    private User provider;

    @ManyToOne
    @JoinColumn(name="id_work")
    private Work work;

    @OneToMany(mappedBy = "appointment")
    private List<ChatMessage> chatMessages;

    @ManyToOne
    @JoinColumn(name="id_invoice")
    private Invoice invoice;

    public Appointment(){

    }

    public Appointment(LocalDateTime start, LocalDateTime end, User customer, User provider, Work work) {
        this.start = start;
        this.end = end;
        this.customer = customer;
        this.provider = provider;
        this.work = work;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public User getCustomer() {
        return customer;
    }

    public void setCustomer(User customer) {
        this.customer = customer;
    }

    public User getProvider() {
        return provider;
    }

    public void setProvider(User provider) {
        this.provider = provider;
    }

    public Work getWork() {
        return work;
    }

    public void setWork(Work work) {
        this.work = work;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<ChatMessage> getChatMessages() {

        Collections.sort(chatMessages);
        return chatMessages;
    }

    public User getCanceler() {
        return canceler;
    }

    public void setCanceler(User canceler) {
        this.canceler = canceler;
    }

    public void setChatMessages(List<ChatMessage> chatMessages) {
        this.chatMessages = chatMessages;
    }

    public LocalDateTime getCanceledAt() {
        return canceledAt;
    }

    public void setCanceledAt(LocalDateTime canceledAt) {
        this.canceledAt = canceledAt;
    }

    @Override
    public int compareTo(Appointment o) {
        return this.getStart().compareTo(o.getStart());
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }
}
