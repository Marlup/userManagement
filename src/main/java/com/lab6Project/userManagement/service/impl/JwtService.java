package com.lab6Project.userManagement.service.impl;

import com.lab6Project.userManagement.entities.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${token.secret.key}")
    String jwtSecretKey;

    @Value("${token.expirationMs}")
    Long jwtExpirationMs;

    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public String returnEmptyToken() {
        return generateEmptyToken();
    }

    //public boolean isTokenValid(String token, UserDetails userDetails) {
    public boolean isTokenValid(String token, UserEntity userDetails) {
        final String userName = extractUserName(token);

        return (userName.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    // Internal usage methods
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        // Sets the configuration, generates and returns a JWT token
        return Jwts
                .builder() // Initialize a customizable token object
                .setClaims(extraClaims) // The generated claims
                .setSubject(userDetails.getUsername()) // The username
                .setIssuedAt(new Date(System.currentTimeMillis())) // The current date in milliseconds
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs)) // Expiration: now + duration
                .signWith(getSigningKey(), SignatureAlgorithm.HS256) // Sign the token by signing key and JWA algorithm
                .compact(); // Actually builds the JWT and serializes it into compact URL-safe string
    }

    private String generateEmptyToken() {
        // Sets the configuration, generates and returns a JWT token
        return Jwts
                .builder() // Initialize a customizable token object
                .setSubject("") // The username
                .setIssuedAt(new Date(System.currentTimeMillis())) // The current date in milliseconds
                .setExpiration(new Date(System.currentTimeMillis())) // Expiration: now + duration
                .compact(); // Actually builds the JWT and serializes it into compact URL-safe string
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
