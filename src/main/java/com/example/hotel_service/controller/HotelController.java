package com.example.hotel_service.controller;

import com.example.hotel_service.dto.request.HotelRequest;
import com.example.hotel_service.dto.request.RatingRequest;
import com.example.hotel_service.dto.responce.HotelResponse;
import com.example.hotel_service.dto.responce.HotelResponseEdit;
import com.example.hotel_service.dto.responce.PaginatedResponse;
import com.example.hotel_service.dto.responce.RatingResponse;
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
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/hotels")
@RequiredArgsConstructor
public class HotelController {

    private final HotelService hotelService;
    private final HotelMapper hotelMapper;

    @GetMapping("/{id}")
    public HotelResponse getById(@PathVariable String id) {
        //TODO Возможно выбросить своё исключение
        UUID uuid = UUID.fromString(id);
        Hotel hotel = hotelService.get(uuid);
        return hotelMapper.hotelToHotelResponse(hotel);
    }

    @GetMapping
    public PaginatedResponse<HotelResponse> getAll(@RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "10") int size) {
        Page<Hotel> hotelPage = hotelService.getAll(page, size);
        return PaginatedResponse.of(hotelPage, hotelMapper::hotelToHotelResponse);
    }

    @GetMapping("/filter")
    public PaginatedResponse<HotelResponse> filter(
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
        Specification<Hotel> spec = HotelSpecification.createSpecification(id, name, title, address,
                distanceFromCenter, minRating, minEstimationCount);
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        Page<Hotel> hotelPage = hotelService.findAll(spec, pageable);
        return PaginatedResponse.of(hotelPage, hotelMapper::hotelToHotelResponse);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public HotelResponseEdit create(@RequestBody HotelRequest hotelRequest) {
        Hotel hotel = hotelService.save(hotelMapper.hotelRequestToHotel(hotelRequest));
        return hotelMapper.hotelToHotelResponseEdit(hotel);
    }

    @PostMapping("/mark")
    public RatingResponse ratingRemake(@RequestBody RatingRequest request) {
        UUID uuid = UUID.fromString(request.getHotelId());
        Hotel hotel = hotelService.get(uuid);
        Double oldRating = hotel.getRating();
        Hotel updateHotel = hotelService.ratingRemake(hotel, request.getMark());
        return RatingResponse.of(oldRating, updateHotel);
    }

    @PutMapping("/{id}")
    public HotelResponseEdit update(@PathVariable UUID id,
                                    @RequestBody HotelRequest hotelRequest) {
        Hotel hotel = hotelService.update(id,
                hotelMapper.hotelRequestToHotel(hotelRequest));
        return hotelMapper.hotelToHotelResponseEdit(hotel);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        hotelService.delete(id);
    }
}
