package com.rent_management_system.authentication;

import com.rent_management_system.exception.UnAuthorizedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


@Slf4j
@Component
public class JWTAccess {

    String SECRET = "RKUGLRKBKBSKLGSFIJSBKFBKJSDJBVugdtyidvctyfktvgkuyrcggchvrydtxtxuvyvgghghhhjhkjkjjurtyvkgvK";
    long MINUTES = TimeUnit.MINUTES.toMillis(30);

    /**
     * @auther Emmanuel Yidana
     * @description: A method to generate a jwt token
     * @date 016-01-2025
     * @param: username
     * @return token
     */
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

    /**
     * @auther Emmanuel Yidana
     * @description: A method to extract user details or claims from a token
     * @date 016-01-2025
     * @param: token
     * @throws UnAuthorizedException - throws UnAuthorizedException if token is invalid
     * @return claims such as email, username,authorities etc
     */
    public Claims getClaims(String token) {
        try{
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (SignatureException | ExpiredJwtException e) {
            throw new UnAuthorizedException("Invalid token or signature");
        }
    }

    /**
     * @auther Emmanuel Yidana
     * @description: A method to extract username from a token
     * @date 016-01-2025
     * @param: token
     * @return username
     */
    public String extractUsername(String token){
        Claims claims = getClaims(token);
        return claims.getSubject();
    }

    /**
     * @auther Emmanuel Yidana
     * @description: A method to check if token is valid
     * @date 016-01-2025
     * @param: token
     * @return boolean
     */
    public boolean  isTokenValid(String token){
        Claims claims = getClaims(token);
        return Date.from(Instant.now()).before(claims.getExpiration());
    }

    /**
     * @auther Emmanuel Yidana
     * @description: A method to generate a secret key for token generation and verification
     * @date 016-01-2025
     * @return SecretKey
     */
    private SecretKey secretKey(){
        byte[] decodedKey = Base64.getDecoder().decode(SECRET);
        return Keys.hmacShaKeyFor(decodedKey);
    }
}