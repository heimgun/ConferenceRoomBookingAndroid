package com.example.conferencebookingapp;

/**
 * @author Heidi (mostly)
 */
public class User {

    String firstName;
    String lastName;
    String username;
    String password;
    String email;
    int phone;
    String organization;
    int orgNumber;
    String street;
    String city;
    int zipCode;

    String token;


    public void setToken(String token) {
        this.token = token;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public void setOrgNumber(int orgNumber) {
        this.orgNumber = orgNumber;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setZipCode(int zipCode) {
        this.zipCode = zipCode;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getToken() {
        return token;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int getPhone() {
        return phone;
    }

    public String getOrganization() {
        return organization;
    }

    public int getOrgNumber() {
        return orgNumber;
    }

    public String getStreet() {
        return street;
    }

    public String getCity() {
        return city;
    }

    public int getZipCode() {
        return zipCode;
    }

    public String getEmail() {
        return email;
    }
}
