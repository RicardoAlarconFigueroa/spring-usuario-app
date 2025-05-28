package com.examen.usuarios.models.services;

import com.examen.usuarios.models.entities.Usuario;
import com.examen.usuarios.models.dao.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDate;
import java.util.Base64;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public Optional<Usuario> login (String login, String password){
        Usuario usuario = usuarioRepository.findById(login)
            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        return Optional.of(usuario)
                .filter(u -> u.getFechaVigencia() == null || u.getFechaVigencia().isAfter(LocalDate.now()))
                .filter(u -> u.getPassword().equals(encodePassword(password)));
    }

    public String encodePassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] hash = md.digest(password.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            throw new RuntimeException("Error codificando la contraseña", e);
        }
    }
}