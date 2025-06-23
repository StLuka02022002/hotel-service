package com.example.hotel_service.service;

import com.example.hotel_service.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends BaseService<User>, UserDetailsService {

    User findByUsername(String name);

    User createNewUser(User user);

    boolean existByUsername(String name);

    boolean existByEmail(String email);
}
