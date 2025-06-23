package com.example.hotel_service.dao.exception;

import jakarta.persistence.EntityNotFoundException;

public class BookingNotFoundException extends EntityNotFoundException {

    public BookingNotFoundException() {
    }

    public BookingNotFoundException(String message) {
        super(message);
    }
}
