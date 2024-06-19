package com.github.kill05.architectstools.exceptions;

public class ArchitectItemException extends RuntimeException {

	public ArchitectItemException() {
	}

	public ArchitectItemException(String message) {
		super(message);
	}

	public ArchitectItemException(String message, Throwable cause) {
		super(message, cause);
	}

	public ArchitectItemException(Throwable cause) {
		super(cause);
	}

	public ArchitectItemException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
