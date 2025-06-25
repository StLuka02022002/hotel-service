package com.example.hotel_service.service.impl;

import com.example.hotel_service.entity.Room;
import com.example.hotel_service.exception.RoomNotFoundException;
import com.example.hotel_service.repository.RoomRepository;
import com.example.hotel_service.service.RoomService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository repository;

    @Override
    public Page<Room> getAll(int page, int size) {
        return repository.findAll(PageRequest.of(page, size));
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
    public Room save(Room room) {
        return repository.save(room);
    }

    @Override
    public Room update(UUID id, Room room) {
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
    public Page<Room> findAll(Specification<Room> specification, Pageable pageable) {
        return repository.findAll(specification, pageable);
    }

    private EntityNotFoundException getException(UUID id) {
        return new RoomNotFoundException("Room with id " + id + " not found");
    }
}
