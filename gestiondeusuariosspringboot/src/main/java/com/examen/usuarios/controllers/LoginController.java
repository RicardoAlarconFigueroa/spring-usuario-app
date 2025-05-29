package com.examen.usuarios.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class LoginController {
    
    @GetMapping({"/", "/login"})
    public String loginForm(
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "message", required = false) String message,
            @RequestParam(value = "logout", required = false) String logout,
            Model model) {
        
        // Si hay parámetro "error" en la URL
        if (error != null) {
            // Si hay mensaje personalizado, usarlo; sino, mensaje genérico
            String errorMessage = (message != null && !message.isEmpty()) 
                ? message 
                : "Usuario o contraseña incorrectos. Por favor, inténtelo de nuevo.";
            model.addAttribute("error", errorMessage);
        }
        
        // Si hay parámetro "logout" en la URL
        if (logout != null) {
            model.addAttribute("mensaje", "Ha cerrado sesión correctamente.");
        }
        
        return "login";
    }
    
    // Método adicional para manejar errores específicos (opcional)
    @GetMapping("/login-error")
    public String loginError(Model model) {
        model.addAttribute("error", "Error de autenticación. Verifique sus credenciales.");
        return "login";
    }
}