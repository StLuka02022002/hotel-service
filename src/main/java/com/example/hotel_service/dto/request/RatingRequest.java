package com.example.hotel_service.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RatingRequest {

    private String hotelId;
    private Integer mark;
}
