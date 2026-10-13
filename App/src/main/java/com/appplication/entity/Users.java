package com.appplication.entity;
 

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true, length = 15)
    private String mobile;

    @Column(nullable = false)
    private LocalDate dob;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Gender gender;

    @Column(nullable = false, length = 500)
    private String address;

    @Column(length = 500)
    private String dpUrl;

	public Users() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Users(Long id, String username, String email, String password, String mobile, LocalDate dob, Gender gender,
			String address, String dpUrl) {
		super();
		this.id = id;
		this.username = username;
		this.email = email;
		this.password = password;
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
		return "Users [id=" + id + ", username=" + username + ", email=" + email + ", password=" + password
				+ ", mobile=" + mobile + ", dob=" + dob + ", gender=" + gender + ", address=" + address + ", dpUrl="
				+ dpUrl + "]";
	}
    
    
    
    

    
    
}
