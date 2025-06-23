package com.example.hotel_service.repository;

import com.example.hotel_service.entity.Room;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RoomRepository extends BaseRepository<Room>, JpaSpecificationExecutor<Room> {
}
