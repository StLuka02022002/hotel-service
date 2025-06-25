package com.example.hotel_service.statistics.event;

import com.example.hotel_service.entity.Booking;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@Document
@NoArgsConstructor
@AllArgsConstructor
@TypeAlias("RoomBookedEvent")
public class RoomBookedEvent {

    private UUID eventId;
    private UUID userId;
    private UUID id;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private LocalDateTime timestamp;

    public static RoomBookedEvent of(Booking booking) {
        return RoomBookedEvent.builder()
                .eventId(UUID.randomUUID())
                .id(booking.getRoom().getId())
                .userId(booking.getUser().getId())
                .checkInDate(booking.getCheckInDate())
                .checkOutDate(booking.getCheckOutDate())
                .timestamp(LocalDateTime.now())
                .build();
    }
}
