package com.capgemini.pojo;

import java.time.LocalDate;
import java.util.Objects;

public class Contact {
	private int id;
	private String firstName;
	private String lastName;
	private String address;
	private String city;
	private String state;
	private String email;
	private long zip;
	private long phoneNumber;
	private LocalDate date;

	public Contact(String firstName, String lastName, String address, String city, String state, String email, long zip,
			long phoneNumber) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
		this.city = city;
		this.state = state;
		this.email = email;
		this.zip = zip;
		this.phoneNumber = phoneNumber;
	}
	
	public Contact(int id, String firstName, String lastName, String address, String city, String state, String email, long zip,
			long phoneNumber) {
		this(firstName, lastName, address, city, state, email, zip, phoneNumber);
		this.id = id;
	}
	
	public Contact(int id, String firstName, String lastName, String address, String city, String state, String email, long zip,
			long phoneNumber, LocalDate date) {
		this(id, firstName, lastName, address, city, state, email, zip, phoneNumber);
		this.date = date;
	}

	public String getFirstName() {
		return firstName;
	}

	@Override
	public String toString() {
		return "firstName=" + firstName + ", lastName=" + lastName + ", address=" + address + ", city=" + city
				+ ", state=" + state + ", email=" + email + ", zip=" + zip + ", phoneNumber=" + phoneNumber;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public long getZip() {
		return zip;
	}

	public void setZip(long zip) {
		this.zip = zip;
	}

	public long getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(long phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public LocalDate getDate() {
		return date;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof Contact)) {
			return false;
		}
		Contact contact = (Contact) obj;
		return  id == contact.getId() && firstName.equals(contact.getFirstName()) && lastName.equals(contact.getLastName());

	}
	
	@Override
	public int hashCode() {
		return Objects.hash(id, firstName, lastName, address, city, state, zip, phoneNumber, email, date);
	}
}
