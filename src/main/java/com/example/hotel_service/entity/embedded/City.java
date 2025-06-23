package com.example.hotel_service.entity.embedded;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class City {

    @NotBlank(message = "City name is required")
    @Size(max = 100, message = "City name must be less than 100 characters")
    @Column(name = "city", nullable = false, length = 100)
    private String name;

    @NotBlank(message = "Country name is required")
    @Size(max = 60, message = "Country name must be less than 60 characters")
    @Column(name = "country", nullable = false, length = 60)
    private String country;
}
