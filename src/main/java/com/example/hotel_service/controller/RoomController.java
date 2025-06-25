package com.example.hotel_service.controller;

import com.example.hotel_service.dto.request.RoomRequest;
import com.example.hotel_service.dto.responce.PaginatedResponse;
import com.example.hotel_service.dto.responce.RoomResponse;
import com.example.hotel_service.entity.Hotel;
import com.example.hotel_service.entity.Room;
import com.example.hotel_service.mapper.RoomMapper;
import com.example.hotel_service.service.HotelService;
import com.example.hotel_service.service.RoomService;
import com.example.hotel_service.specification.RoomSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;
    private final RoomMapper roomMapper;
    private final HotelService hotelService;

    @GetMapping("/{id}")
    public RoomResponse getById(@PathVariable String id) {
        //TODO Возможно выбросить своё исключение
        UUID uuid = UUID.fromString(id);
        Room room = roomService.get(uuid);
        return roomMapper.roomToRoomResponse(room);
    }

    @GetMapping
    public PaginatedResponse<RoomResponse> getAll(@RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "20") int size) {
        Page<Room> roomPage = roomService.getAll(page, size);
        return PaginatedResponse.of(roomPage, roomMapper::roomToRoomResponse);
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
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        Page<Room> roomPage = roomService.findAll(spec, pageable);
        return PaginatedResponse.of(roomPage, roomMapper::roomToRoomResponse);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RoomResponse create(@RequestBody RoomRequest roomRequest) {
        Hotel hotel = hotelService.get(UUID.fromString(roomRequest.getHotelId()));
        //TODO перенести в mapper
        Room room = roomMapper.roomRequestToRoom(roomRequest);
        room.setHotel(hotel);
        room = roomService.save(room);
        return roomMapper.roomToRoomResponse(room);
    }

    @PutMapping("/{id}")
    public RoomResponse update(@PathVariable UUID id,
                               @RequestBody RoomRequest roomRequest) {
        Room room = roomService.update(id, roomMapper.roomRequestToRoom(roomRequest));
        return roomMapper.roomToRoomResponse(room);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        roomService.delete(id);
    }
}
