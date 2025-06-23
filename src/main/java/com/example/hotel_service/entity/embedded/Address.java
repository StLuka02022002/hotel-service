package com.example.hotel_service.entity.embedded;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class Address {

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
}
