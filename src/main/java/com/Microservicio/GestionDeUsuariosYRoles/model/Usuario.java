package com.Microservicio.GestionDeUsuariosYRoles.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(Include.NON_NULL)
@Entity
@Table(name = "usuario")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idUsuario;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoUsuario tipoUsuario;

    @Column(length = 150, nullable = false)
    private String nombreUsuario;

    @Column(length = 150, nullable = false)
    private String apellidoPUsuario;

    @Column(length = 150, nullable = false)
    private String apellidoMUsuario;

    @Column(length = 150, nullable = false, unique = true)
    private String emailInstitucional;

    @JsonIgnore
    @Column(nullable = false)
    private boolean activo = true;

    public String getEstado() {
        return activo ? "ACTIVO" : "INACTIVO";
    }

    // Email automatico
    public void generarEmailInstitucional() {
        String nombreFormateado = this.nombreUsuario.toLowerCase().replace(" ", "");
        String apellidoFormateado = this.apellidoPUsuario.toLowerCase().replace(" ", "");
        String dominio = switch (this.tipoUsuario) {
            case ADMINISTRADOR -> "@admin.duocuc.cl";
            case PROFESOR -> "@profesor.duocuc.cl";
            case ESTUDIANTE -> "@duocuc.cl";
        };
        this.emailInstitucional = nombreFormateado + apellidoFormateado + dominio;
    }

    @ManyToOne
    @JoinColumn(name = "rol_id")
    private Rol rol;
}
