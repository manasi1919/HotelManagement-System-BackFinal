package com.hms.execption;
 
public class DuplicateAmenityException extends RuntimeException{
	public DuplicateAmenityException(String message) {
		super(message);
	}
}