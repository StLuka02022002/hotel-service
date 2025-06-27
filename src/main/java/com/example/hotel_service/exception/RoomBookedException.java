package com.example.hotel_service.exception;

public class RoomBookedException extends IllegalArgumentException{
    public RoomBookedException() {
    }

    public RoomBookedException(String s) {
        super(s);
    }
}
