package com.example.hotel_service.repository;

import com.example.hotel_service.entity.Booking;
import com.example.hotel_service.entity.Room;
import com.example.hotel_service.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface BookingRepository extends BaseRepository<Booking> {

    @Query("""
            SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END
            FROM Booking b
            WHERE b.room = :room
            AND b.checkInDate <= :endBooking
            AND b.checkOutDate >= :startBooking""")
    boolean isRoomBooked(
            @Param("room") Room room,
            @Param("startBooking") LocalDate start,
            @Param("endBooking") LocalDate end
    );

    List<Booking> findByRoom(Room room);
    List<Booking> findByUser(User user);
    List<Booking> findByRoomAndUser(Room room, User user);
}
