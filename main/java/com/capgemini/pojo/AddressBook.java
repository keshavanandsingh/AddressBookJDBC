package com.capgemini.pojo;

import java.util.LinkedList;

public class AddressBook {
	private int id;
	public String name;
	public TYPE type;
	private LinkedList<Contact> contacts;

	public enum TYPE {
		FRIEND, FAMILY, PROFESSION;
	}

	public AddressBook(int id, String name, TYPE type) {
		this.id = id;
		this.name = name;
		this.type = type;
		this.contacts = new LinkedList<Contact>();
	}
	
	public AddressBook(int id, String name, TYPE type, LinkedList<Contact> contacts) {
		this(id, name, type);
		this.contacts = contacts;
	}
	
	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public TYPE getType() {
		return type;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AddressBook other = (AddressBook) obj;
		if (id != other.id)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (type != other.type)
			return false;
		return true;
	}

	public LinkedList<Contact> getContacts() {
		return contacts;
	}
}
