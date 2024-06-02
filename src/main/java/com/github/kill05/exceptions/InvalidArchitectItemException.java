package com.github.kill05.exceptions;

public class InvalidArchitectItemException extends RuntimeException {

	public InvalidArchitectItemException() {
	}

	public InvalidArchitectItemException(String message) {
		super(message);
	}

	public InvalidArchitectItemException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidArchitectItemException(Throwable cause) {
		super(cause);
	}

	public InvalidArchitectItemException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
