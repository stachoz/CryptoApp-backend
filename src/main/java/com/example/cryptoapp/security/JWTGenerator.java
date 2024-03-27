package com.example.cryptoapp.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JWTGenerator {

    @Value("${JWT_SECRET}")
    private String JWT_SECRET;
    private SecretKey SECRET_JWT_KEY;
//    private static final long JWT_EXPIRATION = 1000 * 60 * 60 * 60;
    private static final long JWT_EXPIRATION = 7000;

    @PostConstruct
    private void init(){
        SECRET_JWT_KEY = Keys.hmacShaKeyFor(JWT_SECRET.getBytes());
    }

    public String generateToken(Authentication authentication){
        String username = authentication.getName();
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + JWT_EXPIRATION);
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(expireDate)
                .signWith(SECRET_JWT_KEY)
                .compact();
    }

    public String getUsernameFromJWT(String token){
        return Jwts.parser()
                .verifyWith(SECRET_JWT_KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean validateToken(String token){
        try {
            Jwts.parser().verifyWith(SECRET_JWT_KEY).build().parseSignedClaims(token);
            return true;
        } catch (Exception e){
            throw new AuthenticationCredentialsNotFoundException("JWT was expired or incorrect");
        }
    }
}