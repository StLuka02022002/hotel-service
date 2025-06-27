package com.example.hotel_service.service;

import com.example.hotel_service.dto.request.BookingRequest;
import com.example.hotel_service.dto.responce.BookingResponse;
import com.example.hotel_service.dto.responce.PaginatedResponse;
import com.example.hotel_service.entity.Booking;
import com.example.hotel_service.entity.Room;
import com.example.hotel_service.entity.User;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface BookingService {

    PaginatedResponse<BookingResponse> getAll(int page, int size);

    List<Booking> getAll();

    BookingResponse book(BookingRequest request, User user);

    Booking get(UUID id);

    Booking save(Booking t);

    Booking update(UUID id, Booking t);

    void delete(UUID id);

    boolean exists(UUID id);

    Booking book(Room room, User user, LocalDate in, LocalDate out);

    List<Booking> findByRoom(Room room);

    List<Booking> findByUser(User user);

    List<Booking> findByRoomAndUser(Room room, User user);
}
