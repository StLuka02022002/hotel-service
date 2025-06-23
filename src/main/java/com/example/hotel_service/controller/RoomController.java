package com.example.hotel_service.controller;

import com.example.hotel_service.dao.request.RoomRequest;
import com.example.hotel_service.dao.responce.PaginatedResponse;
import com.example.hotel_service.dao.responce.RoomResponse;
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
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<RoomResponse> getById(@PathVariable String id) {
        UUID uuid = UUID.fromString(id);  //Возможно выбросить своё исключение
        Room room = roomService.get(uuid);
        return ResponseEntity.ok(roomMapper.roomToRoomResponse(room));
    }

    @GetMapping
    public ResponseEntity<PaginatedResponse<RoomResponse>> getAll(@RequestParam(defaultValue = "0") int page,
                                                                  @RequestParam(defaultValue = "20") int size) {
        Page<Room> roomPage = roomService.getAll(page, size);
        PaginatedResponse<RoomResponse> response = PaginatedResponse.of(roomPage, roomMapper::roomToRoomResponse);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/filter")
    public ResponseEntity<PaginatedResponse<RoomResponse>> filter(
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
        UUID uuid = id == null ? null : UUID.fromString(id);
        UUID uuidHotel = hotelId == null ? null : UUID.fromString(hotelId);
        Specification<Room> spec = Specification.where(RoomSpecification.hasId(uuid))
                .and(RoomSpecification.hasName(name))
                .and(RoomSpecification.hasHotelId(uuidHotel))
                .and(RoomSpecification.hasPrice(minPrice, maxPrice))
                .and(RoomSpecification.hasMaxPeopleCount(maxPeopleCount))
                .and(RoomSpecification.isAvailableBetween(checkInDate, checkOutDate));

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        Page<Room> roomPage = roomService.findAll(spec, pageable);
        PaginatedResponse<RoomResponse> response = PaginatedResponse.of(roomPage, roomMapper::roomToRoomResponse);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<RoomResponse> create(@RequestBody RoomRequest roomRequest) {
        Hotel hotel = hotelService.get(UUID.fromString(roomRequest.getHotelId()));
        //TODO перенести в mapper
        Room room = roomMapper.roomRequestToRoom(roomRequest);
        room.setHotel(hotel);
        room = roomService.save(room);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(roomMapper.roomToRoomResponse(room));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoomResponse> update(@PathVariable UUID id,
                                               @RequestBody RoomRequest roomRequest) {
        Room room = roomService.update(id,
                roomMapper.roomRequestToRoom(roomRequest));

        return ResponseEntity.ok(roomMapper.roomToRoomResponse(room));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        roomService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
