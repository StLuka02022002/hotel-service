package com.example.hotel_service.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HotelRequest {

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
}
