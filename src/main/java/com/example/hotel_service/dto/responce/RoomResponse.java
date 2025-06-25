package com.example.hotel_service.dto.responce;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
public class RoomResponse {

    private UUID id;
    private String name;
    private String description;
    private Integer number;
    private BigDecimal price;
    private Integer maxPeopleCount;
}
