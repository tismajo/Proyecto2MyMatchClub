package com.example;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Session;
import org.neo4j.driver.Transaction;
import org.neo4j.driver.TransactionWork;

import static org.neo4j.driver.Values.parameters;

public class ClubRecommendationSystem implements AutoCloseable {
    private final Driver driver;

    public ClubRecommendationSystem(String uri, String user, String password) {
        driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password));
    }

    @Override
    public void close() throws RuntimeException {
        driver.close();
    }

    // Método para agregar un usuario con sus preferencias
    public void addUser(String username, String password, String[] acciones, String concurrenciaPreferida, String[] horariosDisponibles, String carrera) {
        try (Session session = driver.session()) {
            session.writeTransaction(new TransactionWork<Void>() {
                @Override
                public Void execute(Transaction tx) {
                    // Buscar nodos de acción correspondientes a las acciones proporcionadas
                    for (String accion : acciones) {
                        tx.run("MATCH (a:Accion {nombre: $nombre}) RETURN a", parameters("nombre", accion));
                    }
                    
                    // Crear el nodo de usuario
                    tx.run("CREATE (u:Usuario {username: $username, password: $password, concurrenciaPreferida: $concurrenciaPreferida, horariosDisponibles: $horariosDisponibles, carrera: $carrera})",
                            parameters("username", username, "password", password, "concurrenciaPreferida", concurrenciaPreferida, "horariosDisponibles", horariosDisponibles, "carrera", carrera));

                    // Crear relaciones entre el usuario y los nodos de acción
                    for (String accion : acciones) {
                        tx.run("MATCH (u:Usuario {username: $username}), (a:Accion {nombre: $accion}) " +
                               "CREATE (u)-[:INTERESA_EN]->(a)",
                               parameters("username", username, "accion", accion));
                    }
                    return null;
                }
            });
        }
    }

    // Método para recomendar clubes a un usuario basado en sus preferencias y vecinos cercanos
    public void recommendClubsKNN(String username, int k) {
        try (Session session = driver.session()) {
            // Obtener clubes asociados a las acciones del usuario
            session.readTransaction(new TransactionWork<Void>() {
                @Override
                public Void execute(Transaction tx) {
                    tx.run("MATCH (u:Usuario {username: $username})-[:INTERESA_EN]->(a:Accion)-[:ASOCIADO_CON]->(c:Club) " +
                            "RETURN DISTINCT c.nombre AS club, c.concurrencia AS concurrencia, c.horarios AS horarios, c.facilitador AS facilitador, c.lugar AS lugar",
                            parameters("username", username));
                    return null;
                }
            });
        }
    }

    public static void main(String[] args) {
        try (var system = new ClubRecommendationSystem("neo4j+s://87fbfc54.databases.neo4j.io", "neo4j", "d95kqzeZGl5YcfOxDxuNQ7PZ_TYH2dUaiXxq2n6yNKc")) {
            String[] acciones1 = {"canta", "toca guitarra"};
            String[] horariosDisponibles1 = {"Lunes 15:00 - 16:30", "Miércoles 15:00 - 16:30"};
            system.addUser("user123", "password123", acciones1, "Alta", horariosDisponibles1, "CarreraX");

            // Recomendar clubes asociados a las acciones del usuario
            System.out.println("Recomendaciones para user123:");
            system.recommendClubsKNN("user123", 1);
        }
    }
}
