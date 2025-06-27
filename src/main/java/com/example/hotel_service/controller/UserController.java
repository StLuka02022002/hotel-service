package com.example.hotel_service.controller;

import com.example.hotel_service.aop.Log;
import com.example.hotel_service.dto.request.UserRequest;
import com.example.hotel_service.dto.responce.PaginatedResponse;
import com.example.hotel_service.dto.responce.UserResponse;
import com.example.hotel_service.dto.responce.UserResponseEdit;
import com.example.hotel_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Log()
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public UserResponse getById(@PathVariable String id) {
        return userService.get(id);
    }

    @GetMapping
    public PaginatedResponse<UserResponse> getAll(@RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "20") int size) {
        return userService.getAll(page, size);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseEdit create(@RequestBody UserRequest userRequest) {
        return userService.save(userRequest);
    }

    @PutMapping("/{id}")
    public UserResponseEdit update(@PathVariable String id,
                                   @RequestBody UserRequest userRequest) {
        return userService.update(id, userRequest);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        userService.delete(id);
    }

}
