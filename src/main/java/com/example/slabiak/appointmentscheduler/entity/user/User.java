package com.example.slabiak.appointmentscheduler.entity.user;

import com.example.slabiak.appointmentscheduler.entity.BaseEntity;
import com.example.slabiak.appointmentscheduler.entity.Notification;
import com.example.slabiak.appointmentscheduler.model.UserForm;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;
import java.util.Objects;


@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
public class User extends BaseEntity {

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

    @Column(name = "mobile")
    private String mobile;

    @Column(name = "street")
    private String street;

    @Column(name = "city")
    private String city;

    @Column(name = "postcode")
    private String postcode;

    @OneToMany(mappedBy = "user")
    private List<Notification> notifications;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Collection<Role> roles;

    public User() {
    }

    public User(UserForm newUserForm, String encryptedPassword, Collection<Role> roles) {
        this.setUserName(newUserForm.getUserName());
        this.setFirstName(newUserForm.getFirstName());
        this.setLastName(newUserForm.getLastName());
        this.setEmail(newUserForm.getEmail());
        this.setCity(newUserForm.getCity());
        this.setStreet(newUserForm.getStreet());
        this.setPostcode(newUserForm.getPostcode());
        this.setMobile(newUserForm.getMobile());
        this.password = encryptedPassword;
        this.roles = roles;
    }

    public void update(UserForm updateData) {
        this.setEmail(updateData.getEmail());
        this.setFirstName(updateData.getFirstName());
        this.setLastName(updateData.getLastName());
        this.setMobile(updateData.getMobile());
        this.setCity(updateData.getCity());
        this.setStreet(updateData.getStreet());
        this.setPostcode(updateData.getPostcode());
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


    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public boolean hasRole(String roleName) {
        for (Role role : roles) {
            if (role.getName().equals(roleName)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return this.getId().equals(user.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.getId());
    }

    public List<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
    }
}
