package com.example.model;

import java.util.List;

public class Club {
    public String nombre;
    public String categoria;
    List<String> horario;
    String accion;
    String afluencia;


    public Club(String nombre, String categoria, List<String> horario, String accion, String afluencia) {
        this.nombre = nombre;
        this.categoria = categoria;
        this.horario = horario;
        this.accion = accion;
        this.afluencia = afluencia;
    }
}