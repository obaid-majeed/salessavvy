package com.appplication.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="products")
public class Products {
	// add Lombok later

        @Id
        @GeneratedValue(strategy=GenerationType.IDENTITY)
	Long id;
	@Column(nullable=false, unique=true)
	String name;
	@Column(nullable=false)
	int price;
	@Column(nullable=false, length=255)
	String description;
	@Column(nullable=false)
	String photoUrl;
	public Products() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Products(String name, int price, String description, String photoUrl) {
		this.name = name;
		this.price = price;
		this.description = description;
		this.photoUrl = photoUrl;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getPhotoUrl() {
		return photoUrl;
	}
	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}
	@Override
	public String toString() {
		return "Products [id=" + id + ", name=" + name + ", price=" + price + ", description=" + description
				+ ", PhotoUrl=" + photoUrl + "]";
	}
	
	
	
}
