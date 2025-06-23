package com.example.hotel_service.repository;

import com.example.hotel_service.entity.Hotel;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface HotelRepository extends BaseRepository<Hotel>, JpaSpecificationExecutor<Hotel> {
}
