package com.example.slabiak.appointmentscheduler.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="works")
public class Work extends BaseEntity {

    @Column(name="name")
    private String name;

    @Column(name="description")
    private String description;

    @Column(name="price")
    private double price;

    @Column(name="duation")
    private int duration;

    @ManyToMany
    @JoinTable(name="works_providers", joinColumns=@JoinColumn(name="id_work"), inverseJoinColumns=@JoinColumn(name="id_user"))
    private List<User> users;

}
