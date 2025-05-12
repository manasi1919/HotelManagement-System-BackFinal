package com.hms.execption;

public class AmenityNotFoundException extends RuntimeException {
    public AmenityNotFoundException(String message) {
        super(message);
    }
}
