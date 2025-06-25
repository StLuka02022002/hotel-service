package com.example.hotel_service.service;

import com.example.hotel_service.entity.Hotel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.UUID;

public interface HotelService {

    Page<Hotel> getAll(int page, int size);

    List<Hotel> getAll();

    Hotel get(UUID id);

    Hotel save(Hotel t);

    Hotel update(UUID id, Hotel t);

    void delete(UUID id);

    boolean exists(UUID id);

    Hotel ratingRemake(Hotel hotel, Integer newMaker);

    Page<Hotel> findAll(Specification<Hotel> specification, Pageable pageable);
}
