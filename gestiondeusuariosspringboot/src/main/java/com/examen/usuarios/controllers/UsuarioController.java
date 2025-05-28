package com.examen.usuarios.controllers;

import com.examen.usuarios.models.entities.Usuario;
import com.examen.usuarios.models.services.UsuarioService;
import com.examen.usuarios.models.dao.UsuarioRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

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
        if (usuario.getPassword() != null){
            usuario.setPassword(usuarioService.encodePassword(usuario.getPassword()));
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
        @RequestParam(value = "status", required = false) String status, Model model) {
        List<Usuario> usuarios = (status != null)
            ? usuarioRepository.findAll().stream()
            .filter(u -> status.equalsIgnoreCase(u.getStatus())).toList()
            : usuarioRepository.findAll();
        model.addAttribute(USUARIOSC_STRING, usuarios);
        model.addAttribute("statusSeleccionado", status);
        return "/tablero";
    }
}