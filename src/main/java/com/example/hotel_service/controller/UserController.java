package com.example.hotel_service.controller;

import com.example.hotel_service.dao.request.UserRequest;
import com.example.hotel_service.dao.responce.PaginatedResponse;
import com.example.hotel_service.dao.responce.UserResponse;
import com.example.hotel_service.dao.responce.UserResponseEdit;
import com.example.hotel_service.entity.User;
import com.example.hotel_service.mapper.UserMapper;
import com.example.hotel_service.service.UserService;
import com.example.hotel_service.statistics.event.RoomBookedEvent;
import com.example.hotel_service.statistics.event.UserRegisteredEvent;
import com.example.hotel_service.statistics.kafka.EventProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {


    private final UserService userService;
    private final UserMapper userMapper;
    private final EventProducer producer;

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getById(@PathVariable String id) {
        UUID uuid = UUID.fromString(id);
        User user = userService.get(uuid);
        return ResponseEntity.ok(userMapper.userToUserResponse(user));
    }

    @GetMapping
    public ResponseEntity<PaginatedResponse<UserResponse>> getAll(@RequestParam(defaultValue = "0") int page,
                                                                  @RequestParam(defaultValue = "20") int size) {
        Page<User> userPage = userService.getAll(page, size);
        PaginatedResponse<UserResponse> response = PaginatedResponse.<UserResponse>builder()
                .items(userPage.get()
                        .map(userMapper::userToUserResponse)
                        .toList())
                .currentPage(userPage.getNumber())
                .pageSize(userPage.getSize())
                .totalItems(userPage.getTotalElements())
                .totalPages(userPage.getTotalPages())
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponseEdit> create(@RequestBody UserRequest userRequest) {
        User user = userService.createNewUser(userMapper.userRequestToUser(userRequest));
        UserRegisteredEvent event = UserRegisteredEvent.builder()
                .eventId(UUID.randomUUID())
                .id(user.getId())
                .username(user.getName())
                .email(user.getEmail())
                .timestamp(LocalDateTime.now())
                .build();
        producer.sendUserRegisteredEvent(event);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userMapper.userToUserResponseEdit(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseEdit> update(@PathVariable UUID id,
                                                   @RequestBody UserRequest userRequest) {
        User user = userService.update(id,
                userMapper.userRequestToUser(userRequest));
        return ResponseEntity.ok(userMapper.userToUserResponseEdit(user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
