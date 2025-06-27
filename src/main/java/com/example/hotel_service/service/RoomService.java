package com.example.hotel_service.service;

import com.example.hotel_service.dto.request.RoomRequest;
import com.example.hotel_service.dto.responce.PaginatedResponse;
import com.example.hotel_service.dto.responce.RoomResponse;
import com.example.hotel_service.entity.Room;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.UUID;

public interface RoomService {

    PaginatedResponse<RoomResponse> getAll(int page, int size);

    RoomResponse get(String id);

    List<Room> getAll();

    Room get(UUID id);

    RoomResponse save(RoomRequest roomRequest);

    Room save(Room t);

    RoomResponse update(String id, RoomRequest roomRequest);

    Room update(UUID id, Room t);

    void delete(String id);

    void delete(UUID id);

    boolean exists(UUID id);

    PaginatedResponse<RoomResponse> getAll(Specification<Room> specification, int page, int size, String sortBy);
}
