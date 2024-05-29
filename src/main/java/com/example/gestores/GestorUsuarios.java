package com.example.gestores;

import com.example.model.Usuario;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Session;
import org.neo4j.driver.Transaction;
import org.neo4j.driver.TransactionWork;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.neo4j.driver.Values.parameters;

@SuppressWarnings("deprecation")
@Service
public class GestorUsuarios {
    private final Driver driver;
    private final PasswordEncoder passwordEncoder;

    public GestorUsuarios(PasswordEncoder passwordEncoder) {
        this.driver = GraphDatabase.driver("neo4j+s://87fbfc54.databases.neo4j.io", AuthTokens.basic("neo4j", "d95kqzeZGl5YcfOxDxuNQ7PZ_TYH2dUaiXxq2n6yNKc"));
        this.passwordEncoder = passwordEncoder;
    }

    public boolean existeUsuario(String nombreUsuario) {
        try (Session session = driver.session()) {
            return session.readTransaction(new TransactionWork<Boolean>() {
                @Override
                public Boolean execute(Transaction tx) {
                    var result = tx.run("MATCH (u:Usuario {username: $username}) RETURN u", parameters("username", nombreUsuario));
                    return result.hasNext();
                }
            });
        }
    }

    public void registrarUsuario(String nombreUsuario, String contrasena, String nombre, String carrera, int edad, String genero,
                                 String afluenciaPreferida, List<String> intereses, List<String> clubesAsistidos, List<String> accionesPreferidas) {
        if (!existeUsuario(nombreUsuario)) {
            try (Session session = driver.session()) {
                session.writeTransaction(new TransactionWork<Void>() {
                    @Override
                    public Void execute(Transaction tx) {
                        String encodedPassword = passwordEncoder.encode(contrasena);
                        tx.run("CREATE (u:Usuario {username: $username, password: $password, nombre: $nombre, carrera: $carrera, edad: $edad, genero: $genero, afluenciaPreferida: $afluenciaPreferida})",
                                parameters("username", nombreUsuario, "password", encodedPassword, "nombre", nombre, "carrera", carrera, "edad", edad, "genero", genero, "afluenciaPreferida", afluenciaPreferida));
                        for (String accion : accionesPreferidas) {
                            tx.run("MATCH (u:Usuario {username: $username}), (a:Accion {nombre: $accion}) " +
                                    "CREATE (u)-[:INTERESA_EN]->(a)", parameters("username", nombreUsuario, "accion", accion));
                        }
                        return null;
                    }
                });
            }
        } else {
            throw new IllegalArgumentException("El nombre de usuario ya está en uso.");
        }
    }

    public void eliminarUsuario(String nombreUsuario) {
        try (Session session = driver.session()) {
            session.writeTransaction(new TransactionWork<Void>() {
                @Override
                public Void execute(Transaction tx) {
                    tx.run("MATCH (u:Usuario {username: $username}) DETACH DELETE u", parameters("username", nombreUsuario));
                    return null;
                }
            });
        }
    }

    public Usuario iniciarSesion(String nombreUsuario, String contrasena) {
        try (Session session = driver.session()) {
            return session.readTransaction(new TransactionWork<Usuario>() {
                @Override
                public Usuario execute(Transaction tx) {
                    var result = tx.run("MATCH (u:Usuario {username: $username}) RETURN u.password AS password, u.nombre AS nombre, u.carrera AS carrera, u.edad AS edad, u.genero AS genero, u.afluenciaPreferida AS afluenciaPreferida",
                            parameters("username", nombreUsuario));
                    if (result.hasNext()) {
                        var record = result.next();
                        String storedPassword = record.get("password").asString();
                        if (passwordEncoder.matches(contrasena, storedPassword)) {
                            return new Usuario(nombreUsuario, storedPassword, record.get("nombre").asString(), record.get("carrera").asString(),
                                    record.get("edad").asInt(), record.get("genero").asString(), record.get("afluenciaPreferida").asString(), null, null, null);
                        }
                    }
                    return null;
                }
            });
        }
    }
    public List<Map<String, Object>> recommendClubsKNN(String username) {
        try (Session session = driver.session()) {
            return session.readTransaction(new TransactionWork<List<Map<String, Object>>>() {
                @Override
                public List<Map<String, Object>> execute(Transaction tx) {
                    var result = tx.run("MATCH (u:Usuario {username: $username})-[:INTERESA_EN]->(a:Accion)-[:LE_PUEDE_GUSTAR]->(c:Club) " +
                                    "RETURN DISTINCT c.nombre AS club, c.concurrencia AS concurrencia, c.horario AS horario, c.lugar AS lugar",
                            parameters("username",username));

                    List<Map<String, Object>> recommendedClubs = new ArrayList<>();
                    while (result.hasNext()) {
                        var record = result.next();
                        recommendedClubs.add(Map.of("club", record.get("club").asString()));
                    }

                    System.out.println("Recomendaciones encontradas para " + username + ": " + recommendedClubs.size());
                    for (Map<String, Object> club : recommendedClubs) {
                        System.out.println("Club recomendado: " + club.get("club"));
                    }
                    return recommendedClubs;
                }
            });
        }
    }

}
