package com.capgemini.addressbookservicetest;

import static org.junit.Assert.*;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.junit.Test;

import com.capgemini.addressbookservice.AddressBookService;
import com.capgemini.addressbookservice.AddressBookService.IOTYPE;
import com.capgemini.pojo.AddressBook;
import com.capgemini.pojo.AddressBook.TYPE;
import com.capgemini.pojo.Contact;
import com.google.gson.Gson;

public class AddressBookServiceTest {

	@Test
	public void givenAddressBookDB_WhenRetrieved_ShouldMatchTotalEntries() {
		AddressBookService addressBookService = new AddressBookService();
		int enteries = addressBookService.readAddressBook();
		assertEquals(7, enteries);
	} 
	
	@Test
	public void givenNewPhoneNumberForAContact_WhenUpdated_ShouldBeUpdatedInTheDB() {
		AddressBookService addressBookService = new AddressBookService();
		addressBookService.readAddressBook();
		addressBookService.updateContactPhoneNumber(1, 9876543210L);
		boolean result = addressBookService.isContactInSyncWithDB(2);
		assertTrue(result);
	}
	
	@Test
	public void givenDateRange_WhenRetrievedFromDB_ShouldMatchTotalEntries() {
		AddressBookService addressBookService = new AddressBookService();
		addressBookService.readAddressBook();
		LocalDate startDate = LocalDate.of(2019, 01, 01);
		LocalDate endDate = LocalDate.now();
		LinkedList<Contact> contactInGivenDateRange = addressBookService.readContactForDateRange(IOTYPE.DB_IO, startDate, endDate); 
		assertEquals(3, contactInGivenDateRange.size());
	}
	
	@Test
	public void givenAddressBookDB_WhenRetrievedContactCountByCity_ShouldReturnCorrectResult() {
		AddressBookService addressBookService = new AddressBookService();
		Map<String, Integer> contactCountByCity = addressBookService.getContactCountByCityFromDB();
		assertEquals(2, (int) contactCountByCity.get("Jaipur")); 
		assertEquals(1, (int) contactCountByCity.get("Mumbai"));
		assertEquals(1, (int) contactCountByCity.get("New Delhi"));
		assertEquals(1, (int) contactCountByCity.get("Hyderabad"));
	}
	
	@Test
	public void givenAddressBookDB_WhenRetrievedContactCountByState_ShouldReturnCorrectResult() {
		AddressBookService addressBookService = new AddressBookService();
		Map<String, Integer> contactCountByState = addressBookService.getContactCountByStateFromDB();
		assertEquals(2, (int) contactCountByState.get("Rajasthan")); 
		assertEquals(1, (int) contactCountByState.get("Maharasthra"));
		assertEquals(1, (int) contactCountByState.get("Delhi"));
		assertEquals(1, (int) contactCountByState.get("Tamilnadu"));
	}
	
	@Test
	public void givenNewContact_WhenAddedToDB_ShouldBeInSyncWithDB() {
		AddressBookService addressBookService = new AddressBookService();
		addressBookService.readAddressBook();
		addressBookService.addContactToDB("Parth", "Agarwal", "WhiteField", "Bangalore", "Karnataka", "parth.agarwal@gmail.com",
											235678L, 7890653487L, LocalDate.of(2020, 10, 29), "book2", "book3");
		boolean result = addressBookService.isContactInSyncWithDB(6);
		assertTrue(result);
	}
	
	@Test
	public void givenMultipleContacts_WhenAddedToDB_ShouldBeInSyncWithDB() {
		Map<String[], Contact> contacts = new HashMap<String[], Contact>();
		Contact contact1 = new Contact(0,"Suchi", "Maheshwari", "Shahibaug", "Ahmedabad", "Gujarat",
					"suchi.maheshwari@gmail.com", 326845L, 9967290817L, LocalDate.now());
		Contact contact2 = new Contact(0,"Vedant", "Dave", "Matunga Road", "Nashik", "Maharashtra",
				"vedant.dave@gmail.com", 268486L, 9967290813L, LocalDate.now());
		Contact contact3 = new Contact(0,"Varun", "Poddar", "Bhagat Singh Road", "Patna", "Bihar",
				"varun.poddar@gmail.com", 867856L, 9967786814L, LocalDate.now());
		contacts.put(new String[]{"book1", "book2"}, contact1);
		contacts.put(new String[]{"book1", "book3"}, contact2);
		contacts.put(new String[]{"book3"}, contact3);
		AddressBookService addressBookService = new AddressBookService();
		addressBookService.readAddressBook();
		Instant start = Instant.now();
		boolean result = addressBookService.addContactToDB(contacts);
		Instant end = Instant.now();
		System.out.println("Duration for complete execution: " + Duration.between(start, end));
		assertTrue(result);
	}
}
