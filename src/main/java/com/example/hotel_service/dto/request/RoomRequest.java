package com.example.hotel_service.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RoomRequest {

    private String name;
    private String description;
    private Integer number;
    private Integer maxPeopleCount;
    private Double price;
    private String hotelId;
}
