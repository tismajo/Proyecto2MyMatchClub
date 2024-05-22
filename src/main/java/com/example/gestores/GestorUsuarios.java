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

import java.util.List;

import static org.neo4j.driver.Values.parameters;

@Service
public class GestorUsuarios {
    private final Driver driver;
    private final PasswordEncoder passwordEncoder;

    public GestorUsuarios(PasswordEncoder passwordEncoder) {
        this.driver = GraphDatabase.driver("neo4j+s://87fbfc54.databases.neo4j.io", AuthTokens.basic("neo4j", "d95kqzeZGl5YcfOxDxuNQ7PZ_TYH2dUaiXxq2n6yNKc"));
        this.passwordEncoder = passwordEncoder;
    }

    public boolean registrarUsuario(String nombreUsuario, String contrasena, String nombre, String carrera, int edad, String genero,
                                    String afluenciaPreferida, List<String> intereses, List<String> clubesAsistidos, List<String> accionesPreferidas) {
        try (Session session = driver.session()) {
            return session.writeTransaction(new TransactionWork<Boolean>() {
                @Override
                public Boolean execute(Transaction tx) {
                    // Check if user already exists
                    var result = tx.run("MATCH (u:Usuario {username: $username}) RETURN u", parameters("username", nombreUsuario));
                    if (result.hasNext()) {
                        return false;
                    }
                    // Encode password
                    String encodedPassword = passwordEncoder.encode(contrasena);
                    // Create user node
                    tx.run("CREATE (u:Usuario {username: $username, password: $password, nombre: $nombre, carrera: $carrera, edad: $edad, genero: $genero, afluenciaPreferida: $afluenciaPreferida})",
                            parameters("username", nombreUsuario, "password", encodedPassword, "nombre", nombre, "carrera", carrera, "edad", edad, "genero", genero, "afluenciaPreferida", afluenciaPreferida));
                    // Create relationships with actions
                    for (String accion : accionesPreferidas) {
                        tx.run("MATCH (a:Accion {nombre: $accion}) CREATE (u)-[:INTERESA_EN]->(a)", parameters("accion", accion, "username", nombreUsuario));
                    }
                    // Create relationships with clubs
                    for (String club : clubesAsistidos) {
                        tx.run("MATCH (c:Club {nombre: $club}) CREATE (u)-[:ASISTE_A]->(c)", parameters("club", club, "username", nombreUsuario));
                    }
                    return true;
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
}
