package com.capgemini.exceptions;

public class DatabaseException extends Exception {
	
	ExceptionType type;
	
	public enum ExceptionType {
		UNABLE_TO_CONNECT, UNABLE_TO_EXECUTE_QUERY, UNABLE_TO_GET_PREPARED_STATEMENT, UNABLE_TO_RETRIEVE_DATA,
		AUTO_COMMIT_ERROR, UNABLE_TO_ROLL_BACK, UNABLE_TO_CLOSE_CONNECTION, UNABLE_TO_COMMIT;
	}
	
	public DatabaseException(String message, ExceptionType type) {
		super(message);
		this.type = type;
	}
}
