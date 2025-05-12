package com.hms.execption;

public class PaymentException extends RuntimeException {
    public PaymentException(String message) {
        super(message);
    }
}
