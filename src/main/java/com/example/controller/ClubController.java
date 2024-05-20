package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/club")
public class ClubController {

    @GetMapping("/recomendacion")
    public String obtenerRecomendacion() {
        // Lógica para obtener recomendación de clubes
        return "recomendacion_clubes"; // Devuelve el nombre de la vista
    }
}