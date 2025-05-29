package com.examen.usuarios.models.services;

import com.examen.usuarios.models.entities.Usuario;
import com.examen.usuarios.models.dao.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Usar findByLogin para consistencia
        Usuario usuario = usuarioRepository.findByLogin(username)
            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        // Validar fecha de vigencia
        if (usuario.getFechaVigencia() != null && usuario.getFechaVigencia().isBefore(LocalDate.now())) {
            throw new UsernameNotFoundException("Usuario vencido");
        }

        // Validar que el usuario esté activo
        if (!"A".equals(usuario.getStatus())) {
            throw new UsernameNotFoundException("Usuario inactivo");
        }

        return org.springframework.security.core.userdetails.User
                .withUsername(usuario.getLogin())
                .password(usuario.getPassword())
                .roles("USER")
                .build();
    }
}