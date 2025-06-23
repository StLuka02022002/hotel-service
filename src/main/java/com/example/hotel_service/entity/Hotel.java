package com.example.hotel_service.entity;

import com.example.hotel_service.entity.embedded.Address;
import com.example.hotel_service.entity.embedded.City;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "hotels", indexes = @Index(name = "hotel_name_index", columnList = "name"))
public class Hotel extends AbstractEntity {

    @NotBlank(message = "Hotel name is required")
    @Size(max = 100, message = "Hotel name must be less than 100 characters")
    @Column(name = "name", length = 100, nullable = false, unique = true)
    private String name;

    @Size(max = 60, message = "Title must be less than 60 characters")
    @Column(name = "title", length = 60)
    private String title;

    @Embedded
    private City city;

    @Embedded
    private Address address;

    @PositiveOrZero(message = "Distance must be positive or zero")
    @Column(name = "distance_from_center", precision = 5) // Вернуть scale для Postgers
    private Double distanceFromCentreCity;

    @DecimalMin(value = "1.0", message = "Rating must be at least 0.0")
    @DecimalMax(value = "5.0", message = "Rating must be at most 10.0")
    @Column(name = "rating", precision = 3) // Вернуть scale для Postgers
    private Double rating;

    @PositiveOrZero(message = "Estimation count must be positive or zero")
    @Column(name = "estimation_count")
    private Integer countEstimation;

    @CreationTimestamp
    @Column(name = "created_time", updatable = false)
    private LocalDateTime createTime;

    @UpdateTimestamp
    @Column(name = "updated_time")
    private LocalDateTime updatedTime;

    @Builder.Default
    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL)
    private List<Room> rooms = new ArrayList<>();
}
