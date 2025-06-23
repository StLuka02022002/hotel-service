package com.example.hotel_service.service;

import com.example.hotel_service.entity.Booking;
import com.example.hotel_service.entity.Room;
import com.example.hotel_service.entity.User;

import java.time.LocalDate;
import java.util.List;

public interface BookingService extends BaseService<Booking> {
    Booking book(Room room, User user, LocalDate in, LocalDate out);
    List<Booking> findByRoom(Room room);
    List<Booking> findByUser(User user);
    List<Booking> findByRoomAndUser(Room room, User user);
}
