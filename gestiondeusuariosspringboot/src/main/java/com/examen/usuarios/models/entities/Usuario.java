package com.examen.usuarios.models.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "usuario")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario{

    @Id
    @Column(name = "login", nullable = false, length = 20)
    private String login;

    @Column(name = "\"password\"", nullable = false, length = 30) // <-- comillas para coincidir con el DDL
    private String password;

    @Column(name = "nombre", nullable = false, length = 50)
    private String nombre;

    @Column(name = "cliente", nullable = false)
    private Double cliente;

    @Column(name = "email", length = 50)
    private String email;

    @Column(name = "fecha_alta", nullable = false)
    private LocalDate fechaAlta;

    @Column(name = "fecha_baja")
    private LocalDate fechaBaja;

    @Column(name = "status", nullable = false, length = 1)
    private String status;

    @Column(name = "intentos", nullable = false)
    private Double intentos;

    @Column(name = "fecha_revocado")
    private LocalDate fechaRevocado;

    @Column(name = "fecha_vigencia")
    private LocalDate fechaVigencia;

    @Column(name = "no_acceso")
    private Integer noAcceso;

    @Column(name = "apellido_paterno", length = 50)
    private String apellidoPaterno;

    @Column(name = "apellido_materno", length = 50)
    private String apellidoMaterno;

    @Column(name = "area")
    private Integer area;

    @Column(name = "fecha_modificacion", nullable = false)
    private LocalDate fechaModificacion;

    @PrePersist
    public void prePersist(){
        LocalDate ahora = LocalDate.now();
        if (fechaAlta == null) fechaAlta = ahora;
        if (fechaModificacion == null) fechaModificacion = ahora;
        if (status == null) status = "A";
        if (intentos == null) intentos = 0.0;
    }

    @PreUpdate
    public void preUpdate(){
        fechaModificacion = LocalDate.now();
    }
}
