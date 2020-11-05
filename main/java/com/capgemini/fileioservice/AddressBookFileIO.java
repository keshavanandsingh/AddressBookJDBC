package com.capgemini.fileioservice;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedList;

import com.capgemini.pojo.Contact;
import com.google.gson.Gson;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.opencsv.exceptions.CsvValidationException;

public class AddressBookFileIO {
	private static final String HOME = System.getProperty("user.home");
	private static String WORK_SPACE = "\\eclipse-workspace\\AddressBook";

	public AddressBookFileIO() {
	}

	/**
	 * get Path where data needs to be stored
	 */
	public Path getPathToWrite(String addressBookName) {
		Path workPath = null;
		try {
			workPath = Paths.get(HOME + WORK_SPACE + "\\OutputDirectory");
			if (Files.notExists(workPath)) {
				Files.createDirectories(workPath);
			}
			workPath = Paths.get(workPath + "\\" + addressBookName + "--contacts");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return workPath;
	}

	/**
	 * get Path from where data is read
	 */
	public Path getPathToRead(File file) {
		Path workPath = null;
		workPath = Paths.get(HOME + WORK_SPACE + "\\InputDirectory");
		if (Files.exists(workPath)) {
			workPath = Paths.get(workPath + "\\" + file);
			if (Files.notExists(workPath)) {
				workPath = null;
			}
		}
		return workPath;
	}

	/**
	 * Writes contacts to a text file by creating a new file
	 */
	public void writeTextFile(String addressBookName, LinkedList<Contact> contactList) {
		try {
			Path tempPath = getPathToWrite(addressBookName);
			tempPath = Paths.get(tempPath + ".txt");
			StringBuffer contactBuffer = new StringBuffer();
			contactList.stream().forEach(contact -> {
				String contactDataString = contact.toString().concat("\n");
				contactBuffer.append(contactDataString);
			});
			Files.write(tempPath, contactBuffer.toString().getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Reads the contacts from a text file and returns the list of contacts
	 */
	public LinkedList<Contact> readTextFile(File file) {
		LinkedList<Contact> contactList = new LinkedList<Contact>();
		try {
			Path path = getPathToRead(file);
			BufferedReader reader = Files.newBufferedReader(path);
			String currentLine = null;
			while ((currentLine = reader.readLine()) != null) {
				String[] contactDetails = currentLine.trim().split("[,\\s]{0,}[a-zA-Z]+[=]{1}");
				contactList.add(new Contact(contactDetails[1], contactDetails[2], contactDetails[3], contactDetails[4],
						contactDetails[5], contactDetails[6], Long.parseLong(contactDetails[7]),
						Long.parseLong(contactDetails[8])));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return contactList;
	}

	/**
	 * Reads the contacts from a CSV file and returns the list of contacts
	 */
	public LinkedList<Contact> readCSVFile(File file) {
		LinkedList<Contact> contactList = new LinkedList<Contact>();
		Path path = getPathToRead(file);
		try (Reader reader = Files.newBufferedReader(path);) {
			CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(1).build();
			String[] nextRecord;
			while ((nextRecord = csvReader.readNext()) != null) {
				contactList.add(new Contact(nextRecord[0], nextRecord[1], nextRecord[2], nextRecord[3], nextRecord[4],
						nextRecord[5], Long.parseLong(nextRecord[6]), Long.parseLong(nextRecord[7])));
			}
		} catch (CsvValidationException | IOException e) {
			e.printStackTrace();
		}
		return contactList;
	}

	/**
	 * Writes contacts to a CSV file by creating a new file
	 */
	public void writeCSVFile(String addressBookName, LinkedList<Contact> contactList)
			throws CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
		try (Writer writer = Files.newBufferedWriter(Paths.get(getPathToWrite(addressBookName) + ".csv"));) {
			StatefulBeanToCsv<Contact> beanToCsv = new StatefulBeanToCsvBuilder<Contact>(writer)
					.withQuotechar(CSVWriter.NO_QUOTE_CHARACTER).build();
			beanToCsv.write(contactList);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Writes contacts to a JSON file by creating the new file
	 */
	public void writeJSONFile(String addressBookName, LinkedList<Contact> contactList) throws IOException {
		try (Writer writer = Files.newBufferedWriter(Paths.get(getPathToWrite(addressBookName) + ".json"));) {
			Gson gson = new Gson();
			String json = gson.toJson(contactList);
			writer.write(json);
		}
	}

	/**
	 * Reads contacts from JSON file and returns the List of contacts
	 */
	public LinkedList<Contact> readJSONFile(File file) {
		LinkedList<Contact> contactList = new LinkedList<Contact>();
		Path path = getPathToRead(file);
		try (BufferedReader reader = new BufferedReader(new FileReader(path.toString()));) {
			Gson gson = new Gson();
			Contact[] contacts = gson.fromJson(reader, Contact[].class);
			contactList = new LinkedList(Arrays.asList(contacts));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return contactList;
	}
}
