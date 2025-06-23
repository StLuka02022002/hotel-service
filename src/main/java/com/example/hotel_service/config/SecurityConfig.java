package com.example.hotel_service.config;

import com.example.hotel_service.entity.Role;
import com.example.hotel_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationConverter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserService userService;

    @Bean
    public SecurityFilterChain config(HttpSecurity http) throws Exception {
        http.httpBasic(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((request) ->
                        request.requestMatchers(HttpMethod.POST, "/api/users/login").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/hotels/mark").authenticated()
                                .requestMatchers(HttpMethod.GET, "/api/statistics/export").hasAuthority(Role.ADMIN.name())
                                .requestMatchers(HttpMethod.POST, "/api/hotels/**").hasAuthority(Role.ADMIN.name())
                                .requestMatchers(HttpMethod.PUT, "/api/hotels/**").hasAuthority(Role.ADMIN.name())
                                .requestMatchers(HttpMethod.DELETE, "/api/hotels/**").hasAuthority(Role.ADMIN.name())
                                .requestMatchers(HttpMethod.POST, "/api/rooms/**").hasAuthority(Role.ADMIN.name())
                                .requestMatchers(HttpMethod.PUT, "/api/rooms/**").hasAuthority(Role.ADMIN.name())
                                .requestMatchers(HttpMethod.DELETE, "/api/rooms/**").hasAuthority(Role.ADMIN.name())
                                .requestMatchers(HttpMethod.GET, "/api/booking").hasAuthority(Role.ADMIN.name())
                                .anyRequest().authenticated())
                .userDetailsService(userService);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return NoOpPasswordEncoder.getInstance();
    }
}
