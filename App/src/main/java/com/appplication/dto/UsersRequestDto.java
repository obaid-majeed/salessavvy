package com.appplication.dto;

import java.time.LocalDate;

import com.appplication.entity.Gender;

public class UsersRequestDto {
	private String username;
    private String email;
    private String password;
    private String mobile;
    private LocalDate dob;
    private Gender gender;
    private String address;
    private String dpUrl;
	public UsersRequestDto() {
		super();
		// TODO Auto-generated constructor stub
	}
	public UsersRequestDto(String username, String email, String password, String mobile, LocalDate dob, Gender gender,
			String address, String dpUrl) {
		super();
		this.username = username;
		this.email = email;
		this.password = password;
		this.mobile = mobile;
		this.dob = dob;
		this.gender = gender;
		this.address = address;
		this.dpUrl = dpUrl;
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
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
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
		return "UsersRequestDto [username=" + username + ", email=" + email + ", password=" + password + ", mobile="
				+ mobile + ", dob=" + dob + ", gender=" + gender + ", address=" + address + ", dpUrl=" + dpUrl + "]";
	}
    
    
}
