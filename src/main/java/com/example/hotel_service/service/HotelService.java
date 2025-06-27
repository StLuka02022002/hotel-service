package com.example.hotel_service.service;

import com.example.hotel_service.dto.request.HotelRequest;
import com.example.hotel_service.dto.request.RatingRequest;
import com.example.hotel_service.dto.responce.HotelResponse;
import com.example.hotel_service.dto.responce.HotelResponseEdit;
import com.example.hotel_service.dto.responce.PaginatedResponse;
import com.example.hotel_service.dto.responce.RatingResponse;
import com.example.hotel_service.entity.Hotel;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.UUID;

public interface HotelService {

    PaginatedResponse<HotelResponse> getAll(int page, int size);

    List<Hotel> getAll();

    HotelResponse get(String id);

    Hotel get(UUID id);

    Hotel save(Hotel hotel);

    HotelResponseEdit save(HotelRequest hotelRequest);

    Hotel update(UUID id, Hotel hotel);

    HotelResponseEdit update(String id, HotelRequest hotelRequest);

    void delete(String id);

    void delete(UUID id);

    boolean exists(UUID id);

    RatingResponse ratingRemake(RatingRequest request);

    Hotel ratingRemake(Hotel hotel, Integer newMaker);

    PaginatedResponse<HotelResponse> getAll(Specification<Hotel> specification, int page, int size, String sortBy);
}
