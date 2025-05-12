package com.hms.execption;

public class ReviewException extends RuntimeException {
    private String code;

    public ReviewException(String code, String message) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
