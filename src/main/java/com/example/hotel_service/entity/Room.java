package com.example.hotel_service.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "rooms", indexes = @Index(name = "room_name_index", columnList = "name"))
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    protected UUID id;

    @Size(max = 100, message = "Room name must be less than 100 characters")
    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "description")
    private String description;

    @PositiveOrZero(message = "Distance must be positive or zero")
    @Column(name = "number", nullable = false)
    private Integer number;

    @PositiveOrZero(message = "Price must be positive or zero")
    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Positive(message = "Distance must be positive")
    @Column(name = "max_people_count", nullable = false)
    private Integer maxPeopleCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id", nullable = false)
    private Hotel hotel;

    @Builder.Default
    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    private List<Booking> bookings = new ArrayList<>();
}
