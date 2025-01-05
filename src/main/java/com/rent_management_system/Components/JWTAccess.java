package com.rent_management_system.Components;

import com.rent_management_system.Exception.InvalidDataException;
import com.rent_management_system.Exception.UnAuthorizedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class JWTAccess {

    String SECRET = "RKUGLRKBKBSKLGSFIJSBKFBKJSDJBVugdtyidvctyfktvgkuyrcggchvrydtxtxuvyvgghghhhjhkjkjjurtyvkgvK";
    long MINUTES = TimeUnit.MINUTES.toMillis(30);

    public String generateToken(String username){
        Map<String, Object> claims = new HashMap<>();
        claims.put("issuer", "www.emma.com");
        return Jwts.builder()
                .setClaims(claims)
                .signWith(secretKey())
                .setExpiration(Date.from(Instant.now().plusMillis(MINUTES)))
                .setIssuedAt(Date.from(Instant.now()))
                .setSubject(username)
                .compact();
    }

    public Claims getClaims(String token) {
       try{
           return Jwts.parserBuilder()
                   .setSigningKey(secretKey())
                   .build()
                   .parseClaimsJws(token)
                   .getBody();
       } catch (ExpiredJwtException e) {
           throw new UnAuthorizedException("Invalid token");
       }

    }

    public String extractUsername(String token){
        Claims claims = getClaims(token);
        return claims.getSubject();
    }

    public boolean  isTokenValid(String token){
        Claims claims = getClaims(token);
        return Date.from(Instant.now()).before(claims.getExpiration());
    }

    private SecretKey secretKey(){
        byte[] decodedKey = Base64.getDecoder().decode(SECRET);
        return Keys.hmacShaKeyFor(decodedKey);
    }
}