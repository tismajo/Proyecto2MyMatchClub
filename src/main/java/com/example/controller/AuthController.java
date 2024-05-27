package com.example.controller;

import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import com.example.model.Usuario;
import com.example.gestores.GestorUsuarios;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private GestorUsuarios gestorUsuarios;

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<String> register(@RequestParam MultiValueMap<String, String> formData) {
        // Extrae los datos del formulario
        String nombreUsuario = formData.getFirst("nombreUsuario");
        String contrasena = formData.getFirst("contrasena");
        String nombre = formData.getFirst("nombre");
        String carrera = formData.getFirst("carrera");
        int edad = Integer.parseInt(formData.getFirst("edad"));
        String genero = formData.getFirst("genero");
        String afluenciaPreferida = formData.getFirst("afluenciaPreferida");
        List<String> intereses = Arrays.asList(formData.getFirst("intereses").split(","));
        List<String> clubesAsistidos = Arrays.asList(formData.getFirst("clubesAsistidos").split(","));
        List<String> accionesPreferidas = Arrays.asList(formData.getFirst("accionesPreferidas").split(","));

        try {
            // Realiza el registro del usuario utilizando el GestorUsuarios
            gestorUsuarios.registrarUsuario(
                    nombreUsuario,
                    contrasena,
                    nombre,
                    carrera,
                    edad,
                    genero,
                    afluenciaPreferida,
                    intereses,
                    clubesAsistidos,
                    accionesPreferidas
            );
            return ResponseEntity.ok("Usuario registrado con éxito.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
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

    @GetMapping("/{username}/recomendaciones")
    public ResponseEntity<List<Map<String, Object>>> obtenerRecomendaciones(@PathVariable String username, @RequestParam int k) {
        List<Map<String, Object>> recomendaciones = gestorUsuarios.recommendClubsKNN(username, k);
        if (recomendaciones != null && !recomendaciones.isEmpty()) {
            return ResponseEntity.ok(recomendaciones);
        } else {
            return ResponseEntity.noContent().build();
        }
    }
}
