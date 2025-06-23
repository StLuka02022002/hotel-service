package com.example.hotel_service.service;

import com.example.hotel_service.entity.Hotel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface HotelService extends BaseService<Hotel> {

    Hotel ratingRemake(Hotel hotel, Integer newMaker);

    Page<Hotel> findAll(Specification<Hotel> specification, Pageable pageable);
}
