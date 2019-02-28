package com.example.slabiak.appointmentscheduler.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;


@Entity
@Table(name="users")
public class User extends BaseEntity {

    @JsonIgnore
    @Column(name = "username")
    private String userName;

    @JsonIgnore
    @Column(name = "password")
    private String password;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @JsonIgnore
    @Column(name = "email")
    private String email;

    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Collection<Role> roles;

    @JsonIgnore
    @OneToMany(mappedBy = "customer")
    private List<Appointment> appointmentsByCustomer;

    @JsonIgnore
    @OneToMany(mappedBy = "provider")
    private List<Appointment> appointmentsByProvider;

    @JsonIgnore
    @ManyToMany
    @JoinTable(name="works_providers", joinColumns=@JoinColumn(name="id_user"), inverseJoinColumns=@JoinColumn(name="id_work"))
    private List<Work> works;

    public User(){

    }

    public User(String userName, String password, String firstName, String lastName, String email) {
        this.userName = userName;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Collection<Role> getRoles() {
        return roles;
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = roles;
    }

    public List<Appointment> getAppointmentsByCustomer() {
        return appointmentsByCustomer;
    }

    public void setAppointmentsByCustomer(List<Appointment> appointmentsByCustomer) {
        this.appointmentsByCustomer = appointmentsByCustomer;
    }

    public List<Appointment> getAppointmentsByProvider() {
        return appointmentsByProvider;
    }

    public void setAppointmentsByProvider(List<Appointment> appointmentsByProvider) {
        this.appointmentsByProvider = appointmentsByProvider;
    }

    public List<Work> getWorks() {
        return works;
    }

    public void setWorks(List<Work> works) {
        this.works = works;
    }

    @Override
    public boolean equals(Object user) {
        User compareUser = (User) user;
        if(compareUser.getUserName().equals(this.userName) &&
                compareUser.getPassword().equals(this.password) &&
                compareUser.getFirstName().equals(this.firstName) &&
                compareUser.getLastName().equals(this.lastName) &&
                compareUser.getEmail().equals(this.email) &&
                compareUser.getRoles().equals(this.roles))   return true;

        else return false;

    }
}
