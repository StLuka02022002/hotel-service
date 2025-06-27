package com.example.hotel_service.exception;

public class BadIdException extends IllegalArgumentException{
    public BadIdException() {
    }

    public BadIdException(String s) {
        super(s);
    }
}
