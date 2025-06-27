package com.example.hotel_service.service.impl;

import com.example.hotel_service.dto.request.RoomRequest;
import com.example.hotel_service.dto.responce.PaginatedResponse;
import com.example.hotel_service.dto.responce.RoomResponse;
import com.example.hotel_service.entity.Hotel;
import com.example.hotel_service.entity.Room;
import com.example.hotel_service.exception.RoomNotFoundException;
import com.example.hotel_service.mapper.RoomMapper;
import com.example.hotel_service.repository.RoomRepository;
import com.example.hotel_service.service.HotelService;
import com.example.hotel_service.service.RoomService;
import com.example.hotel_service.util.UUIDUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final HotelService hotelService;
    private final RoomRepository repository;
    private final RoomMapper roomMapper;

    @Override
    public PaginatedResponse<RoomResponse> getAll(int page, int size) {
        Page<Room> roomPage = repository.findAll(PageRequest.of(page, size));
        return PaginatedResponse.of(roomPage, roomMapper::roomToRoomResponse);
    }

    @Override
    public RoomResponse get(String id) {
        UUID uuid = UUIDUtil.fromString(id);
        Room room = this.get(uuid);
        return roomMapper.roomToRoomResponse(room);
    }

    @Override
    public List<Room> getAll() {
        return repository.findAll();
    }

    @Override
    public Room get(UUID id) {
        return repository.findById(id).orElseThrow(() -> this.getException(id));
    }

    @Override
    public RoomResponse save(RoomRequest roomRequest) {
        Hotel hotel = hotelService.get(UUIDUtil.fromString(roomRequest.getHotelId()));
        Room room = roomMapper.roomRequestToRoom(roomRequest);
        room.setHotel(hotel);
        room = this.save(room);
        return roomMapper.roomToRoomResponse(room);
    }

    @Override
    public Room save(Room room) {
        return repository.save(room);
    }

    @Override
    public RoomResponse update(String id, RoomRequest roomRequest) {
        UUID uuid = UUIDUtil.fromString(id);
        Room room = this.update(uuid, roomMapper.roomRequestToRoom(roomRequest));
        return roomMapper.roomToRoomResponse(room);
    }

    @Override
    public Room update(UUID id, Room room) {
        if (!this.exists(id)) {
            throw this.getException(id);
        }
        room.setId(id);
        return save(room);
    }

    @Override
    public void delete(String id) {
        UUID uuid = UUIDUtil.fromString(id);
        this.delete(uuid);
    }

    @Override
    public void delete(UUID id) {
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
    public PaginatedResponse<RoomResponse> getAll(Specification<Room> specification, int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        Page<Room> roomPage = repository.findAll(specification, pageable);
        return PaginatedResponse.of(roomPage, roomMapper::roomToRoomResponse);
    }

    private EntityNotFoundException getException(UUID id) {
        return new RoomNotFoundException("Room with id " + id + " not found");
    }
}
