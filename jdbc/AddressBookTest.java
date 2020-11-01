package com.addbook.jdbc;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.capgemini.addressbook.AddressBookService;
import com.capgemini.addressbook.Contact;

import junit.framework.Assert;

class AddressBookTest {

	@Test
	public void contactsWhenRetrievedFromDB_ShouldMatchCount() {
		AddressBookService addressBookService = new AddressBookService();
		List<Contact> contactList = addressBookService.readContactData();
		Assert.assertEquals(2, contactList.size());
	}
	@Test
	public void givenNewSalaryForEmployee_WhenUpdatedUsingPreparedStatement_ShouldSyncWithDB() {
		AddressBookService addressBookService = new AddressBookService();
		List<Contact> contactList = addressBookService.readContactData();
		addressBookService.updateContactDetails("sreeja", "godishala");
		boolean result = addressBookService.checkContactDetailsInSyncWithDB("sreeja");
		Assert.assertTrue(result);
	}

}
