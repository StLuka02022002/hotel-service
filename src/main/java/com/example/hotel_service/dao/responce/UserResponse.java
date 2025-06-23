package com.example.hotel_service.dao.responce;

import com.example.hotel_service.entity.Booking;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class UserResponse {

    private UUID id;
    private String name;
    private String email;
    private List<Booking> bookings;
}
