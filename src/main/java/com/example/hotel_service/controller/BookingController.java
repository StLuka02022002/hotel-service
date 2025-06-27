package com.example.hotel_service.controller;

import com.example.hotel_service.aop.Log;
import com.example.hotel_service.dto.request.BookingRequest;
import com.example.hotel_service.dto.responce.BookingResponse;
import com.example.hotel_service.dto.responce.PaginatedResponse;
import com.example.hotel_service.entity.User;
import com.example.hotel_service.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/booking")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    @Log(logExecutionTime = true)
    public BookingResponse book(@RequestBody BookingRequest request, @AuthenticationPrincipal User user) {
        return bookingService.book(request, user);
    }

    @GetMapping
    public PaginatedResponse<BookingResponse> getAll(@RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "10") int size) {
        return bookingService.getAll(page, size);
    }
}
