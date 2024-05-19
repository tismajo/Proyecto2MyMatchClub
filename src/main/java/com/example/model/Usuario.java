package com.example.model;

public class Usuario {
    private String nombreUsuario;
    private String contrasena;
    private Estudiante estudiante;

    public Usuario(String nombreUsuario, String contrasena, Estudiante estudiante) {
        this.nombreUsuario = nombreUsuario;
        this.contrasena = contrasena;
        this.estudiante = estudiante;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public boolean verificarContrasena(String contrasena) {
        return this.contrasena.equals(contrasena);
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }
}

