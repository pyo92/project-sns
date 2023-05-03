package com.example.projectsns.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

public class JwtTokenUtils {

    public static String getEmail(String token, String jwtSecretKey) {
        return extractClaims(token, jwtSecretKey).get("email", String.class);
    }

    public static boolean isExpired(String token, String jwtSecretKey) {
        return ((Date) extractClaims(token, jwtSecretKey).getExpiration()).before(new Date());
    }

    private static Claims extractClaims(String token, String jwtSecretKey) {
        return Jwts.parserBuilder().setSigningKey(getKey(jwtSecretKey))
                .build()
                .parseClaimsJws(token) //jws 임에 유의
                .getBody();
    }

    public static String generateToken(String userName, String jwtSecretKey, long expiredTimeMs) {
        Claims claims = Jwts.claims();
        claims.put("email", userName);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiredTimeMs))
                .signWith(getKey(jwtSecretKey), SignatureAlgorithm.HS256)
                .compact();
    }

    private static Key getKey(String jwtSecretKey) {
        return Keys.hmacShaKeyFor(jwtSecretKey.getBytes(StandardCharsets.UTF_8));
    }
}
