package com.examen.usuarios.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.examen.usuarios.models.dao.UsuarioRepository;

@Controller
@RequiredArgsConstructor
public class BienvenidoController {

    private final UsuarioRepository usuarioRepository;

    @GetMapping("/bienvenido")
    public String bienvenido(Authentication authentication, Model model) {
        String username = authentication.getName();
        usuarioRepository.findById(username).ifPresent(usuario -> {
            model.addAttribute("usuario", usuario);
        });
        return "bienvenido";
    }
}