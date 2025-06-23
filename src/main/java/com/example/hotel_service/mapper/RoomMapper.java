package com.example.hotel_service.mapper;

import com.example.hotel_service.dao.request.RoomRequest;
import com.example.hotel_service.dao.responce.RoomResponse;
import com.example.hotel_service.entity.Room;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RoomMapper {

    Room roomRequestToRoom(RoomRequest request);
    RoomResponse roomToRoomResponse(Room room);

}
