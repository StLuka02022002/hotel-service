package com.example.hotel_service.service.impl;

import com.example.hotel_service.dao.exception.RoomNotFoundException;
import com.example.hotel_service.entity.Room;
import com.example.hotel_service.repository.RoomRepository;
import com.example.hotel_service.service.AbstractBaseService;
import com.example.hotel_service.service.RoomService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RoomServiceImpl extends AbstractBaseService<Room, RoomRepository> implements RoomService {

    public RoomServiceImpl(RoomRepository repository) {
        super(repository);
    }

    @Override
    protected EntityNotFoundException getException(UUID id) {
        return new RoomNotFoundException("Room with id " + id + " not found");
    }

    @Override
    public Page<Room> findAll(Specification<Room> specification, Pageable pageable) {
        return repository.findAll(specification, pageable);
    }
}
