package com.example.hotel_service.dao.exception;

public class InvalidDateException extends IllegalArgumentException{

    public InvalidDateException() {
    }

    public InvalidDateException(String message) {
        super(message);
    }
}
