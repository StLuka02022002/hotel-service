package com.example.hotel_service.exception;

import jakarta.persistence.EntityNotFoundException;

public class RoomNotFoundException extends EntityNotFoundException {

    public RoomNotFoundException() {
    }

    public RoomNotFoundException(String message) {
        super(message);
    }
}
