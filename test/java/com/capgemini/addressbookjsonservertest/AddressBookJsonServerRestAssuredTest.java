package com.capgemini.addressbookjsonservertest;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.capgemini.addressbookservice.AddressBookService;
import com.capgemini.addressbookservice.AddressBookService.IOTYPE;
import com.capgemini.pojo.AddressBook;
import com.capgemini.pojo.AddressBook.TYPE;
import com.capgemini.pojo.Contact;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class AddressBookJsonServerRestAssuredTest {

	@Before
	public void SetUp() {
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = 3000;
	} 

	private LinkedList<Contact> getContacts(String bookName){
		Response response = RestAssured.get("/" + bookName);
		Contact[] contacts = new Gson().fromJson(response.asString(), Contact[].class);
		return new LinkedList<Contact>(Arrays.asList(contacts));
	}

	private Map<String, AddressBook> getAddressBooks() {
		Map<String, TYPE> addressBookTypeMap = new HashMap<String, AddressBook.TYPE>() {
			{
				put("book1", TYPE.PROFESSION);
				put("book2", TYPE.FRIEND);
				put("book3", TYPE.FAMILY);
			}
		};
		Map<String, AddressBook> addressBooks = new HashMap<String, AddressBook>();
		int[] id = { 1 };
		addressBookTypeMap.entrySet().stream().forEach(entry -> {
			AddressBook addressbook = new AddressBook(id[0], entry.getKey(), entry.getValue(),
					getContacts(entry.getKey()));
			addressBooks.put(addressbook.getName(), addressbook);
			id[0]++;
		});
		return addressBooks;
	}

	private Response addContactToAddressBook(Contact contact, String addressBookName) {
		String jsonString = new Gson().toJson(contact);
		RequestSpecification request = RestAssured.given();	
		request.header("Content-Type","application/json");
		request.body(jsonString);
		return request.post("/"+ addressBookName); 
	}

	private Map<Integer, Boolean> addMultipleContactsToJSONServer(Map<String, Contact[]> contactsToAdd) {
		Map<String, Boolean> contactInsertionStatus = new HashMap<String, Boolean>();
		Map<Integer, Boolean> contactAdditionStatusCodes = new HashMap<Integer, Boolean>();
		contactsToAdd.entrySet().forEach(entry -> {
			String bookName = entry.getKey();
			Contact[] contacts = entry.getValue();
			contactInsertionStatus.put(bookName, false);
			Runnable task = () -> {
				for (Contact contact : contacts) {
					Response response = addContactToAddressBook(contact, bookName);
					int statusCode = response.getStatusCode();
					if (statusCode == 201) {
						String responseAsString = response.asString();
						JsonObject jsonObject = new Gson().fromJson(responseAsString, JsonObject.class);
						int id = jsonObject.get("id").getAsInt();
						contact.setId(id);
						contactAdditionStatusCodes.put(contact.hashCode(), true);
					} else {
						contactAdditionStatusCodes.put(contact.hashCode(), true);
					}
				}
				contactInsertionStatus.put(bookName, true);
			};
			Thread thread = new Thread(task);
			thread.start();
		});
		
		while (contactInsertionStatus.containsValue(false)) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				System.out.println(e.getMessage());
			}
		}
		return contactAdditionStatusCodes;
	}

	@Test
	public void givenAddressBookJsonServer_WhenRetrieved_ShouldMatchTotalContactCount() {
		AddressBookService addressBookService = new AddressBookService(getAddressBooks());
		int enteries = addressBookService.getContactCount();	
		assertEquals(3, enteries);
	}

	@Test
	public void givenAddressBook_WhenAddedToJsonServer_ShouldReturnCorrectStatusCode() {
		AddressBookService addressBookService = new AddressBookService(getAddressBooks());
		Contact contact = new Contact(0, "Tushar", "Chandra", "Shahibaug", "Ahmedabad", "Gujarat",
									  "tushar.chandra@gmail.com", 386549L, 7511548678L, LocalDate.now());
		Response response = addContactToAddressBook(contact, "book1");
		int statusCode = response.getStatusCode();
		assertEquals(201, statusCode);
		String responseAsString = response.asString();
		JsonObject jsonObject = new Gson().fromJson(responseAsString, JsonObject.class);
		int id = jsonObject.get("id").getAsInt();
		contact.setId(id);
		addressBookService.addContactToParticularAddressBook("book1", contact, IOTYPE.REST_IO);
		int enteries = addressBookService.getContactCount();	
		assertEquals(4, enteries);
	}
	
	@Test
	public void givenMultipleAddressBook_WhenAddedToJsonServer_ShouldReturnCorrectStatusCode() {
		AddressBookService addressBookService = new AddressBookService(getAddressBooks());
		Map<String, Contact[]> contactsToAdd = new HashMap<String, Contact[]>(){{
			put("book1", new Contact[] {new Contact(0, "Vaibhavi", "Agarwal", "Andheri", "Mumbai", "Maharashtra",
									  "vaibhavi.agarwal@gmail.com", 386549L, 7511548648L, LocalDate.now()),
										new Contact(0, "Vedant", "Dave", "Patel Nagar", "New Delhi", "Delhi",
											  "vedant.dave@gmail.com", 110049L, 7511548486L, LocalDate.now())});
			put("book2", new Contact[] {new Contact(0, "Vaibhavi", "Agarwal", "Andheri", "Mumbai", "Maharashtra",
					  "vaibhavi.agarwal@gmail.com", 386549L, 7511548648L, LocalDate.now())});
			put("book3", new Contact[] {new Contact(0, "Vedant", "Dave", "Patel Nagar", "New Delhi", "Delhi",
					  "vedant.dave@gmail.com", 110049L, 7511548486L, LocalDate.now())});
		}};
		Map<Integer, Boolean> contactInsertionStatusCode = addMultipleContactsToJSONServer(contactsToAdd);
		contactsToAdd.entrySet().forEach(entry -> {
			String bookName = entry.getKey();
			Contact[] contacts = entry.getValue();
			for(Contact contact : contacts) {
				addressBookService.addContactToParticularAddressBook(bookName, contact, IOTYPE.REST_IO);
			}
		});
		assertFalse(contactInsertionStatusCode.containsValue(false));
		int enteries = addressBookService.getContactCount();	
		assertEquals(8, enteries);
	} 
	
	@Test
	public void givenNewPhoneNumber_WhenUpdated_ShouldReturnCorrectStatusCode() {
		AddressBookService addressBookService = new AddressBookService(getAddressBooks());
		int contact_id = 3;
		String bookName = "book1";
		Contact contact = addressBookService.getAddressBooks()
											.get(bookName)
											.getContacts()
											.stream()
											.filter(contactObj -> contactObj.getId() == contact_id)
											.findAny()
											.get();
		contact.setPhoneNumber(9904135236L);
		String jsonString = new Gson().toJson(contact);
		RequestSpecification request = RestAssured.given();	
		request.header("Content-Type","application/json");
		request.body(jsonString);
		Response response = request.put("/" + bookName + "/" + contact_id);
		int statusCode = response.getStatusCode();
		assertEquals(200, statusCode);
	}
	
	@Test
	public void givenAContactToDelete_WhenDeleted_ShouldMatchTheTotalCount() {
		AddressBookService addressBookService = new AddressBookService(getAddressBooks());
		int contact_id = 4;
		String bookName = "book1";
		Contact contact = addressBookService.getAddressBooks()
											.get(bookName)
											.getContacts()
											.stream()
											.filter(contactObj -> contactObj.getId() == contact_id)
											.findAny()
											.get();
		RequestSpecification request = RestAssured.given();	
		request.header("Content-Type","application/json");
		Response response = request.delete("/" + bookName + "/" + contact_id);
		int statusCode = response.getStatusCode();
		assertEquals(200, statusCode);
		
		boolean result = addressBookService.getAddressBooks()
										   .get(bookName)
										   .getContacts()
										   .remove(contact);
		assertTrue(result);
		int enteries = addressBookService.getContactCount();	
		assertEquals(7, enteries);
	}
}