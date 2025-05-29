package com.examen.usuarios.controllers;

import com.examen.usuarios.models.entities.Usuario;
import com.examen.usuarios.models.services.UsuarioService;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private static final String USUARIOS_STRING = "usuarios";
    private static final String REDIRECT_USUARIOS = "redirect:/usuarios";

    @GetMapping()
    public String listarUsuarios(Model model) {
        model.addAttribute(USUARIOS_STRING, usuarioService.findAll());
        return USUARIOS_STRING;
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioAlta(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "formulario";
    }

    @PostMapping("/guardar")
    public String guardarUsuario(@ModelAttribute Usuario usuario, 
                                @RequestParam(value = "loginOriginal", required = false) String loginOriginal,
                                RedirectAttributes redirectAttributes) {
        try {
            // Si viene loginOriginal, es una edición
            boolean esEdicion = (loginOriginal != null && !loginOriginal.trim().isEmpty());
            
            if (esEdicion) {
                // Asegurar que el login sea el original en caso de edición
                usuario.setLogin(loginOriginal);
                usuarioService.update(usuario);
                redirectAttributes.addFlashAttribute("mensaje", "Usuario actualizado exitosamente");
            } else {
                // Es un usuario nuevo
                usuarioService.save(usuario);
                redirectAttributes.addFlashAttribute("mensaje", "Usuario creado exitosamente");
            }
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al guardar el usuario: " + e.getMessage());
        }
        
        return REDIRECT_USUARIOS;
    }

    @GetMapping("/editar/{login}")
    public String editarUsuario(@PathVariable String login, Model model, RedirectAttributes redirectAttributes) {
        try {
            Usuario usuario = usuarioService.findByLogin(login)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            
            // Limpiar SOLO la contraseña para que no aparezca en el formulario
            usuario.setPassword(null);
            model.addAttribute("usuario", usuario);
            return "formulario";
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al cargar el usuario: " + e.getMessage());
            return REDIRECT_USUARIOS;
        }
    }

    @PostMapping("/eliminar/{login}")
    public String eliminarUsuario(@PathVariable String login, RedirectAttributes redirectAttributes) {
        try {
            usuarioService.deleteByLogin(login);
            redirectAttributes.addFlashAttribute("mensaje", "Usuario eliminado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar el usuario: " + e.getMessage());
        }
        return REDIRECT_USUARIOS;
    }

    @GetMapping("/tablero")
    public String tableroUsuario(
        @RequestParam(value = "status", required = false) String status,
        @RequestParam(value = "nombre", required = false) String nombre,
        @RequestParam(value = "fechaInicial", required = false) String fechaInicial,
        @RequestParam(value = "fechaFinal", required = false) String fechaFinal,
        Model model) {
        
        try {
            // Convertir fechas
            LocalDate fechaIni = (fechaInicial != null && !fechaInicial.isEmpty()) 
                ? LocalDate.parse(fechaInicial) : null;
            LocalDate fechaFin = (fechaFinal != null && !fechaFinal.isEmpty()) 
                ? LocalDate.parse(fechaFinal) : null;
            
            // Obtener usuarios filtrados usando el service
            var usuarios = usuarioService.findWithFilters(status, nombre, fechaIni, fechaFin);
            
            // Contar por status
            long activos = usuarioService.countByStatus("A");
            long inactivos = usuarioService.countByStatus("B");
            long revocados = usuarioService.countByStatus("R");
            
            // Agregar atributos al model
            model.addAttribute(USUARIOS_STRING, usuarios);
            model.addAttribute("statusSeleccionado", status);
            model.addAttribute("nombreFiltro", nombre);
            model.addAttribute("fechaInicial", fechaInicial);
            model.addAttribute("fechaFinal", fechaFinal);
            model.addAttribute("activos", activos);
            model.addAttribute("inactivos", inactivos);
            model.addAttribute("revocados", revocados);
            
        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar el tablero: " + e.getMessage());
            model.addAttribute(USUARIOS_STRING, usuarioService.findAll());
        }
        
        return "tablero";
    }
}