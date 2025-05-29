package com.examen.usuarios.config;

import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, 
                                      HttpServletResponse response, 
                                      AuthenticationException exception) throws IOException, ServletException {
        
        String errorMessage = "Error de autenticación";
        
        // Determinar el tipo específico de error
        if (exception instanceof BadCredentialsException) {
            errorMessage = "Usuario o contraseña incorrectos";
        } else if (exception instanceof UsernameNotFoundException) {
            String message = exception.getMessage();
            if (message.contains("vencido")) {
                errorMessage = "Su cuenta ha expirado. Contacte al administrador";
            } else if (message.contains("inactivo")) {
                errorMessage = "Su cuenta está inactiva. Contacte al administrador";
            } else {
                errorMessage = "Usuario no encontrado";
            }
        } else if (exception instanceof AccountExpiredException) {
            errorMessage = "Su cuenta ha expirado";
        } else if (exception instanceof LockedException) {
            errorMessage = "Su cuenta está bloqueada";
        } else if (exception instanceof DisabledException) {
            errorMessage = "Su cuenta está deshabilitada";
        }
        
        // Codificar el mensaje para URL
        String encodedError = URLEncoder.encode(errorMessage, StandardCharsets.UTF_8);
        
        // Redireccionar con el mensaje de error
        response.sendRedirect("/login?error=true&message=" + encodedError);
    }
}