package com.addbook.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.capgemini.addressbook.Contact;

public class AddressBookDBService {
	private PreparedStatement ContactDataStatement;
	private static AddressBookDBService addressBookDBService;

	private AddressBookDBService() {
	}

	public static AddressBookDBService getInstance() {
		if (addressBookDBService == null) {
			addressBookDBService = new AddressBookDBService();
		}
		return addressBookDBService;
	}

	public Connection getConnection() throws SQLException {
		String jdbcURL = "jdbc:mysql://localhost:3306/address_book_service";
		String userName = "root";
		String password = "Sreeja6shreya$";
		Connection connection;
		System.out.println("connecting to database: " + jdbcURL);
		connection = DriverManager.getConnection(jdbcURL, userName, password);
		System.out.println("connection successful !!!! " + connection);
		return connection;
	}

	public List<Contact> readData() {
		String sql = "SELECT contacts.first_name, contacts.last_name,contacts.address_book_name,contacts.address,c.city,"
				+ "contacts.state,contacts.zip,contacts.phone_number,contacts.email,address_book_name_and_type.address_book_type "
				+ "from contacts  inner join address_book_name_and_type "
				+ "on contacts.Address_book_name=address_book_name_and_type.Address_book_name; ";
		return this.getContactDetailsUsingSqlQuery(sql);
	}

	private List<Contact> getContactDetailsUsingSqlQuery(String sql) {
		List<Contact> ContactList = null;
		try (Connection connection = addressBookDBService.getConnection();) {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet result = preparedStatement.executeQuery(sql);
			ContactList = this.getAddressBookData(result);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ContactList;
	}

	private List<Contact> getAddressBookData(ResultSet result) {
		List<Contact> contactList = new ArrayList<>();
		try {
			while (result.next()) {
				String firstName = result.getString("first_name");
				String lastName = result.getString("last_name");
				String addressBookName = result.getString("address_book_name");
				String address = result.getString("address");
				String city = result.getString("city");
				String state = result.getString("state");
				String zip = result.getString("zip");
				String phoneNumber = result.getString("phone_number");
				String email = result.getString("email");
				String addressBookType = result.getString("address_book_type");
				contactList.add(new Contact(firstName, lastName, address, city, state, zip, phoneNumber, email,
						addressBookName, addressBookType));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return contactList;
	}
	
	public int updateEmployeeData(String name, String address) {
		return this.updateContactDataUsingPreparedStatement(name, address);
	}

	private int updateContactDataUsingPreparedStatement(String first_name, String address) {
		try (Connection connection = addressBookDBService.getConnection();) {
			String sql = "update contact_details set address=? where first_name=?";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, address);
			preparedStatement.setString(2, first_name);
			int status = preparedStatement.executeUpdate();
			return status;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public List<Contact> getcontactData(String name) {
		List<Contact> contactList = null;
		if (this.ContactDataStatement == null)
			this.prepareStatementForContactData();
		try {
			ContactDataStatement.setString(1, name);
			ResultSet resultSet = ContactDataStatement.executeQuery();
			contactList = this.getAddressBookData(resultSet);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return contactList;
	}
	
	private void prepareStatementForContactData() {
		try {
			Connection connection = addressBookDBService.getConnection();
			String sql = "SELECT contacts.first_name, contacts.last_name,contacts.address_book_name,contacts.address,c.city,"
					+ "contacts.state,contacts.zip,contacts.phone_number,contacts.email,address_book_name_and_type.address_book_type "
					+ "from contacts  inner join address_book_name_and_type "
					+ "on contacts.Address_book_name=address_book_name_and_type.Address_book_name; ";
			ContactDataStatement = connection.prepareStatement(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
