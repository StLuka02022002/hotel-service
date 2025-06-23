package com.example.hotel_service.dao.responce;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RatingResponse {

    private String hotelId;
    private Double oldRating;
    private Double newRating;
}
