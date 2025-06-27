package com.example.hotel_service.controller;

import com.example.hotel_service.aop.Log;
import com.example.hotel_service.dto.request.HotelRequest;
import com.example.hotel_service.dto.request.RatingRequest;
import com.example.hotel_service.dto.responce.HotelResponse;
import com.example.hotel_service.dto.responce.HotelResponseEdit;
import com.example.hotel_service.dto.responce.PaginatedResponse;
import com.example.hotel_service.dto.responce.RatingResponse;
import com.example.hotel_service.entity.Hotel;
import com.example.hotel_service.service.HotelService;
import com.example.hotel_service.specification.HotelSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Log(message = "hotel controller")
@RestController
@RequestMapping("/api/hotels")
@RequiredArgsConstructor
public class HotelController {

    private final HotelService hotelService;


    @GetMapping("/{id}")
    public HotelResponse getById(@PathVariable String id) {
        return hotelService.get(id);
    }

    @GetMapping
    public PaginatedResponse<HotelResponse> getAll(@RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "10") int size) {
        return hotelService.getAll(page, size);
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
        return hotelService.getAll(spec, page, size, sortBy);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public HotelResponseEdit create(@RequestBody HotelRequest hotelRequest) {
        return hotelService.save(hotelRequest);
    }

    @PostMapping("/mark")
    public RatingResponse ratingRemake(@RequestBody RatingRequest request) {
        return hotelService.ratingRemake(request);
    }

    @PutMapping("/{id}")
    public HotelResponseEdit update(@PathVariable String id,
                                    @RequestBody HotelRequest hotelRequest) {

        return hotelService.update(id, hotelRequest);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        hotelService.delete(id);
    }
}
