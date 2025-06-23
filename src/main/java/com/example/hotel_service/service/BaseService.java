package com.example.hotel_service.service;

import com.example.hotel_service.entity.AbstractEntity;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface BaseService<T extends AbstractEntity> {

    Page<T> getAll(int page, int size);

    List<T> getAll();

    T get(UUID id);

    T save(T t);

    T update(UUID id, T t);

    void delete(UUID id);

    boolean exists(UUID id);
}
