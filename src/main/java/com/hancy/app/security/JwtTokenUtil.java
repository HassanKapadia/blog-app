package com.hancy.app.security;

import com.hancy.app.common.constants.BlogAppConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenUtil {

  private static final String SECRET_KEY = "thisIsMyVeryLongSuperSecretTokenKey";
  private static final long EXPIRATION_MS = 24 * 60 * 60 * 1000; // 1 day

  private final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

  public String generateToken(Long userId, String username) {
    return Jwts.builder()
        .setSubject(username)
        .claim(BlogAppConstants.AUTH_USER_ID, userId)
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_MS))
        .signWith(key, SignatureAlgorithm.HS256)
        .compact();
  }

  public Jws<Claims> validateToken(String token) {
    return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
  }

  public Long extractUserId(String token) {
    return validateToken(token).getBody().get(BlogAppConstants.AUTH_USER_ID, Long.class);
  }

  public String extractUsername(String token) {
    return validateToken(token).getBody().getSubject();
  }
}
