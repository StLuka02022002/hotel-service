package com.example.hotel_service.controller;

import com.example.hotel_service.dto.request.BookingRequest;
import com.example.hotel_service.dto.responce.BookingResponse;
import com.example.hotel_service.dto.responce.PaginatedResponse;
import com.example.hotel_service.entity.Booking;
import com.example.hotel_service.entity.Room;
import com.example.hotel_service.entity.User;
import com.example.hotel_service.mapper.BookingMapper;
import com.example.hotel_service.service.BookingService;
import com.example.hotel_service.service.RoomService;
import com.example.hotel_service.statistics.event.RoomBookedEvent;
import com.example.hotel_service.statistics.kafka.EventProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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
    public BookingResponse book(@RequestBody BookingRequest request, @AuthenticationPrincipal User user) {
        Room room = roomService.get(UUID.fromString(request.getRoomId()));
        Booking booking = bookingService.book(room, user,
                request.getCheckInDate(), request.getCheckOutDate());
        RoomBookedEvent event = RoomBookedEvent.of(booking);
        producer.sendRoomBookedEvent(event);
        return bookingMapper.bookingToBookingResponse(booking);
    }

    @GetMapping
    public PaginatedResponse<BookingResponse> getAll(@RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "10") int size) {
        Page<Booking> bookingPage = bookingService.getAll(page, size);
        return PaginatedResponse.of(bookingPage, bookingMapper::bookingToBookingResponse);
    }
}
