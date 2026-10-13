package com.appplication.dto;

import java.time.LocalDate;

import com.appplication.entity.Gender;

public class UsersResponseDto {

    private Long id;
    private String username;
    private String email;
    private String mobile;
    private LocalDate dob;
    private Gender gender;
    private String address;
    private String dpUrl;

    public UsersResponseDto() {
        super();
    }

    public UsersResponseDto(Long id, String username, String email, String mobile, LocalDate dob,
                            Gender gender, String address, String dpUrl) {
        super();
        this.id = id;
        this.username = username;
        this.email = email;
        this.mobile = mobile;
        this.dob = dob;
        this.gender = gender;
        this.address = address;
        this.dpUrl = dpUrl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDpUrl() {
        return dpUrl;
    }

    public void setDpUrl(String dpUrl) {
        this.dpUrl = dpUrl;
    }

    @Override
    public String toString() {
        return "UsersResponseDto [id=" + id + ", username=" + username + ", email=" + email + ", mobile=" + mobile
                + ", dob=" + dob + ", gender=" + gender + ", address=" + address + ", dpUrl=" + dpUrl + "]";
    }
}
