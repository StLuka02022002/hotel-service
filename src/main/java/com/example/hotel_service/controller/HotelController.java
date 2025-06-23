package com.example.hotel_service.controller;

import com.example.hotel_service.dao.request.HotelRequest;
import com.example.hotel_service.dao.request.RatingRequest;
import com.example.hotel_service.dao.responce.HotelResponse;
import com.example.hotel_service.dao.responce.HotelResponseEdit;
import com.example.hotel_service.dao.responce.PaginatedResponse;
import com.example.hotel_service.dao.responce.RatingResponse;
import com.example.hotel_service.entity.Hotel;
import com.example.hotel_service.mapper.HotelMapper;
import com.example.hotel_service.service.HotelService;
import com.example.hotel_service.specification.HotelSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/hotels")
@RequiredArgsConstructor
public class HotelController {

    private final HotelService hotelService;
    private final HotelMapper hotelMapper;

    @GetMapping("/{id}")
    public ResponseEntity<HotelResponse> getById(@PathVariable String id) {
        UUID uuid = UUID.fromString(id); //Возможно выбросить своё исключение
        Hotel hotel = hotelService.get(uuid);
        return ResponseEntity.ok(hotelMapper.hotelToHotelResponse(hotel));
    }

    @GetMapping
    public ResponseEntity<PaginatedResponse<HotelResponse>> getAll(@RequestParam(defaultValue = "0") int page,
                                                                   @RequestParam(defaultValue = "10") int size) {
        Page<Hotel> hotelPage = hotelService.getAll(page, size);
        PaginatedResponse<HotelResponse> response = PaginatedResponse.of(hotelPage, hotelMapper::hotelToHotelResponse);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/filter")
    public ResponseEntity<PaginatedResponse<HotelResponse>> filter(
            @RequestParam(required = false) String id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) Double distanceFromCenter,
            @RequestParam(required = false) Double minRating,
            @RequestParam(required = false) Integer minEstimationCount,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy
    ) {
        UUID uuid = id == null ? null : UUID.fromString(id);
        Specification<Hotel> spec = Specification.where(HotelSpecification.hasId(uuid))
                .and(HotelSpecification.hasName(name))
                .and(HotelSpecification.hasTitle(title))
                .and(HotelSpecification.hasAddress(address))
                .and(HotelSpecification.hasDistanceFromCenter(distanceFromCenter))
                .and(HotelSpecification.hasMinRating(minRating))
                .and(HotelSpecification.hasMinEstimationCount(minEstimationCount));
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        Page<Hotel> hotelPage = hotelService.findAll(spec, pageable);
        PaginatedResponse<HotelResponse> response = PaginatedResponse.of(hotelPage, hotelMapper::hotelToHotelResponse);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<HotelResponseEdit> create(@RequestBody HotelRequest hotelRequest) {
        Hotel hotel = hotelService.save(hotelMapper.hotelRequestToHotel(hotelRequest));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(hotelMapper.hotelToHotelResponseEdit(hotel));
    }

    @PostMapping("/mark")
    public ResponseEntity<RatingResponse> ratingRemake(@RequestBody RatingRequest request) {
        UUID uuid = UUID.fromString(request.getHotelId());
        Hotel hotel = hotelService.get(uuid);
        if (hotel.getRating() == null) {
            hotel.setRating(0d);
        }
        Double oldRating = hotel.getRating();
        hotel = hotelService.ratingRemake(hotel, request.getMark());
        hotelService.update(uuid, hotel);
        Double newRating = hotel.getRating();
        return ResponseEntity.ok(RatingResponse.builder()
                .hotelId(hotel.getId().toString())
                .oldRating(oldRating)
                .newRating(newRating)
                .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<HotelResponseEdit> update(@PathVariable UUID id,
                                                    @RequestBody HotelRequest hotelRequest) {
        Hotel hotel = hotelService.update(id,
                hotelMapper.hotelRequestToHotel(hotelRequest));

        return ResponseEntity.ok(hotelMapper.hotelToHotelResponseEdit(hotel));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        hotelService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
