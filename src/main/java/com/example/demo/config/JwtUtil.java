package com.example.demo.config;

import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {


    // Clé générée sécurisée (remplacez par la clé générée)
    private static final String SECRET_KEY = "oWz1T6fs1jK4R5NtG7lP5mHJQmtGKHYQ2T2oWT5yfQ8=";

    public String generateToken(String username) {
        // Crée un nouveau JWT en utilisant le builder fourni par Jwts
        return Jwts.builder()
                // Définit le "subject" du token (généralement l'identifiant de l'utilisateur, ici le username)
                .setSubject(username)
                // Définit la date de création du token
                .setIssuedAt(new Date())
                // Définit la date d'expiration du token (10 heures à partir de la création)
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 1000 ms * 60 s * 60 min * 10 h
                // Définit l'algorithme de signature (HS256) et encode le token avec une clé secrète
                .signWith(SignatureAlgorithm.HS256, Base64.getDecoder().decode(SECRET_KEY))
                // Compile le token en une chaîne compacte et lisible
                .compact();
    }

    public String extractUsername(String token) {
        return Jwts.parser() // Initialise un parser pour analyser le token JWT
                .setSigningKey(Base64.getDecoder().decode(SECRET_KEY)) // Décode et utilise la clé secrète pour valider le token
                .parseClaimsJws(token) // Analyse le JWT signé, valide sa signature et extrait son contenu (claims)
                .getBody() // Accède aux données (claims) du token après validation
                .getSubject(); // Récupère la valeur du champ "subject" défini lors de la création du token

    }

    public boolean validateToken(String token, String username) {
        String extractedUsername = extractUsername(token);
        return extractedUsername.equals(username) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {

        Date expiration = Jwts.parser() // Initialise un parser JWT pour analyser le token
                .setSigningKey(Base64.getDecoder().decode(SECRET_KEY)) // Décode et utilise la clé secrète pour valider le token
                .parseClaimsJws(token) // Analyse et valide le token signé, extrait son contenu (claims)
                .getBody() // Accède aux données (claims) du token après validation
                .getExpiration(); // Récupère la date d'expiration (claim "exp") du token

        // Retourne "true" si la date d'expiration du token est avant la date actuelle, sinon "false"
        return expiration.before(new Date());
    }
}

