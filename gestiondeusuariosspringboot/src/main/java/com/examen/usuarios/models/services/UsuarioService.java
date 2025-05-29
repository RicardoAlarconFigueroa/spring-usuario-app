package com.examen.usuarios.models.services;

import com.examen.usuarios.models.entities.Usuario;
import com.examen.usuarios.models.dao.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    // Método para login (manteniendo el original)
    public Optional<Usuario> login(String login, String password) {
        Usuario usuario = usuarioRepository.findByLogin(login)
            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        return Optional.of(usuario)
                .filter(u -> u.getFechaVigencia() == null || u.getFechaVigencia().isAfter(LocalDate.now()))
                .filter(u -> passwordEncoder.matches(password, u.getPassword()));
    }

    // Obtener todos los usuarios
    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    // Buscar usuario por login
    public Optional<Usuario> findByLogin(String login) {
        return usuarioRepository.findByLogin(login);
    }

    // Crear nuevo usuario
    public Usuario save(Usuario usuario) {
        // Si es un usuario nuevo y tiene contraseña, encriptarla
        if (usuario.getPassword() != null && !usuario.getPassword().trim().isEmpty()) {
            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        }
        return usuarioRepository.save(usuario);
    }

    // Actualizar usuario existente
    public Usuario update(Usuario usuario) {
        // Buscar el usuario existente
        Usuario usuarioExistente = usuarioRepository.findByLogin(usuario.getLogin())
            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        // Actualizar campos
        usuarioExistente.setNombre(usuario.getNombre());
        usuarioExistente.setEmail(usuario.getEmail());
        usuarioExistente.setCliente(usuario.getCliente());
        usuarioExistente.setStatus(usuario.getStatus());
        usuarioExistente.setFechaVigencia(usuario.getFechaVigencia());
        usuarioExistente.setApellidoPaterno(usuario.getApellidoPaterno());
        usuarioExistente.setApellidoMaterno(usuario.getApellidoMaterno());
        usuarioExistente.setArea(usuario.getArea());

        // Solo actualizar contraseña si se proporciona una nueva y no está vacía
        if (usuario.getPassword() != null && !usuario.getPassword().trim().isEmpty()) {
            usuarioExistente.setPassword(passwordEncoder.encode(usuario.getPassword()));
        }
        // Si password es null o vacío, mantener la contraseña existente (no hacer nada)

        return usuarioRepository.save(usuarioExistente);
    }

    // Eliminar usuario
    public void deleteByLogin(String login) {
        if (!usuarioRepository.existsById(login)) {
            throw new UsernameNotFoundException("Usuario no encontrado");
        }
        usuarioRepository.deleteById(login);
    }

    // Verificar si existe un usuario
    public boolean existsByLogin(String login) {
        return usuarioRepository.existsById(login);
    }

    // Filtros para el tablero
    public List<Usuario> findWithFilters(String status, String nombre, LocalDate fechaInicial, LocalDate fechaFinal) {
        List<Usuario> usuarios = usuarioRepository.findAll();

        return usuarios.stream()
            .filter(u -> status == null || status.isEmpty() || status.equalsIgnoreCase(u.getStatus()))
            .filter(u -> nombre == null || nombre.isEmpty() || 
                    u.getNombre().toLowerCase().contains(nombre.toLowerCase()))
            .filter(u -> fechaInicial == null || 
                    u.getFechaAlta().isAfter(fechaInicial) || u.getFechaAlta().isEqual(fechaInicial))
            .filter(u -> fechaFinal == null || 
                    u.getFechaAlta().isBefore(fechaFinal) || u.getFechaAlta().isEqual(fechaFinal))
            .collect(Collectors.toList());
    }

    // Contar usuarios por status
    public long countByStatus(String status) {
        return usuarioRepository.findAll().stream()
            .filter(u -> status.equals(u.getStatus()))
            .count();
    }
}