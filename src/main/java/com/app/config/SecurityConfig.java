package com.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(csrf -> csrf.disable())  // Deshabilitar CSRF explícitamente
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.GET, "/agency/hotels").permitAll()
                        .requestMatchers(HttpMethod.GET, "/agency/hotels/{id}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/agency/rooms").permitAll()
                        .requestMatchers(HttpMethod.GET, "/agency/flights").permitAll()
                        .requestMatchers(HttpMethod.GET, "/agency/flights/{id}").permitAll()
                        .requestMatchers(HttpMethod.POST, "/agency/room-booking/new").permitAll()
                        .requestMatchers(HttpMethod.POST, "/agency/flight-booking/new").permitAll()
                        .anyRequest().permitAll())  // 🔥 Permitimos el acceso a cualquier otra ruta no especificada
                .formLogin(form -> form.disable())  // Deshabilitamos el formulario de login
                .httpBasic(httpBasic -> httpBasic.disable())  // Deshabilitamos la autenticación básica
                .build();
    }
}

