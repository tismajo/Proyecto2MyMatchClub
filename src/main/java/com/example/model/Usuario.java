package com.example.model;

import java.util.List;

public class Usuario {

    private String nombreUsuario;
    private String contrasena;
    private String nombre;
    private String carrera;
    private int edad;
    private String genero;
    private String afluenciaPreferida;
    private List<String> intereses;
    private List<String> clubesAsistidos;
    private List<String> accionesPreferidas;

    // Constructor vacío
    public Usuario() {
    }

    // Constructor completo
    public Usuario(String nombreUsuario, String contrasena, String nombre, String carrera, int edad, String genero,
                   String afluenciaPreferida, List<String> intereses, List<String> clubesAsistidos, List<String> accionesPreferidas) {
        this.nombreUsuario = nombreUsuario;
        this.contrasena = contrasena;
        this.nombre = nombre;
        this.carrera = carrera;
        this.edad = edad;
        this.genero = genero;
        this.afluenciaPreferida = afluenciaPreferida;
        this.intereses = intereses;
        this.clubesAsistidos = clubesAsistidos;
        this.accionesPreferidas = accionesPreferidas;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCarrera() {
        return carrera;
    }

    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getAfluenciaPreferida() {
        return afluenciaPreferida;
    }

    public void setAfluenciaPreferida(String afluenciaPreferida) {
        this.afluenciaPreferida = afluenciaPreferida;
    }

    public List<String> getIntereses() {
        return intereses;
    }

    public void setIntereses(List<String> intereses) {
        this.intereses = intereses;
    }

    public List<String> getClubesAsistidos() {
        return clubesAsistidos;
    }

    public void setClubesAsistidos(List<String> clubesAsistidos) {
        this.clubesAsistidos = clubesAsistidos;
    }

    public List<String> getAccionesPreferidas() {
        return accionesPreferidas;
    }

    public void setAccionesPreferidas(List<String> accionesPreferidas) {
        this.accionesPreferidas = accionesPreferidas;
    }
}
