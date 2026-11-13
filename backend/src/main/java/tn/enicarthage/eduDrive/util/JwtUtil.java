package tn.enicarthage.eduDrive.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import tn.enicarthage.eduDrive.model.Utilisateur;

import javax.crypto.SecretKey;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Properties;

public class JwtUtil {

    private static String SECRET_STRING;
    private static long EXPIRATION_TIME;

    static {
        try (InputStream input = JwtUtil.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new RuntimeException("ERREUR CRITIQUE : config.properties introuvable !");
            }

            Properties prop = new Properties();
            prop.load(input);

            SECRET_STRING = prop.getProperty("jwt.secret");
            String expirationStr = prop.getProperty("jwt.expiration");

            if (SECRET_STRING == null || SECRET_STRING.trim().isEmpty()) {
                throw new RuntimeException("ERREUR CRITIQUE : La clé jwt.secret est manquante dans config.properties.");
            }
            if (SECRET_STRING.getBytes(StandardCharsets.UTF_8).length < 32) {
                throw new RuntimeException("ERREUR CRITIQUE : La clé jwt.secret doit faire au moins 32 caractères (HS256).");
            }

            if (expirationStr == null || expirationStr.trim().isEmpty()) {
                throw new RuntimeException("ERREUR CRITIQUE : La valeur jwt.expiration est manquante dans config.properties.");
            }
            try {
                EXPIRATION_TIME = Long.parseLong(expirationStr.trim());
            } catch (NumberFormatException e) {
                throw new RuntimeException("ERREUR CRITIQUE : jwt.expiration doit être un nombre (millisecondes).");
            }

        } catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private static SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_STRING.getBytes(StandardCharsets.UTF_8));
    }

    public static String generateToken(Utilisateur user) {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("id", user.getId())
                .claim("role", user.getRole())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public static boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}