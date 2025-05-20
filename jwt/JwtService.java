package com.example.HRMS.config.jwt;

import com.example.HRMS.config.UserDetailsImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtService {

    @Value("${sample.secret}")
    private String SECRET_KEY;

    @Value("${sample.jwt.expiration}")
    private int jwtExpirationMs;

    public String generateToken(UserDetailsImpl userDetails){
        Map<String,Object> claims = new HashMap<>();
        return createToken(claims,userDetails.getUserId());
    }

    private String createToken(Map<String, Object> claims, String username) {
        return Jwts
                .builder()
                .claims(claims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(getSignKey())
                .compact();
    }

    private SecretKey getSignKey() {
        byte[] byteKey = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(byteKey);
    }

    private Claims extractAllClaim(String token){
        return Jwts
                .parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public<T> T extractClaim(String token, Function<Claims,T> resolver){
        Claims claims = extractAllClaim(token);
        return resolver.apply(claims);
    }

    public String extractUserName(String token){
        return extractClaim(token,Claims::getSubject);
    }

    public boolean isValid(String token,UserDetails userDetails){
        String userName = extractUserName(token);
        return userName.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token,Claims::getExpiration);
    }
}
