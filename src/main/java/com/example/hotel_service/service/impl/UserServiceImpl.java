package com.example.hotel_service.service.impl;

import com.example.hotel_service.dao.exception.UserNotFoundException;
import com.example.hotel_service.entity.User;
import com.example.hotel_service.repository.UserRepository;
import com.example.hotel_service.service.AbstractBaseService;
import com.example.hotel_service.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserServiceImpl extends AbstractBaseService<User, UserRepository> implements UserService {

    public UserServiceImpl(UserRepository repository) {
        super(repository);
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
    protected EntityNotFoundException getException(UUID id) {
        return new UserNotFoundException("Hotel with id " + id + " not found");
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByName(username).orElseThrow(() ->
                new UserNotFoundException("User with username: " + username + "not found"));
    }
}
