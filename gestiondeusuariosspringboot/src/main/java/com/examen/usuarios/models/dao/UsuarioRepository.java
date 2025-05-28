package com.examen.usuarios.models.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.examen.usuarios.models.entities.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, String>{

    Optional<Usuario> findByLogin(String login);
}