package com.example.todo_api.security;

import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;

import java.util.Date;

import static java.security.KeyRep.Type.SECRET;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String SECRET;

    private final long EXPIRY_MS = 1000L * 60 * 60 * 24;

    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    //called on successful login

    public String generateToken(String username) {
        return Jwts.builder().subject(username).issuedAt(new Date()).expiration(new Date(System.currentTimeMillis()+EXPIRY_MS)).signWith(getKey()).compact();
    }

    //extract username from a token (used in the filter)

    public String extractUsername(String token) {
        return Jwts.parser().verifyWith(getKey()).build().parseSignedClaims(token).getPayload().getSubject();
    }

    //return true if token signature is valid and not expired

    public boolean isTokenValid(String token) {
        try{
            extractUsername(token);
            return true;
        } catch(JwtException | IllegalArgumentException e){
            return false;
        }
    }
}
