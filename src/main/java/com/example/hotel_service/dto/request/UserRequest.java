package com.example.hotel_service.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserRequest {

    private String login;
    private String password;
    private String email;
    private String role;
}
