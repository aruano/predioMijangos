package com.predio.mijangos.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * Utilitario JWT: generación y lectura de claims.
 * Encapsula el detalle criptográfico.
 */
@Component
public class JwtUtil {

  private final Key key;
  private final long expirationMinutes;

  public JwtUtil(
      @Value("${app.security.jwt.secret}") String secret,
      @Value("${app.security.jwt.expiration-minutes:480}") long expirationMinutes
  ) {
    // Acepta secret BASE64 o texto plano (dev). En prod usar BASE64.
    byte[] bytes;
    try { bytes = Decoders.BASE64.decode(secret); }
    catch (IllegalArgumentException ex) { bytes = secret.getBytes(StandardCharsets.UTF_8); }
    this.key = Keys.hmacShaKeyFor(bytes);
    this.expirationMinutes = expirationMinutes;
  }

  public String generateToken(UserDetails user) {
    Instant now = Instant.now();
    return Jwts.builder()
        .setSubject(user.getUsername())
        .claim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet()))
        .setIssuedAt(Date.from(now))
        .setExpiration(Date.from(now.plus(expirationMinutes, ChronoUnit.MINUTES)))
        .signWith(key, SignatureAlgorithm.HS256)
        .compact();
  }

  public String extractUsername(String token) {
    return Jwts.parserBuilder().setSigningKey(key).build()
        .parseClaimsJws(token).getBody().getSubject();
  }
}
