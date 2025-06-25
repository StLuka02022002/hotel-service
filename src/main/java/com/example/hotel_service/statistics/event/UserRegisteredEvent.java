package com.example.hotel_service.statistics.event;

import com.example.hotel_service.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@Document
@NoArgsConstructor
@AllArgsConstructor
@TypeAlias("UserRegisteredEvent")
public class UserRegisteredEvent {

    private UUID eventId;
    private UUID id;
    private String username;
    private String email;
    private LocalDateTime timestamp;

    public static UserRegisteredEvent of(User user) {
        return UserRegisteredEvent.builder()
                .eventId(UUID.randomUUID())
                .id(user.getId())
                .username(user.getName())
                .email(user.getEmail())
                .timestamp(LocalDateTime.now())
                .build();
    }
}
