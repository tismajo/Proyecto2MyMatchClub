package com.example.controller;

import com.example.model.Usuario;
import com.example.gestores.GestorUsuarios;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private GestorUsuarios gestorUsuarios;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody Usuario usuario) {
        boolean isRegistered = gestorUsuarios.registrarUsuario(
                usuario.getNombreUsuario(),
                usuario.getContrasena(),
                usuario.getNombre(),
                usuario.getCarrera(),
                usuario.getEdad(),
                usuario.getGenero(),
                usuario.getAfluenciaPreferida(),
                usuario.getIntereses(),
                usuario.getClubesAsistidos(),
                usuario.getAccionesPreferidas()
        );
        if (isRegistered) {
            return ResponseEntity.ok("Usuario registrado con éxito.");
        } else {
            return ResponseEntity.badRequest().body("El nombre de usuario ya está en uso.");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Usuario usuario) {
        Usuario loggedInUser = gestorUsuarios.iniciarSesion(usuario.getNombreUsuario(), usuario.getContrasena());
        if (loggedInUser != null) {
            return ResponseEntity.ok("Inicio de sesión exitoso.");
        } else {
            return ResponseEntity.status(401).body("Nombre de usuario o contraseña incorrectos.");
        }
    }
}
