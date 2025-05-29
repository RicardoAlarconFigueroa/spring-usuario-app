package com.examen.usuarios.controllers;

import com.examen.usuarios.models.services.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class BienvenidoController {

    private final UsuarioService usuarioService;

    @GetMapping("/bienvenido")
    public String bienvenido(Authentication authentication, Model model) {
        String username = authentication.getName();
        usuarioService.findByLogin(username).ifPresent(usuario -> {
            model.addAttribute("usuario", usuario);
        });
        return "bienvenido";
    }
}