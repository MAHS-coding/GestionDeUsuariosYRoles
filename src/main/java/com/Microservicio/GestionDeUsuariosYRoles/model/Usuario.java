package com.Microservicio.GestionDeUsuariosYRoles.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "usuario")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idUsuario;

    @Column(length = 150, nullable = false)
    private String nombreUsuario;

    @Column(length = 150, nullable = false)
    private String apellidoPUsuario;

    @Column(length = 150, nullable = false)
    private String apellidoMUsuario;

    @Column(length = 150, nullable = false)
    private String emailInstitucional;

    private boolean activo = true;

}
