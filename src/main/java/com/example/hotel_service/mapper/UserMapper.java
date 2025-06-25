package com.example.hotel_service.mapper;

import com.example.hotel_service.dto.request.UserRequest;
import com.example.hotel_service.dto.responce.UserResponse;
import com.example.hotel_service.dto.responce.UserResponseEdit;
import com.example.hotel_service.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    @Mapping(target = "name", source = "login")
    User userRequestToUser(UserRequest user);

    UserResponse userToUserResponse(User user);

    UserResponseEdit userToUserResponseEdit(User user);
}
