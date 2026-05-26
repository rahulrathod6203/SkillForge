package com.awp.auth.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JWTTokenProvider {

    @Value("${app.jwt-secret}")
    private String JWT_SECRET;

    @Value("${app.jwt-expiration-milliseconds}")
    private Long JWT_EXPIRE_TIME;

    //Generate JWT Token
    public String generateToken(Authentication authentication) {

        String username = authentication.getName();
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + JWT_EXPIRE_TIME);

        String roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        return Jwts.builder()
                .subject(username)
                .claim("roles", roles)
                .issuedAt(currentDate)
                .expiration(expireDate)
                .signWith(key())
                .compact();
    }

    //
    private SecretKey key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(JWT_SECRET));
    }

    // Get Username from JWT Token
    public String getUsername(String token) {
        return Jwts.parser()
                .verifyWith(key())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    // Validate JWT Token Layout
    public Boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(key())
                    .build()
                    .parseSignedClaims(token);
            return true;

        } catch (MalformedJwtException exception) {
            throw new RuntimeException("Invalid JWT Token");

        } catch (ExpiredJwtException exception) {
            throw new RuntimeException("JWT Token expired");

        } catch (IllegalArgumentException exception) {
            throw new RuntimeException("JWT claims string is empty.");

        } catch (Exception exception) {
            throw new RuntimeException("JWT signature validation failed.");
        }
    }

}