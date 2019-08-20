package com.hotel;

import java.io.Serializable;

public class InvalidPathException extends Exception implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String exceptionDesc;
	
	public String getExceptionDesc() {
		return exceptionDesc;
	}

	public InvalidPathException() { /* Default Constructor */ }
	
	public InvalidPathException(String exceptionDesc) {
		this.exceptionDesc = exceptionDesc;
	}
}
