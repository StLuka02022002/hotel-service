package com.example.hotel_service.service.impl;

import com.example.hotel_service.dto.request.UserRequest;
import com.example.hotel_service.dto.responce.PaginatedResponse;
import com.example.hotel_service.dto.responce.UserResponse;
import com.example.hotel_service.dto.responce.UserResponseEdit;
import com.example.hotel_service.entity.User;
import com.example.hotel_service.exception.UserAlreadyExistsException;
import com.example.hotel_service.exception.UserNotFoundException;
import com.example.hotel_service.mapper.UserMapper;
import com.example.hotel_service.repository.UserRepository;
import com.example.hotel_service.service.UserService;
import com.example.hotel_service.statistics.event.UserRegisteredEvent;
import com.example.hotel_service.statistics.kafka.EventProducer;
import com.example.hotel_service.util.UUIDUtil;
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
    private final EventProducer producer;
    private final UserMapper userMapper;

    @Override
    public UserResponse get(String id) {
        UUID uuid = UUIDUtil.fromString(id);
        User user = this.get(uuid);
        return userMapper.userToUserResponse(user);
    }

    @Override
    public PaginatedResponse<UserResponse> getAll(int page, int size) {
        Page<User> userPage = repository.findAll(PageRequest.of(page, size));
        return PaginatedResponse.of(userPage, userMapper::userToUserResponse);
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
    public UserResponseEdit save(UserRequest userRequest) {
        User user = this.createNewUser(userMapper.userRequestToUser(userRequest));
        UserRegisteredEvent event = UserRegisteredEvent.of(user);
        producer.sendUserRegisteredEvent(event);
        return userMapper.userToUserResponseEdit(user);
    }

    @Override
    public User save(User t) {
        return repository.save(t);
    }

    @Override
    public UserResponseEdit update(String id, UserRequest userRequest) {
        UUID uuid = UUIDUtil.fromString(id);
        User user = this.update(uuid,
                userMapper.userRequestToUser(userRequest));
        return userMapper.userToUserResponseEdit(user);
    }

    @Override
    public User update(UUID id, User t) {
        if (!this.exists(id)) {
            throw this.getException(id);
        }
        t.setId(id);
        return save(t);
    }

    @Override
    public void delete(String id) {
        UUID uuid = UUIDUtil.fromString(id);
        this.delete(uuid);
    }

    @Override
    public void delete(UUID id) {
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
            throw new UserAlreadyExistsException("User already exists");
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
