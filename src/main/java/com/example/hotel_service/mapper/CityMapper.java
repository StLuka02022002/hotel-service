package com.example.hotel_service.mapper;

import com.example.hotel_service.dao.request.HotelRequest;
import com.example.hotel_service.entity.embedded.City;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CityMapper {

    @Mapping(target = "name", source = "city")
    @Mapping(target = "country", source = "country")
    City toCity(HotelRequest request);
}