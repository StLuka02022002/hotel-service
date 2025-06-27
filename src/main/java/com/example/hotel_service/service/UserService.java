package com.example.hotel_service.service;

import com.example.hotel_service.dto.request.UserRequest;
import com.example.hotel_service.dto.responce.PaginatedResponse;
import com.example.hotel_service.dto.responce.UserResponse;
import com.example.hotel_service.dto.responce.UserResponseEdit;
import com.example.hotel_service.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.UUID;

public interface UserService extends UserDetailsService {

    UserResponse get(String id);

    PaginatedResponse<UserResponse> getAll(int page, int size);

    List<User> getAll();

    User get(UUID id);

    UserResponseEdit save(UserRequest userRequest);

    User save(User t);

    UserResponseEdit update(String id, UserRequest userRequest);

    User update(UUID id, User t);

    void delete(String id);

    void delete(UUID id);

    boolean exists(UUID id);

    User findByUsername(String name);

    User createNewUser(User user);

    boolean existByUsername(String name);

    boolean existByEmail(String email);
}
