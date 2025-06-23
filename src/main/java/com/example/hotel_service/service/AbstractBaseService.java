package com.example.hotel_service.service;

import com.example.hotel_service.entity.AbstractEntity;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.parser.Entity;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public abstract class AbstractBaseService<T extends AbstractEntity, R extends JpaRepository<T, UUID>> implements BaseService<T> {

    protected final R repository;

    @Override
    public Page<T> getAll(int page, int size) {
        return repository.findAll(PageRequest.of(page, size));
    }

    @Override
    public List<T> getAll() {
        return repository.findAll();
    }

    @Override
    public T get(UUID id) {
        return repository.findById(id).orElseThrow(() -> this.getException(id));
    }

    @Override
    public T save(T t) {
        return repository.save(t);
    }

    @Override
    public T update(UUID id, T t) {
        //TODO может не стоит так делать
        if (!this.exists(id)) {
            throw this.getException(id);
        }
        t.setId(id);
        return save(t);
    }

    @Override
    public void delete(UUID id) {
        //TODO может не стоит так делать
        if (exists(id)) {
            repository.deleteById(id);
        } else {
            throw this.getException(id);
        }
    }

    @Override
    public boolean exists(UUID id) {
        return repository.existsById(id);
    }

    protected abstract EntityNotFoundException getException(UUID id);


}
