package com.example.hotel_service.service.impl;

import com.example.hotel_service.dao.exception.HotelNotFoundException;
import com.example.hotel_service.entity.Hotel;
import com.example.hotel_service.repository.HotelRepository;
import com.example.hotel_service.service.AbstractBaseService;
import com.example.hotel_service.service.HotelService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

@Service
public class HotelServiceImpl extends AbstractBaseService<Hotel, HotelRepository> implements HotelService {

    public HotelServiceImpl(HotelRepository repository) {
        super(repository);
    }


    @Override
    protected EntityNotFoundException getException(UUID id) {
        return new HotelNotFoundException("Hotel with id " + id + " not found");
    }

    @Override
    public Hotel ratingRemake(Hotel hotel, Integer newMaker) {
        if (hotel == null || newMaker == null || newMaker < 1 || newMaker > 5) {
            throw new IllegalArgumentException("Invalid hotel or rating value");
        }

        Double rating = hotel.getRating();
        Integer numberOfRating = hotel.getCountEstimation();

        if (numberOfRating == null || numberOfRating == 0) {
            hotel.setRating(newMaker.doubleValue());
            hotel.setCountEstimation(1);
            return hotel;
        }

        Double totalRating = rating * numberOfRating - rating + newMaker;
        rating = BigDecimal.valueOf(totalRating / numberOfRating)
                .setScale(2, RoundingMode.HALF_EVEN)
                .doubleValue();
        hotel.setRating(rating);
        hotel.setCountEstimation(++numberOfRating);
        return hotel;
    }

    @Override
    public Page<Hotel> findAll(Specification<Hotel> specification, Pageable pageable) {
        return repository.findAll(specification, pageable);
    }
}
