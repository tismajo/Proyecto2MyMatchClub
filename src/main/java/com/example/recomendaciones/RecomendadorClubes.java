package com.example.recomendaciones;

import com.example.model.Club;
import com.example.model.Estudiante;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RecomendadorClubes {
    public List<String> obtenerVecinos(String clubInteres, Map<String, Club> grafo) {
        List<String> vecinos = new ArrayList<>();
        for (Club club : grafo.values()) {
            if (clubInteres.equals(club.categoria)) {
                vecinos.add(club.nombre);
            }
        }
        return vecinos;
    }
    // Función para encontrar clubes similares a los que un estudiante ya está interesado
    public List<String> recomendacionClubes(Estudiante estudiante, Map<String, Club> grafo) {
        List<String> clubesRecomendados = new ArrayList<>();
        for (String interes : estudiante.getIntereses()) {
            List<String> vecinos = obtenerVecinos(interes, grafo);
            for (String vecino : vecinos) {
                if (!estudiante.getClubesAsistidos().contains(vecino) && !clubesRecomendados.contains(vecino)) {
                    clubesRecomendados.add(vecino);}
            }
        }
        return clubesRecomendados;
    }
}

