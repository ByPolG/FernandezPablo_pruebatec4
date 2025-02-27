package com.app.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    private Long id;

    @Column(nullable = false, unique = true)
    @JsonProperty("nombre")
    private String nombre;

    @Column(nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) // Esto oculta la contraseña en respuestas JSON
    private String contrasenia;

    @Enumerated(EnumType.STRING)
    @JsonProperty("rol")
    private Role rol;

    @ManyToMany(mappedBy = "huespedes")
    private List<ReservaHotel> reservasHotel;

    @ManyToMany(mappedBy = "pasajeros")
    private List<ReservaVuelo> reservasVuelo;

    public enum Role {
        ADMIN, USER
    }
}