package com.example.hotel_service.controller;

import com.example.hotel_service.dao.exception.InvalidDateException;
import com.example.hotel_service.dao.request.BookingRequest;
import com.example.hotel_service.dao.responce.BookingResponse;
import com.example.hotel_service.dao.responce.PaginatedResponse;
import com.example.hotel_service.entity.Booking;
import com.example.hotel_service.entity.Room;
import com.example.hotel_service.entity.User;
import com.example.hotel_service.mapper.BookingMapper;
import com.example.hotel_service.service.BookingService;
import com.example.hotel_service.service.RoomService;
import com.example.hotel_service.statistics.event.RoomBookedEvent;
import com.example.hotel_service.statistics.kafka.EventProducer;
import com.example.hotel_service.util.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/booking")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;
    private final RoomService roomService;
    private final BookingMapper bookingMapper;
    private final EventProducer producer;

    @PostMapping
    public ResponseEntity<BookingResponse> book(@RequestBody BookingRequest request, @AuthenticationPrincipal User user) {
        Room room = roomService.get(UUID.fromString(request.getRoomId()));
        if (!DateUtil.isValidDate(request.getCheckInDate(), request.getCheckOutDate())) {
            throw new InvalidDateException("Check-out date must be after check-in date");
        }
        Booking booking = bookingService.book(room, user,
                request.getCheckInDate(), request.getCheckOutDate());
        RoomBookedEvent event = RoomBookedEvent.builder()
                .eventId(UUID.randomUUID())
                .id(room.getId())
                .userId(user.getId())
                .checkInDate(booking.getCheckInDate())
                .checkOutDate(booking.getCheckOutDate())
                .timestamp(LocalDateTime.now())
                .build();
        producer.sendRoomBookedEvent(event);
        return ResponseEntity.ok(bookingMapper.bookingToBookingResponse(booking));
    }

    @GetMapping
    public ResponseEntity<PaginatedResponse<BookingResponse>> getAll(@RequestParam(defaultValue = "0") int page,
                                                                     @RequestParam(defaultValue = "10") int size) {
        Page<Booking> bookingPage = bookingService.getAll(page, size);
        PaginatedResponse<BookingResponse> response = PaginatedResponse.<BookingResponse>builder()
                .items(bookingPage.get()
                        .map(bookingMapper::bookingToBookingResponse)
                        .toList())
                .currentPage(bookingPage.getNumber())
                .pageSize(bookingPage.getSize())
                .totalItems(bookingPage.getTotalElements())
                .totalPages(bookingPage.getTotalPages())
                .build();
        return ResponseEntity.ok(response);
    }
}
