package com.Microservicio.GestionDeUsuariosYRoles.model;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "rol")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Rol {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nombre;

    @ElementCollection
    @CollectionTable(name = "rol_permisos", joinColumns = @JoinColumn(name = "rol_id"))
    @Column(name = "permiso")
    private Set<String> permisos = new HashSet<>();

    @Column(nullable = false)
    private boolean activo = true;

    public String getEstado() {
        return activo ? "ACTIVO" : "INACTIVO";
    }

    @JsonIgnore
    public boolean isActivo() {
        return activo;
    }
}