package com.example.hotel_service.dao.request;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class BookingRequest {

    private String roomId;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
}
