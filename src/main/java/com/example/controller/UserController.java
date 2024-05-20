package com.example.controller;

import com.example.gestores.GestorUsuarios;
import com.example.model.Club;
import com.example.model.Usuario;
import com.example.recomendaciones.RecomendadorClubes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
class UserController {
    private final GestorUsuarios gestorUsuarios = new GestorUsuarios();
    private final Map<String, Club> grafo = new HashMap<>();

    public UserController() {
        // Inicializar algunos clubes para simular datos de ejemplo
        grafo.put("Marimba", new Club("Marimba", "Musical", Arrays.asList("Miércoles 18:15-19:45", "Viernes 14:15-16:30"), "tocar", "Alta"));
        grafo.put("Futsal", new Club("Futsal", "Deportiva", Arrays.asList("Lunes y miércoles 15:00-16:30"), "correr", "Moderada"));
        grafo.put("Coro Voces del Valle", new Club("Coro Voces del Valle", "Musical", Arrays.asList("Martes 15:30-17:30", "Sábado 13:00-15:00"), "cantar", "Baja"));
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, Model model) {
        Usuario usuario = gestorUsuarios.iniciarSesion(username, password);
        if (usuario != null) {
            model.addAttribute("usuario", usuario);
            return "bienvenida";
        } else {
            return "error";
        }
    }

    @GetMapping("/recomendacion")
    public String recomendacion(@RequestParam String username, Model model) {
        Usuario usuario = gestorUsuarios.getUsuario(username);
        if (usuario != null) {
            RecomendadorClubes recomendador = new RecomendadorClubes();
            List<String> recomendaciones = recomendador.recomendacionClubes(usuario.getEstudiante(), grafo);
            model.addAttribute("recomendaciones", recomendaciones);
            return "recomendaciones";
        } else {
            return "error";
        }
    }

}