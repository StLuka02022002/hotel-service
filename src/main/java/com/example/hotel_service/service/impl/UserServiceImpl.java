package com.example.hotel_service.service.impl;

import com.example.hotel_service.entity.User;
import com.example.hotel_service.exception.UserNotFoundException;
import com.example.hotel_service.repository.UserRepository;
import com.example.hotel_service.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    @Override
    public Page<User> getAll(int page, int size) {
        return repository.findAll(PageRequest.of(page, size));
    }

    @Override
    public List<User> getAll() {
        return repository.findAll();
    }

    @Override
    public User get(UUID id) {
        return repository.findById(id).orElseThrow(() -> this.getException(id));
    }

    @Override
    public User save(User t) {
        return repository.save(t);
    }

    @Override
    public User update(UUID id, User t) {
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

    @Override
    public User findByUsername(String name) {
        return repository.findByName(name).orElse(null);
    }

    @Override
    public User createNewUser(User user) {
        if (this.existByUsername(user.getName()) ||
                this.existByEmail(user.getEmail())) {
            //TODO поменять ошибку
            throw new UserNotFoundException("User exists");
        }
        return repository.save(user);
    }

    @Override
    public boolean existByUsername(String name) {
        return repository.existsByName(name);
    }

    @Override
    public boolean existByEmail(String email) {
        return repository.existsByEmail(email);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByName(username).orElseThrow(() ->
                new UserNotFoundException("User with username: " + username + "not found"));
    }

    private EntityNotFoundException getException(UUID id) {
        return new UserNotFoundException("User with id " + id + " not found");
    }
}
