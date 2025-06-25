package com.example.hotel_service.exception;

public class InvalidDateException extends IllegalArgumentException{

    public InvalidDateException() {
    }

    public InvalidDateException(String message) {
        super(message);
    }
}
