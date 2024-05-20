package com.example.service;

import com.example.model.Estudiante;
import com.example.model.Usuario;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {

    private final Map<String, Usuario> usuariosRegistrados;

    public UserService() {
        // Podrías inicializar este mapa con datos de una base de datos u otro almacenamiento
        // en tu implementación real
        usuariosRegistrados = new HashMap<>();
    }

    // Método para registrar un nuevo usuario
    public boolean registrarUsuario(String nombreUsuario, String contrasena, Estudiante estudiante) {
        if (usuariosRegistrados.containsKey(nombreUsuario)) {
            // El nombre de usuario ya está en uso
            return false;
        }
        // Crear y agregar el nuevo usuario al mapa
        usuariosRegistrados.put(nombreUsuario, new Usuario(nombreUsuario, contrasena, estudiante));
        return true;
    }

    // Método para iniciar sesión
    public Usuario iniciarSesion(String nombreUsuario, String contrasena) {
        Usuario usuario = usuariosRegistrados.get(nombreUsuario);
        if (usuario != null && usuario.verificarContrasena(contrasena)) {
            // Inicio de sesión exitoso
            return usuario;
        }
        // Nombre de usuario o contraseña incorrectos
        return null;
    }
}
