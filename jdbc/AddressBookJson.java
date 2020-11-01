package com.addbook.jdbc;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.capgemini.addressbook.Contact;
import com.google.gson.Gson;

public class AddressBookJson {
	public static String HOME_JSON="C:\\Users\\HP\\.eclipse\\org.eclipse.tips.state\\com.addbook.jdbc\\json.txt";
	public static void main(String[] args) throws IOException {
		AddressBookJson addressBookJson = new AddressBookJson();
		addressBookJson.readDataJson();
		addressBookJson.writeDataJson();
	}
	//reading data
	private void readDataJson() throws IOException {
			List<Contact> contactList = new ArrayList<Contact>();
			try {
				Reader reader=Files.newBufferedReader(Paths.get(HOME_JSON));
				contactList.addAll(Arrays.asList(new Gson().fromJson(reader, Contact[].class)));
			} catch (IOException e) {
				e.printStackTrace();
			}
		
	}
	//writing data into file
	private void writeDataJson() throws IOException {
		Writer writer=Files.newBufferedWriter(Paths.get(HOME_JSON));
		List<Contact> contactList=new ArrayList<>();
		if (contactList == null) {
			contactList = new ArrayList<Contact>();
		}
		contactList.add(new Contact("sheldon","Cooper","caltech","texas","usa","758242","8983938163","sheldoncooper@gmail.com"));
		contactList.add(new Contact("penny","tbbt","factory","texas","usa","828422","4792839839","penny@gmail.com"));
		contactList.add(new Contact("lenoard","hofsdater","caltech","texas","usa","823939","24834743681","lenoardhofsdater@gmail.com"));
		new Gson().toJson(contactList, writer);
		writer.close();
	}

}
