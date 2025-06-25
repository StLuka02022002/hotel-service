package com.example.hotel_service.service;

import com.example.hotel_service.entity.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.UUID;

public interface RoomService {

    Page<Room> getAll(int page, int size);

    List<Room> getAll();

    Room get(UUID id);

    Room save(Room t);

    Room update(UUID id, Room t);

    void delete(UUID id);

    boolean exists(UUID id);

    Page<Room> findAll(Specification<Room> specification, Pageable pageable);
}
