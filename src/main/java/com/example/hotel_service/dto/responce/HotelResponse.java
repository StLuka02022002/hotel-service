package com.example.hotel_service.dto.responce;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class HotelResponse {

    private UUID id;
    private String name;
    private String title;
    private String country;
    private String city;
    private String district;
    private String street;
    private String postalCode;
    private Integer buildingNumber;
    private Integer floor;
    private String apartment;
    private Double distanceFromCentreCity;
    private Double rating;
    private Integer countEstimation;
    private LocalDateTime createTime;
    private LocalDateTime updatedTime;
}
