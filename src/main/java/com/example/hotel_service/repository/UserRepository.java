package com.example.hotel_service.repository;

import com.example.hotel_service.entity.User;

import java.util.Optional;

public interface UserRepository extends BaseRepository<User> {

    Optional<User> findByName(String name);

    boolean existsByName(String name);

    boolean existsByEmail(String email);
}
