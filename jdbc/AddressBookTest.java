package com.addbook.jdbc;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import junit.framework.Assert;

class AddressBookTest {

	@Test
	public void retreivedContacts_Should_MatchCount() {
		AddressBookService addressBookService = new AddressBookService();
		List<Contact> contactList = addressBookService.readContactData();
		Assert.assertEquals(2, contactList.size());
	}
	@Test
	public void givenName_WhenUpdatedContactInfo_ShouldSyncWithDB()  {
		AddressBookService addressBookService = new AddressBookService();
		List<Contact> contactList = addressBookService.readContactData();
		addressBookService.updateContactDetails("sreeja", "godishala");
		boolean result = addressBookService.checkContactDetailsInSyncWithDB("sreeja");
		Assert.assertTrue(result);
	}
	@Test
	public void givenAddressBookData_whenRetreivedByCity_ShouldMatchContactCount(){
		AddressBookService addressBookService = new AddressBookService();
		addressBookService.readContactData();
		LocalDate startDate = LocalDate.of(2018, 02, 06);
		LocalDate endDate = LocalDate.now();
		List<Contact> contactList = addressBookService.readContactDataForDateRange(startDate, endDate);
		Assert.assertEquals(2, contactList.size());
	}

	@Test
	public void givenAddressBookData_whenRetreivedByState_ShouldMatchContactCount() {
		AddressBookService addressBookService = new AddressBookService();
		addressBookService.readContactData();
		Map<String, Integer> contactByCityMap = addressBookService.readContactByCityOrState();
		Integer count = 2;
		Assert.assertEquals(count, contactByCityMap.get("hnk"));
	}
	@Test
	public void givenNewContact_WhenAdded_ShouldSyncWithDB() {
		AddressBookService.addNewContact("2018-08-08", "Sreeja", "Godishala", "Gopalpur", "Hnk",
				"wgl", "873485", "7289472389", "srijagodishala@gmail.com");
		boolean isSynced = AddressBookService.checkContactDetailsInSyncWithDB("Sreeja");
		assertTrue(isSynced);
	}

}
