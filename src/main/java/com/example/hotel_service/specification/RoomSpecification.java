package com.example.hotel_service.specification;

import com.example.hotel_service.entity.Booking;
import com.example.hotel_service.entity.Room;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.UUID;

public class RoomSpecification {

    public static Specification<Room> createSpecification(String id, String name, String hotelId,
                                                          Double minPrice, Double maxPrice, Integer maxPeopleCount,
                                                          LocalDate checkInDate, LocalDate checkOutDate) {
        UUID uuid = id == null ? null : UUID.fromString(id);
        UUID uuidHotel = hotelId == null ? null : UUID.fromString(hotelId);
        return Specification.where(RoomSpecification.hasId(uuid))
                .and(RoomSpecification.hasName(name))
                .and(RoomSpecification.hasHotelId(uuidHotel))
                .and(RoomSpecification.hasPrice(minPrice, maxPrice))
                .and(RoomSpecification.hasMaxPeopleCount(maxPeopleCount))
                .and(RoomSpecification.isAvailableBetween(checkInDate, checkOutDate));
    }

    public static Specification<Room> hasId(UUID id) {
        return (root, query, criteriaBuilder) ->
                id == null ? null : criteriaBuilder.equal(root.get("id"), id);
    }

    public static Specification<Room> hasName(String name) {
        return createLikeSpecification("name", name);
    }

    public static Specification<Room> hasHotelId(UUID hotelId) {
        return (root, query, criteriaBuilder) -> hotelId == null ? null :
                criteriaBuilder.equal(root.get("hotel").get("id"), hotelId);
    }

    public static Specification<Room> hasPrice(Double minPrice, Double maxPrice) {
        return (root, query, cb) -> (minPrice == null || maxPrice == null) ? null :
                cb.and(cb.greaterThanOrEqualTo(root.get("price"), minPrice),
                        cb.lessThanOrEqualTo(root.get("price"), maxPrice));
    }

    public static Specification<Room> hasMaxPeopleCount(Integer peopleCount) {
        return (root, query, cb) -> peopleCount == null ? null :
                cb.greaterThanOrEqualTo(root.get("maxPeopleCount"), peopleCount);
    }

    public static Specification<Room> isAvailableBetween(LocalDate checkInDate, LocalDate checkOutDate) {
        return (root, query, cb) -> {
            if (checkInDate == null || checkOutDate == null) {
                return null;
            }

            Subquery<UUID> subquery = query.subquery(UUID.class);
            Root<Booking> booking = subquery.from(Booking.class);
            subquery.where(
                    cb.equal(booking.get("room").get("id"), root.get("id")),
                    cb.lessThan(booking.get("checkInDate"), checkOutDate),
                    cb.greaterThan(booking.get("checkOutDate"), checkInDate)
            );

            return cb.not(cb.exists(subquery));
        };
    }

    private static Specification<Room> createLikeSpecification(String field, String value) {
        return (root, query, criteriaBuilder) -> !(value != null && !value.isBlank()) ? null :
                criteriaBuilder.like(criteriaBuilder.lower(root.get(field)), "%" + value.toLowerCase() + "%");
    }
}
