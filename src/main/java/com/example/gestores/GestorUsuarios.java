package com.example.gestores;

import com.example.model.Estudiante;
import com.example.model.Usuario;

import java.util.HashMap;
import java.util.Map;

public class GestorUsuarios {
    private Map<String, Usuario> usuariosRegistrados;

    public GestorUsuarios() {
        this.usuariosRegistrados = new HashMap<>();
    }

    public boolean registrarUsuario(String nombreUsuario, String contrasena, Estudiante estudiante) {
        if (usuariosRegistrados.containsKey(nombreUsuario)) {
            System.out.println("El nombre de usuario ya está en uso.");
            return false;
        }
        usuariosRegistrados.put(nombreUsuario, new Usuario(nombreUsuario, contrasena, estudiante));
        System.out.println("Usuario registrado con éxito.");
        return true;
    }

    public Usuario iniciarSesion(String nombreUsuario, String contrasena) {
        Usuario usuario = usuariosRegistrados.get(nombreUsuario);
        if (usuario != null && usuario.verificarContrasena(contrasena)) {
            System.out.println("Inicio de sesión exitoso.");
            return usuario;
        }
        System.out.println("Nombre de usuario o contraseña incorrectos.");
        return null;
    }
    public Usuario getUsuario(String nombreUsuario) {
        return usuariosRegistrados.get(nombreUsuario);
    }
}