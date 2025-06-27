package com.example.hotel_service.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "hotels", indexes = @Index(name = "hotel_name_index", columnList = "name"))
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    protected UUID id;

    @NotBlank(message = "Hotel name is required")
    @Size(max = 100, message = "Hotel name must be less than 100 characters")
    @Column(name = "name", length = 100, nullable = false, unique = true)
    private String name;

    @Size(max = 60, message = "Title must be less than 60 characters")
    @Column(name = "title", length = 60)
    private String title;

    @NotBlank(message = "Country name is required")
    @Size(max = 60, message = "Country name must be less than 60 characters")
    @Column(name = "country", nullable = false, length = 60)
    private String country;

    @NotBlank(message = "City name is required")
    @Size(max = 100, message = "City name must be less than 100 characters")
    @Column(name = "city", nullable = false, length = 100)
    private String city;

    @Size(max = 100, message = "District must be less than 100 characters")
    @Column(name = "district", length = 100)
    private String district;

    @NotBlank(message = "Street is required")
    @Size(max = 150, message = "Street must be less than 150 characters")
    @Column(name = "street", nullable = false, length = 150)
    private String street;

    @Pattern(regexp = "^[0-9]{5,10}(?:-[0-9]{4})?$",
            message = "Postal code must be in valid format (e.g. 12345 or 12345-6789)")
    @Column(name = "postal_code", length = 15)
    private String postalCode;

    @NotNull(message = "Building number is required")
    @PositiveOrZero(message = "Building number must be positive")
    @Column(name = "building_number", nullable = false)
    private Integer buildingNumber;

    @PositiveOrZero(message = "Floor must be positive or zero")
    @Column(name = "floor")
    private Integer floor;

    @Size(max = 20, message = "Apartment must be less than 20 characters")
    @Column(name = "apartment", length = 20)
    private String apartment;

    @PositiveOrZero(message = "Distance must be positive or zero")
    @Column(name = "distance_from_center", precision = 19)
    private Double distanceFromCentreCity;

    @DecimalMin(value = "1.0", message = "Rating must be at least 0.0")
    @DecimalMax(value = "5.0", message = "Rating must be at most 10.0")
    @Column(name = "rating", precision = 3)
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
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL)
    private List<Room> rooms = new ArrayList<>();
}
