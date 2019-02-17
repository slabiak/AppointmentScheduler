package com.example.slabiak.appointmentscheduler.work;

import com.example.slabiak.appointmentscheduler.model.BaseEntity;
import com.example.slabiak.appointmentscheduler.provider.Provider;

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
    @JoinTable(name="work_provider", joinColumns=@JoinColumn(name="work_id"), inverseJoinColumns=@JoinColumn(name="provider_id"))
    private List<Provider> providers;

}
