package com.mang.management.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {
   /* @Value("${spring.jpa.properties.jwt.secret-key}")
    private String SECRET_KEY;
    @Value("${spring.jpa.properties.jwt.access-expiration-time}")
    private  long EXPIRATION_TIME;*/
    private final Key key;
    private final long expiration;

    public JwtUtil(@Value("${spring.jpa.properties.jwt.secret-key}") String secret, @Value("${spring.jpa.properties.jwt.access-expiration-time}") long expiration){
        this.key= Keys.hmacShaKeyFor(secret.getBytes());
        this.expiration=expiration;
    }
    public String generateToken(String email){
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+expiration))
                .signWith(key,SignatureAlgorithm.HS256)
                .compact();
    }
    public String extraUsername(String token){
        return Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String token){
        try {
            Jwts.parser().setSigningKey(key).parseClaimsJws(token);
            return true;
        }catch (Exception e){
            return false;
        }
    }
}
