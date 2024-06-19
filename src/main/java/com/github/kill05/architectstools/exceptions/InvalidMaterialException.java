package com.github.kill05.architectstools.exceptions;

public class InvalidMaterialException extends RuntimeException {

	public InvalidMaterialException() {
	}

	public InvalidMaterialException(String message) {
		super(message);
	}

	public InvalidMaterialException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidMaterialException(Throwable cause) {
		super(cause);
	}

	public InvalidMaterialException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
