package com.example.hotel_service.mapper;

import com.example.hotel_service.dao.request.HotelRequest;
import com.example.hotel_service.dao.responce.HotelResponse;
import com.example.hotel_service.dao.responce.HotelResponseEdit;
import com.example.hotel_service.entity.Hotel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {CityMapper.class, AddressMapper.class})
public interface HotelMapper {

    @Mapping(target = "city", source = ".")
    @Mapping(target = "address", source = ".")
    Hotel hotelRequestToHotel(HotelRequest hotelRequest);

    HotelResponse hotelToHotelResponse(Hotel hotel);

    HotelResponseEdit hotelToHotelResponseEdit(Hotel hotel);
}
