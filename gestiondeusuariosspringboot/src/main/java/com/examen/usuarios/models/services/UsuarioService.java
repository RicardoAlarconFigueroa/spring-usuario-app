package com.examen.usuarios.models.services;

import com.examen.usuarios.models.entities.Usuario;
import com.examen.usuarios.models.dao.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    private final PasswordEncoder passwordEncoder;

    public Optional<Usuario> login (String login, String password){
        Usuario usuario = usuarioRepository.findById(login)
            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        return Optional.of(usuario)
                .filter(u -> u.getFechaVigencia() == null || u.getFechaVigencia().isAfter(LocalDate.now()))
                .filter(u -> passwordEncoder.matches(password, u.getPassword()));
    }
}