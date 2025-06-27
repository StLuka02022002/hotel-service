package com.example.hotel_service.controller;

import com.example.hotel_service.aop.Log;
import com.example.hotel_service.dto.request.RoomRequest;
import com.example.hotel_service.dto.responce.PaginatedResponse;
import com.example.hotel_service.dto.responce.RoomResponse;
import com.example.hotel_service.entity.Room;
import com.example.hotel_service.service.RoomService;
import com.example.hotel_service.specification.RoomSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Log()
@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @GetMapping("/{id}")
    public RoomResponse getById(@PathVariable String id) {
        return roomService.get(id);
    }

    @GetMapping
    public PaginatedResponse<RoomResponse> getAll(@RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "20") int size) {
        return roomService.getAll(page, size);
    }

    @GetMapping("/filter")
    public PaginatedResponse<RoomResponse> filter(
            @RequestParam(required = false) String id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String hotelId,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) Integer maxPeopleCount,
            @RequestParam(required = false) LocalDate checkInDate,
            @RequestParam(required = false) LocalDate checkOutDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "name") String sortBy
    ) {
        Specification<Room> spec = RoomSpecification.createSpecification(id, name, hotelId,
                minPrice, maxPrice, maxPeopleCount, checkInDate, checkOutDate);
        return roomService.getAll(spec, page, size, sortBy);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RoomResponse create(@RequestBody RoomRequest roomRequest) {
        return roomService.save(roomRequest);
    }

    @PutMapping("/{id}")
    public RoomResponse update(@PathVariable String id,
                               @RequestBody RoomRequest roomRequest) {
        return roomService.update(id, roomRequest);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        roomService.delete(id);
    }
}
