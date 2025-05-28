package com.examen.usuarios.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class LoginController {
    
    @GetMapping({"/", "/login"})
    public String loginForm() {
        return "login";
    }
}