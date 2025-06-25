package com.example.hotel_service.controller;

import com.example.hotel_service.dto.request.UserRequest;
import com.example.hotel_service.dto.responce.PaginatedResponse;
import com.example.hotel_service.dto.responce.UserResponse;
import com.example.hotel_service.dto.responce.UserResponseEdit;
import com.example.hotel_service.entity.User;
import com.example.hotel_service.mapper.UserMapper;
import com.example.hotel_service.service.UserService;
import com.example.hotel_service.statistics.event.UserRegisteredEvent;
import com.example.hotel_service.statistics.kafka.EventProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final EventProducer producer;

    @GetMapping("/{id}")
    public UserResponse getById(@PathVariable String id) {
        //TODO Возможно выбросить своё исключение
        UUID uuid = UUID.fromString(id);
        User user = userService.get(uuid);
        return userMapper.userToUserResponse(user);
    }

    @GetMapping
    public PaginatedResponse<UserResponse> getAll(@RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "20") int size) {
        Page<User> userPage = userService.getAll(page, size);
        return PaginatedResponse.of(userPage, userMapper::userToUserResponse);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseEdit create(@RequestBody UserRequest userRequest) {
        User user = userService.createNewUser(userMapper.userRequestToUser(userRequest));
        UserRegisteredEvent event = UserRegisteredEvent.of(user);
        producer.sendUserRegisteredEvent(event);
        return userMapper.userToUserResponseEdit(user);
    }

    @PutMapping("/{id}")
    public UserResponseEdit update(@PathVariable UUID id,
                                   @RequestBody UserRequest userRequest) {
        User user = userService.update(id,
                userMapper.userRequestToUser(userRequest));
        return userMapper.userToUserResponseEdit(user);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        userService.delete(id);
    }

}
