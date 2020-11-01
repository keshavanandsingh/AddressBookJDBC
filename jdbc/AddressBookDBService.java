package com.addbook.jdbc;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddressBookDBService {
	private static final String phone = null;
	private PreparedStatement ContactDataStatement;
	private static AddressBookDBService addressBookDBService;

	private AddressBookDBService() {
	}

	static AddressBookDBService getInstance() {
		if (addressBookDBService == null) {
			addressBookDBService = new AddressBookDBService();
		}
		return addressBookDBService;
	}

	public static Connection getConnection() throws SQLException {
		String jdbcURL = "jdbc:mysql://localhost:3306/address_book_service?useSSL=false";
		String userName = "root";
		String password = "Sreeja6shreya$";
		Connection connection;
		System.out.println("connecting to database: " + jdbcURL);
		connection = DriverManager.getConnection(jdbcURL, userName, password);
		System.out.println("connection successful !!!! " + connection);
		return connection;
	}

	public List<Contact> readData() {
		String sql = "SELECT c.first_name, c.last_name,c.address_book_name,c.address,c.city,"
				+ "c.state,c.zip,c.phone_number,c.email,abd.address_book_type "
				+ "from contact_details c inner join address_book_dict abd "
				+ "on c.address_book_name=abd.address_book_name; ";
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

	public List<Contact> getContactForDateRange(LocalDate startDate, LocalDate endDate) {
		String sql = String.format( "SELECT contacts.first_name, contacts.last_name,contacts.address_book_name,contacts.address,c.city,"
						+ "contacts.state,contacts.zip,contacts.phone_number,contacts.email,address_book_name_and_type.address_book_type "
						+ "from contacts  inner join address_book_name_and_type "
						+ "on contacts.Address_book_name=address_book_name_and_type.Address_book_name; ",
				startDate,endDate);
		return this.getContactDetailsUsingSqlQuery(sql);
	}

	public Map<String, Integer> getContactByCity() {
		String sql = "SELECT city, COUNT(firstName) as count from contact_details group by city; ";
		Map<String, Integer> contactByCityMap = new HashMap<>();
		try (Connection connection = addressBookDBService.getConnection()) {
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery(sql);
			while (result.next()) {
				String city = result.getString("city");
				Integer count = result.getInt("count");
				contactByCityMap.put(city,count);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return contactByCityMap;
	}

	public Contact addContact(String firstName, String lastName, String address, String city, String state, String zip,
			String phoneNumber, String email, String addressBookName, String addressBookType, LocalDate date) {
		String sql = String.format(
				"INSERT INTO contacts (first_name,last_name,address,city,state,zip,phone_number,email) VALUES ('%s','%s','%s','%s','%s','%s','%s','%s','%s');",
				date, firstName, lastName, address, city, state, zip, phoneNumber, email);
		Contact contact = null;
		try (Connection connection = getConnection()) {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			int result = preparedStatement.executeUpdate();
			if (result == 1)
				contact = new Contact(firstName, lastName, address, city, state, zip, phoneNumber, email);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return contact;
	}

	public static Contact insertNewContactToDB(String date, String firstName, String lastName, String address,
			String city, String state, String zip, String phoneNo, String email) {
		String sql = String.format(
				"INSERT INTO contacs (date_added,first_name,last_name,address,city,state,zip,phone_number,email) VALUES ('%s','%s','%s','%s','%s','%s','%s','%s','%s');",
				date, firstName, lastName, address, city, state, zip, phoneNo, email);
		Contact contact = null;
		try (Connection connection = getConnection()) {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			int result = preparedStatement.executeUpdate();
			if (result == 1)
				contact = new Contact(firstName, lastName, address, city, state, zip, phoneNo, email);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return contact;
	}

	
}
