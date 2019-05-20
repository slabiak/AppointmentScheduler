package com.example.slabiak.appointmentscheduler.entity;

import com.example.slabiak.appointmentscheduler.entity.user.User;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="notifications")
public class Notification extends BaseEntity {

    @Column(name="message")
    private String message;

    @Column(name="created_at")
    private Date createdAt;

    @Column(name="is_read")
    private boolean isRead;

    @ManyToOne
    @JoinColumn(name="id_user")
    private User user;

    public Notification(){}

    public Notification(String message,Date createdAt,User user){
        this.message = message;
        this.createdAt = createdAt;
        this.user = user;
        this.isRead = false;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean isRead) {
        this.isRead = isRead;
    }

}
