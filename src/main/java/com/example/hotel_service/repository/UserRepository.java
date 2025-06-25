package com.example.hotel_service.repository;

import com.example.hotel_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByName(String name);

    boolean existsByName(String name);

    boolean existsByEmail(String email);
}
