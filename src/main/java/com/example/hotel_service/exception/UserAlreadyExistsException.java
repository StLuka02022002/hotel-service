package com.example.hotel_service.exception;

public class UserAlreadyExistsException extends IllegalArgumentException {

    public UserAlreadyExistsException() {
    }

    public UserAlreadyExistsException(String s) {
        super(s);
    }
}
