package com.example.hotel_service.dao.responce;

import com.example.hotel_service.entity.embedded.Address;
import com.example.hotel_service.entity.embedded.City;
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
    private City city;
    private Address address;
    private Double distanceFromCentreCity;
    private Double rating;
    private Integer countEstimation;
    private LocalDateTime createTime;
    private LocalDateTime updatedTime;
}
