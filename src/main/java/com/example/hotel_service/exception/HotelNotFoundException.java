package com.example.hotel_service.exception;

import jakarta.persistence.EntityNotFoundException;

public class HotelNotFoundException extends EntityNotFoundException {

    public HotelNotFoundException() {
    }

    public HotelNotFoundException(String message) {
        super(message);
    }
}
