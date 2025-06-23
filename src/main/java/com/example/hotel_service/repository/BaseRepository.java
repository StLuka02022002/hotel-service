package com.example.hotel_service.repository;

import com.example.hotel_service.entity.AbstractEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BaseRepository<T extends AbstractEntity> extends JpaRepository<T, UUID> {
}
