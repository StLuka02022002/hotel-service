package com.example.hotel_service.service.impl;

import com.example.hotel_service.dao.exception.BookingNotFoundException;
import com.example.hotel_service.entity.Booking;
import com.example.hotel_service.entity.Room;
import com.example.hotel_service.entity.User;
import com.example.hotel_service.repository.BookingRepository;
import com.example.hotel_service.service.AbstractBaseService;
import com.example.hotel_service.service.BookingService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class BookingServiceImpl extends AbstractBaseService<Booking, BookingRepository> implements BookingService {
    public BookingServiceImpl(BookingRepository repository) {
        super(repository);
    }

    @Override
    public Booking book(Room room, User user, LocalDate in, LocalDate out) {
        if (repository.isRoomBooked(room, in, out)) {
            //TODO сделать другую ошибку
            throw new IllegalArgumentException("Room is booked");
        }

        Booking booking = Booking.builder()
                .checkInDate(in)
                .checkOutDate(out)
                .room(room)
                .user(user)
                .build();
        return repository.save(booking);
    }

    @Override
    public List<Booking> findByRoom(Room room) {
        return repository.findByRoom(room);
    }

    @Override
    public List<Booking> findByUser(User user) {
        return repository.findByUser(user);
    }

    @Override
    public List<Booking> findByRoomAndUser(Room room, User user) {
        return repository.findByRoomAndUser(room, user);
    }


    @Override
    protected EntityNotFoundException getException(UUID id) {
        return new BookingNotFoundException("Booking with id " + id + " not found");
    }
}
