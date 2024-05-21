package com.example.gestores;

import com.example.model.Usuario;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GestorUsuarios {
    private Map<String, Usuario> usuariosRegistrados = new HashMap<>();

    private final PasswordEncoder passwordEncoder;

    public GestorUsuarios(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public boolean registrarUsuario(String nombreUsuario, String contrasena, String nombre, String carrera, int edad, String genero,
                                    String afluenciaPreferida, List<String> intereses, List<String> clubesAsistidos, List<String> accionesPreferidas) {
        if (usuariosRegistrados.containsKey(nombreUsuario)) {
            return false;
        }
        String encodedPassword = passwordEncoder.encode(contrasena);
        Usuario nuevoUsuario = new Usuario(nombreUsuario, encodedPassword, nombre, carrera, edad, genero,
                afluenciaPreferida, intereses, clubesAsistidos, accionesPreferidas);
        usuariosRegistrados.put(nombreUsuario, nuevoUsuario);
        return true;
    }

    public Usuario iniciarSesion(String nombreUsuario, String contrasena) {
        Usuario usuario = usuariosRegistrados.get(nombreUsuario);
        if (usuario != null && passwordEncoder.matches(contrasena, usuario.getContrasena())) {
            return usuario;
        }
        return null;
    }
}
