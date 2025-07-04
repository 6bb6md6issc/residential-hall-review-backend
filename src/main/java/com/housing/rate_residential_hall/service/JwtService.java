package com.housing.rate_residential_hall.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

  @Value("${security.jwt.secret-key}")
  private String jwtSecretKey;

  @Value("${security.jwt.expiration-time}")
  private long jwtExpirationTime;

  public String generateToken(
          UserDetails userDetails
  ){
    return buildToken(new HashMap<>(), userDetails, jwtExpirationTime);
  }

  public boolean isTokenValid(String token, UserDetails userDetails) {
    String userEmail = extractClaim(token, Claims::getSubject);
    return userEmail.equals(userDetails.getUsername()) && !isTokenExpired(token);
  }

  public String buildToken(
          Map<String, Object> extraClaims,
          UserDetails userDetails,
          long expiration
  ){
    return Jwts.builder()
            .setClaims(extraClaims)
            .setSubject(userDetails.getUsername())
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + expiration))
            .signWith(getSignInKey(), SignatureAlgorithm.HS256)
            .compact();
  }

  public String getUsername(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  private Claims extractAllClaims(String token){
    return Jwts
            .parserBuilder()
            .setSigningKey(getSignInKey())
            .build()
            .parseClaimsJws(token)
            .getBody();
  }

  private Key getSignInKey(){
    byte[] keyBytes = Decoders.BASE64.decode(jwtSecretKey);
    return Keys.hmacShaKeyFor(keyBytes);
  }

  private boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }

  private Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }

  public <T> T extractClaim (String token, Function<Claims, T> claimsResolver){
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }
}
