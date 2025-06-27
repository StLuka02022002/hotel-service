package com.example.hotel_service.util;

import com.example.hotel_service.exception.BadIdException;

import java.util.UUID;

public class UUIDUtil {

    private UUIDUtil() {
    }

    public static UUID fromString(String uuid) {
        try {
            return UUID.fromString(uuid);
        } catch (IllegalArgumentException e) {
            throw new BadIdException("Id must be UUID format. Error: " + e.getMessage());
        }
    }
}
