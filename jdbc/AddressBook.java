package com.addbook.jdbc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.capgemini.addressbook.Contact;
import com.opencsv.CSVReader;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.opencsv.exceptions.CsvValidationException;

public class AddressBook {
	public static String ADDRESS_BOOK_CSV="C:\\Users\\HP\\.eclipse\\org.eclipse.tips.state\\com.addbook.jdbc\\csv.csv";
	public static void main(String[] args) throws CsvValidationException, IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
		 AddressBook addressBookCsv = new  AddressBook();
		 addressBookCsv.ReadData();
		 addressBookCsv.writeData();
	}
	private void writeData() throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
		Writer writer =Files.newBufferedWriter(Paths.get(ADDRESS_BOOK_CSV));
		StatefulBeanToCsvBuilder<Contact> csvWriter=new StatefulBeanToCsvBuilder<>(writer);
		StatefulBeanToCsv<Contact> csvBuilder=csvWriter.build();
		List<Contact> contactList=new ArrayList<>();
		contactList.add(new Contact("sreeja","Godishala","hnk","wgl","telangana","563781","6474781891","xyz.abc@gmail.com"));
		contactList.add(new Contact("abc","xyz","bankcolony","karimnagar","telangana","746387","875833748","wgl.tlgna@gmail.com"));
		csvBuilder.write(contactList);
		writer.close();
	}
	private void ReadData() throws CsvValidationException, IOException {
		BufferedReader r=Files.newBufferedReader(Paths.get(ADDRESS_BOOK_CSV));
		CSVReader csvReader=new CSVReader(r);{
			String  details[];
			while((details=csvReader.readNext())!=null) {
				System.out.println("First Name:"+details[0]);
				System.out.println("Last Name:"+details[1]);
				System.out.println(" Address:"+details[2]);
				System.out.println("City:"+details[3]);
				System.out.println("State:"+details[4]);
				System.out.println("Phone Number:"+details[5]);
				System.out.println("Zip:"+details[6]);
				System.out.println("Email:"+details[7]);
			}
		}
		
	}
}
