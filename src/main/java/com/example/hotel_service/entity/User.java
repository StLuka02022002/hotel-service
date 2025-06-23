package com.example.hotel_service.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "users", indexes = {
        @Index(name = "user_name_index", columnList = "name"),
        @Index(name = "email_name_index", columnList = "email")})
public class User extends AbstractEntity implements UserDetails {

    @NotBlank(message = "User name is required")
    @Size(max = 30, message = "User name must be less than 30 characters")
    @Column(name = "name", length = 30, nullable = false, unique = true)
    private String name;

    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 30, message = "Password must be less than 30 and more then 6  characters")
    @Column(name = "password", length = 30, nullable = false)
    private String password;

    @Column(name = "email", unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Booking> bookings = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.role.toString()));
    }

    @Override
    public String getUsername() {
        return this.name;
    }
}
