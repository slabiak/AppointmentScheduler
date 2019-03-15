package com.example.slabiak.appointmentscheduler.dto;

import com.example.slabiak.appointmentscheduler.entity.User;
import com.example.slabiak.appointmentscheduler.entity.Work;

import java.util.List;

public class UserFormDTO {

    private int id;
    private String userName;
    private String newPassword;
    private String matchingPassword;
    private String currentPassword;
    private String firstName;
    private String lastName;
    private String email;
    private String mobile;
    private String street;
    private String city;
    private String postcode;
    private List<Work> works;
    boolean providerAccount;

    public UserFormDTO() {
    }

    public UserFormDTO(User user){
        this.id = user.getId();
        this.userName = user.getUserName();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.mobile = user.getMobile();
        this.street = user.getStreet();
        this.city = user.getCity();
        this.postcode = user.getPostcode();
        this.works = user.getWorks();
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getMatchingPassword() {
        return matchingPassword;
    }

    public void setMatchingPassword(String matchingPassword) {
        this.matchingPassword = matchingPassword;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
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

    public List<Work> getWorks() {
        return works;
    }

    public void setWorks(List<Work> selectedWorks) {
        this.works = selectedWorks;
    }

    public boolean isProviderAccount() {
        return providerAccount;
    }

    public void setProviderAccount(boolean providerAccount) {
        this.providerAccount = providerAccount;
    }
}


