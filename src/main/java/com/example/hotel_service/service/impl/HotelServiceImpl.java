package com.example.hotel_service.service.impl;

import com.example.hotel_service.dto.request.HotelRequest;
import com.example.hotel_service.dto.request.RatingRequest;
import com.example.hotel_service.dto.responce.HotelResponse;
import com.example.hotel_service.dto.responce.HotelResponseEdit;
import com.example.hotel_service.dto.responce.PaginatedResponse;
import com.example.hotel_service.dto.responce.RatingResponse;
import com.example.hotel_service.entity.Hotel;
import com.example.hotel_service.exception.HotelNotFoundException;
import com.example.hotel_service.mapper.HotelMapper;
import com.example.hotel_service.repository.HotelRepository;
import com.example.hotel_service.service.HotelService;
import com.example.hotel_service.util.UUIDUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService {

    private final HotelRepository repository;
    private final HotelMapper hotelMapper;

    @Override
    public PaginatedResponse<HotelResponse> getAll(int page, int size) {
        Page<Hotel> hotelPage = repository.findAll(PageRequest.of(page, size));
        return PaginatedResponse.of(hotelPage, hotelMapper::hotelToHotelResponse);
    }

    @Override
    public List<Hotel> getAll() {
        return repository.findAll();
    }

    @Override
    public HotelResponse get(String id) {
        UUID uuid = UUIDUtil.fromString(id);
        Hotel hotel = this.get(uuid);
        return hotelMapper.hotelToHotelResponse(hotel);
    }

    @Override
    public Hotel get(UUID id) {
        return repository.findById(id).orElseThrow(() -> this.getException(id));
    }

    @Override
    public Hotel save(Hotel hotel) {
        return repository.save(hotel);
    }

    @Override
    public HotelResponseEdit save(HotelRequest hotelRequest) {
        Hotel hotel = this.save(hotelMapper.hotelRequestToHotel(hotelRequest));
        return hotelMapper.hotelToHotelResponseEdit(hotel);
    }

    @Override
    public Hotel update(UUID id, Hotel hotel) {
        if (!this.exists(id)) {
            throw this.getException(id);
        }
        hotel.setId(id);
        return this.save(hotel);
    }

    @Override
    public HotelResponseEdit update(String id, HotelRequest hotelRequest) {
        UUID uuid = UUIDUtil.fromString(id);
        Hotel hotel = hotelMapper.hotelRequestToHotel(hotelRequest);
        hotel = this.update(uuid, hotel);
        return hotelMapper.hotelToHotelResponseEdit(hotel);
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
    public RatingResponse ratingRemake(RatingRequest request) {
        UUID uuid = UUIDUtil.fromString(request.getHotelId());
        Hotel hotel = this.get(uuid);
        Double oldRating = hotel.getRating();
        Hotel updateHotel = this.ratingRemake(hotel, request.getMark());
        return RatingResponse.of(oldRating, updateHotel);
    }

    @Override
    public Hotel ratingRemake(Hotel hotel, Integer newMaker) {
        if (hotel == null) {
            throw new IllegalArgumentException("Invalid hotel value");
        }

        if (newMaker == null || newMaker < 1 || newMaker > 5) {
            throw new IllegalArgumentException("Invalid rating value");
        }

        double rating = hotel.getRating() == null ? 0d : hotel.getRating();
        Integer numberOfRating = hotel.getCountEstimation();

        if (numberOfRating == null || numberOfRating == 0) {
            hotel.setRating(newMaker.doubleValue());
            hotel.setCountEstimation(1);
            return this.update(hotel.getId(), hotel);
        }

        Double totalRating = rating * numberOfRating - rating + newMaker;
        rating = BigDecimal.valueOf(totalRating / numberOfRating)
                .setScale(2, RoundingMode.HALF_EVEN)
                .doubleValue();
        hotel.setRating(rating);
        hotel.setCountEstimation(++numberOfRating);
        return this.update(hotel.getId(), hotel);
    }

    @Override
    public PaginatedResponse<HotelResponse> getAll(Specification<Hotel> specification, int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        Page<Hotel> hotelPage = repository.findAll(specification, pageable);
        return PaginatedResponse.of(hotelPage, hotelMapper::hotelToHotelResponse);
    }

    private EntityNotFoundException getException(UUID id) {
        return new HotelNotFoundException("Hotel with id " + id + " not found");
    }
}
