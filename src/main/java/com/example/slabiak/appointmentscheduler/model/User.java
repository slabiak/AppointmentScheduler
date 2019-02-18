package com.example.slabiak.appointmentscheduler.model;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;


@Entity
@Table(name="users")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "username")
    private String userName;

    @Column(name = "password")
    private String password;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Collection<Role> roles;

    @OneToMany(mappedBy = "customer")
    private List<Appointment> appointmentsByCustomer;

    @OneToMany(mappedBy = "provider")
    private List<Appointment> appointmentsByProvider;

    @ManyToMany
    @JoinTable(name="works_providers", joinColumns=@JoinColumn(name="id_user"), inverseJoinColumns=@JoinColumn(name="id_work"))
    private List<Work> works;

}
