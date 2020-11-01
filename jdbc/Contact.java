package com.addbook.jdbc;

import java.time.LocalDate;

public class Contact {
	String firstName;
	private String lastName;
	String address;
	private String city;
	private String state;
	private String zip;
	private String phoneNumber;
	private String email;
	public String addressBookName;
	public String addressBookType;
	public LocalDate date;
	//constructor
	public Contact(String firstName, String lastName, String address, String city, String state, String zip,
		String phone,String email) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
		this.city = city;
		this.state = state;
		this.zip = zip;
		this.phoneNumber = phone;
		this.email = email;
	}
	public Contact(String firstName, String lastName, String address, String city, String state, String zip,
			String phoneNo, String email, String addressBookName, String addressBookType) {
		this(firstName, lastName, address, city, state, zip, phoneNo, email);
		this.addressBookName = addressBookName;
		this.addressBookType = addressBookType;
	}
	public Contact(String firstName, String lastName, String address, String city, String state, String zip,
			String phoneNo, String email, String addressBookName, String addressBookType, LocalDate date) {
		this(firstName, lastName, address, city, state, zip, phoneNo, email, addressBookName, addressBookType);
		this.date = date;
	}
	public Contact(String firstName2, String lastName2, String address2, String city2, String state2, String zip2,
			String phoneNo, Object email2) {
		
	}
	//getters and setters
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
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getZip() {
		return zip;
	}
	public void setZip(String zip) {
		this.zip = zip;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String toString() {
		return firstName + " " + lastName + " : " + address + " : " + city + " : " + state + " : " + zip + " : "
				+ phoneNumber + " : " + email + " : "+"addressBookName"+" : "+"addressBookType"+"\n";
	}
}
