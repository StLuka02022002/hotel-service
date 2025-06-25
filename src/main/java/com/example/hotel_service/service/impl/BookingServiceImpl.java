package com.example.hotel_service.service.impl;

import com.example.hotel_service.entity.Booking;
import com.example.hotel_service.entity.Room;
import com.example.hotel_service.entity.User;
import com.example.hotel_service.exception.BookingNotFoundException;
import com.example.hotel_service.exception.InvalidDateException;
import com.example.hotel_service.repository.BookingRepository;
import com.example.hotel_service.service.BookingService;
import com.example.hotel_service.util.DateUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository repository;

    @Override
    public Page<Booking> getAll(int page, int size) {
        return repository.findAll(PageRequest.of(page, size));
    }

    @Override
    public List<Booking> getAll() {
        return repository.findAll();
    }

    @Override
    public Booking get(UUID id) {
        return repository.findById(id).orElseThrow(() -> this.getException(id));
    }

    @Override
    public Booking save(Booking room) {
        return repository.save(room);
    }

    @Override
    public Booking update(UUID id, Booking room) {
        //TODO может не стоит так делать
        if (!this.exists(id)) {
            throw this.getException(id);
        }
        room.setId(id);
        return save(room);
    }

    @Override
    public void delete(UUID id) {
        //TODO может не стоит так делать
        if (exists(id)) {
            repository.deleteById(id);
        } else {
            throw this.getException(id);
        }
    }

    @Override
    public boolean exists(UUID id) {
        return repository.existsById(id);
    }

    @Override
    public Booking book(Room room, User user, LocalDate in, LocalDate out) {
        if (!DateUtil.isValidDate(in, out)) {
            throw new InvalidDateException("Check-out date must be after check-in date");
        }

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

    private EntityNotFoundException getException(UUID id) {
        return new BookingNotFoundException("Booking with id " + id + " not found");
    }
}
