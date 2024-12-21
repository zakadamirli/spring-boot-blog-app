package com.zekademirli.questapp.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${questApp.app.secret}")
    private String APP_SECRET;

    @Value("${questApp.expires.in}")
    private long EXPIRES_IN;

    public String generateJwtToken(Authentication authentication) {
        JwtUserDetails jwtUserDetails = (JwtUserDetails) authentication.getPrincipal();
        long now = System.currentTimeMillis();

        Key secretKey = Keys.hmacShaKeyFor(APP_SECRET.getBytes());

        return Jwts.builder()
                .setSubject(String.valueOf(jwtUserDetails.getId()))
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + EXPIRES_IN))
                .signWith(secretKey, SignatureAlgorithm.HS256) // SecretKey ilə imzalama
                .compact();
    }

    public String generateJwtTokenByUserId(Long id) {
        long now = System.currentTimeMillis();
        Key secretKey = Keys.hmacShaKeyFor(APP_SECRET.getBytes());
        return Jwts.builder()
                .setSubject(String.valueOf(id))
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + EXPIRES_IN))
                .signWith(secretKey, SignatureAlgorithm.HS256) // SecretKey ilə imzalama
                .compact();
    }

    public Long getUserIdFromJwtToken(String token) {
        Key secretKey = Keys.hmacShaKeyFor(APP_SECRET.getBytes());

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return Long.parseLong(claims.getSubject());
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Key secretKey = Keys.hmacShaKeyFor(APP_SECRET.getBytes());
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(authToken);
            return !isTokenExpired(authToken);
        } catch (SignatureException | MalformedJwtException | ExpiredJwtException | UnsupportedJwtException |
                 IllegalArgumentException e) {
            return false;
        }
    }

    private boolean isTokenExpired(String authToken) {
        Key secretKey = Keys.hmacShaKeyFor(APP_SECRET.getBytes());

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(authToken)
                .getBody();
        Date expiration = claims.getExpiration();
        return expiration.before(new Date());
    }

}
