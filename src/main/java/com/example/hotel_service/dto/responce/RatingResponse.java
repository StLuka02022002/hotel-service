package com.example.hotel_service.dto.responce;

import com.example.hotel_service.entity.Hotel;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RatingResponse {

    private String hotelId;
    private Double oldRating;
    private Double newRating;

    public static RatingResponse of(Double oldRating, Hotel updateHotel) {
        return RatingResponse.builder()
                .hotelId(updateHotel.getId().toString())
                .oldRating(oldRating == null ? 0d : oldRating)
                .newRating(updateHotel.getRating())
                .build();
    }
}
