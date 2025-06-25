package com.example.hotel_service.service.impl;

import com.example.hotel_service.entity.Hotel;
import com.example.hotel_service.exception.HotelNotFoundException;
import com.example.hotel_service.repository.HotelRepository;
import com.example.hotel_service.service.HotelService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    @Override
    public Page<Hotel> getAll(int page, int size) {
        return repository.findAll(PageRequest.of(page, size));
    }

    @Override
    public List<Hotel> getAll() {
        return repository.findAll();
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
    public Hotel update(UUID id, Hotel hotel) {
        //TODO может не стоит так делать
        if (!this.exists(id)) {
            throw this.getException(id);
        }
        hotel.setId(id);
        return save(hotel);
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
    public Page<Hotel> findAll(Specification<Hotel> specification, Pageable pageable) {
        return repository.findAll(specification, pageable);
    }

    private EntityNotFoundException getException(UUID id) {
        return new HotelNotFoundException("Hotel with id " + id + " not found");
    }
}
