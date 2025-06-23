package com.example.hotel_service.mapper;

import com.example.hotel_service.dao.request.HotelRequest;
import com.example.hotel_service.entity.embedded.Address;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AddressMapper {

    @Mapping(target = "district", source = "district")
    @Mapping(target = "street", source = "street")
    @Mapping(target = "postalCode", source = "postalCode")
    @Mapping(target = "buildingNumber", source = "buildingNumber")
    @Mapping(target = "floor", source = "floor")
    @Mapping(target = "apartment", source = "apartment")
    Address toAddress(HotelRequest request);
}