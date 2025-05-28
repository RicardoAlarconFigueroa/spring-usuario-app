package com.examen.usuarios.models.services;

import com.examen.usuarios.models.entities.Usuario;
import com.examen.usuarios.models.dao.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findById(username)
            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        // Usa un rol fijo para pruebas
        return org.springframework.security.core.userdetails.User
                .withUsername(usuario.getLogin())
                .password(usuario.getPassword())
                .roles("USER")
                .build();
    }
}