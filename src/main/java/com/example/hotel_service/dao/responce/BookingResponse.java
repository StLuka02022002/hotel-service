package com.example.hotel_service.dao.responce;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
public class BookingResponse {

    private UUID id;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
}
