package com.example.hotel_service.mapper;

import com.example.hotel_service.dao.responce.BookingResponse;
import com.example.hotel_service.entity.Booking;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BookingMapper {

    BookingResponse bookingToBookingResponse(Booking booking);
}
