package com.example.hotel_service.service;

import com.example.hotel_service.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.UUID;

public interface UserService extends UserDetailsService {

    Page<User> getAll(int page, int size);

    List<User> getAll();

    User get(UUID id);

    User save(User t);

    User update(UUID id, User t);

    void delete(UUID id);

    boolean exists(UUID id);

    User findByUsername(String name);

    User createNewUser(User user);

    boolean existByUsername(String name);

    boolean existByEmail(String email);
}
