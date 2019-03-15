package com.example.slabiak.appointmentscheduler.model;


import java.util.List;

public class UserRegisterForm {

    private String userName;

    private String password;

    private String matchingPassword;

    private String firstName;

    private String lastName;

    private String email;

    private String mobile;

    private String street;
    private String city;
    private String postCode;


    private List<Integer> selectedWorks;

    public UserRegisterForm(){

    }


    public UserRegisterForm(String userName, String password, String matchingPassword, String firstName, String lastName, String email) {
        this.userName = userName;
        this.password = password;
        this.matchingPassword = matchingPassword;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public UserRegisterForm(String userName, String password, String matchingPassword, String firstName, String lastName, String email, String mobile, String street, String city, String postCode) {
        this.userName = userName;
        this.password = password;
        this.matchingPassword = matchingPassword;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.mobile = mobile;
        this.street = street;
        this.city = city;
        this.postCode = postCode;
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

    public String getMatchingPassword() {
        return matchingPassword;
    }

    public void setMatchingPassword(String matchingPassword) {
        this.matchingPassword = matchingPassword;
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

    public List<Integer> getSelectedWorks() {
        return selectedWorks;
    }

    public void setSelectedWorks(List<Integer> selectedWorks) {
        this.selectedWorks = selectedWorks;
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

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }
}
