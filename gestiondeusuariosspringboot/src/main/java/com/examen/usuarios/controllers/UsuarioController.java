package com.examen.usuarios.controllers;

import com.examen.usuarios.models.entities.Usuario;
import com.examen.usuarios.models.services.UsuarioService;
import com.examen.usuarios.models.dao.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.time.LocalDate;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequiredArgsConstructor
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    private static final String USUARIOSC_STRING = "usuarios";

    @GetMapping()
    public String listarUsuarios(Model model) {
        model.addAttribute(USUARIOSC_STRING, usuarioRepository.findAll());
        return USUARIOSC_STRING;
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioAlta(Model model){
        model.addAttribute("usuario", new Usuario());
        return "formulario";
    }

    @PostMapping("/guardar")
    public String guardarUsuario(@ModelAttribute Usuario usuario) {
        // Si es edición y la contraseña está vacía, mantener la contraseña actual
        if (usuario.getLogin() != null && (usuario.getPassword() == null || usuario.getPassword().trim().isEmpty())) {
            Usuario usuarioExistente = usuarioRepository.findById(usuario.getLogin()).orElseThrow();
            usuario.setPassword(usuarioExistente.getPassword());
        } else if (usuario.getPassword() != null && !usuario.getPassword().trim().isEmpty()) {
            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        }
        
        usuarioRepository.save(usuario);
        return "redirect:/usuarios";
    }

    @GetMapping("/editar/{login}")
    public String editarUsuario(@PathVariable String login, Model model) {
        model.addAttribute("usuario", usuarioRepository.findById(login).orElseThrow());
        return "formulario";
    }

    @PostMapping("/eliminar/{login}")
    public String eliminarUsuario(@PathVariable String login) {
        usuarioRepository.deleteById(login);
        return "redirect:/usuarios";
    }

    @GetMapping("/tablero")
    public String tableroUsuario(
        @RequestParam(value = "status", required = false) String status,
        @RequestParam(value = "nombre", required = false) String nombre,
        @RequestParam(value = "fechaInicial", required = false) String fechaInicial,
        @RequestParam(value = "fechaFinal", required = false) String fechaFinal,
        Model model) {
        
        List<Usuario> todosUsuarios = usuarioRepository.findAll();
        List<Usuario> usuarios = todosUsuarios;
        
        // Filtrar por status
        if (status != null && !status.isEmpty()) {
            usuarios = usuarios.stream()
                .filter(u -> status.equalsIgnoreCase(u.getStatus()))
                .toList();
        }
        
        // Filtrar por nombre
        if (nombre != null && !nombre.isEmpty()) {
            usuarios = usuarios.stream()
                .filter(u -> u.getNombre().toLowerCase().contains(nombre.toLowerCase()))
                .toList();
        }
        
        // Filtrar por fecha inicial
        if (fechaInicial != null && !fechaInicial.isEmpty()) {
            LocalDate fechaIni = LocalDate.parse(fechaInicial);
            usuarios = usuarios.stream()
                .filter(u -> u.getFechaAlta().isAfter(fechaIni) || u.getFechaAlta().isEqual(fechaIni))
                .toList();
        }
        
        // Filtrar por fecha final
        if (fechaFinal != null && !fechaFinal.isEmpty()) {
            LocalDate fechaFin = LocalDate.parse(fechaFinal);
            usuarios = usuarios.stream()
                .filter(u -> u.getFechaAlta().isBefore(fechaFin) || u.getFechaAlta().isEqual(fechaFin))
                .toList();
        }
        
        // Contar por status para las tarjetas
        long activos = todosUsuarios.stream().filter(u -> "A".equals(u.getStatus())).count();
        long inactivos = todosUsuarios.stream().filter(u -> "B".equals(u.getStatus())).count();
        long revocados = todosUsuarios.stream().filter(u -> "R".equals(u.getStatus())).count();
        
        // Agregar atributos al model
        model.addAttribute(USUARIOSC_STRING, usuarios);
        model.addAttribute("statusSeleccionado", status);
        model.addAttribute("nombreFiltro", nombre);
        model.addAttribute("fechaInicial", fechaInicial);
        model.addAttribute("fechaFinal", fechaFinal);
        model.addAttribute("activos", activos);
        model.addAttribute("inactivos", inactivos);
        model.addAttribute("revocados", revocados);
        
        return "tablero";
    }
}