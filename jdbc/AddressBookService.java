package com.addbook.jdbc;

import java.util.List;

import com.capgemini.addressbook.AddressBookDBService;
import com.capgemini.addressbook.Contact;

public class AddressBookService {
	private List<Contact> contactList;
	private AddressBookDBService addressBookDBService;

	public AddressBookService(List<Contact> contactList) {
		this();
		this.contactList = contactList;
	}

	public AddressBookService() {
		addressBookDBService = AddressBookDBService.getInstance();
	}

	public List<Contact> readContactData() {
		this.contactList = addressBookDBService.readData();
		return contactList;
	}
	public void updateContactDetails(String name, String address) {
		int result = addressBookDBService.updateEmployeeData(name, address);
		if (result == 0)
			return;
		Contact Contact = this.getContactData(name);
		if (Contact != null)
			Contact.address = address;
	}

	private Contact getContactData(String name) {
		return this.contactList.stream().filter(contact -> contact.firstName.equals(name)).findFirst().orElse(null);
	}

	public boolean checkContactDetailsInSyncWithDB(String name) {
		List<Contact> contactList = addressBookDBService.getcontactData(name);
		return contactList.get(0).equals(getContactData(name));
	}
	
}
