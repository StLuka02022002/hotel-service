package com.example.hotel_service.mapper;

import com.example.hotel_service.dto.request.HotelRequest;
import com.example.hotel_service.dto.responce.HotelResponse;
import com.example.hotel_service.dto.responce.HotelResponseEdit;
import com.example.hotel_service.entity.Hotel;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface HotelMapper {

    Hotel hotelRequestToHotel(HotelRequest hotelRequest);

    HotelResponse hotelToHotelResponse(Hotel hotel);

    HotelResponseEdit hotelToHotelResponseEdit(Hotel hotel);
}
