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
	
	@Test
	public void givenAddressBookJsonServer_WhenRetrieved_ShouldMatchTotalContactCount() {
		AddressBookService addressBookService = new AddressBookService(getAddressBooks());
		int enteries = addressBookService.getContactCount();	
		assertEquals(3, enteries);
	}
	
	@Test
	public void givenAddressBook_WhenAddedToJsonServer_ShouldBeInSyncWithTheServer() {
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
}