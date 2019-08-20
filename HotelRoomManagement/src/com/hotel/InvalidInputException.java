package com.hotel;

import java.io.Serializable;

public class InvalidInputException extends Exception implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String exceptionDesc;
	
	public String getExceptionDesc() {
		return exceptionDesc;
	}
	
	public InvalidInputException() { /* Default Constructor */ }
	
	public InvalidInputException(String exceptionDesc) {
		this.exceptionDesc = exceptionDesc;
	}
}
