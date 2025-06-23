package com.example.hotel_service.service;

import com.example.hotel_service.entity.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface RoomService extends BaseService<Room> {

    Page<Room> findAll(Specification<Room> specification, Pageable pageable);
}
