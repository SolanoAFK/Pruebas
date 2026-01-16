package solano.com.civil.portal.security;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import solano.com.civil.portal.config.JwtProperties;

@Component
@Slf4j
public class JwtUtil {
    
    @Autowired
    private JwtProperties jwtProperties;
    
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes());
    }
    
    /**
     * Genera un JWT a partir de los detalles del usuario
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername());
    }
    
    /**
     * Genera un JWT con claims personalizados
     */
    public String generateToken(String username, Map<String, Object> claims) {
        return createToken(claims, username);
    }
    
    /**
     * Crea el token JWT
     */
    private String createToken(Map<String, Object> claims, String subject) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtProperties.getExpiration());
        
        return Jwts.builder()
                .claims().add(claims).and()
                .subject(subject)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSigningKey())
                .compact();
    }
    
    /**
     * Extrae el nombre de usuario del token
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }
    
    /**
     * Extrae la fecha de expiración del token
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
    
    /**
     * Extrae un claim específico del token
     */
    public <T> T extractClaim(String token, java.util.function.Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    
    /**
     * Extrae todos los claims del token
     */
    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (RuntimeException e) {
            log.error("Error al parsear el token JWT: ", e.getMessage());
            throw new RuntimeException("Token JWT inválido", e);
        }
    }
    
    /**
     * Verifica si el token ha expirado
     */
    private Boolean isTokenExpired(String token) {
        try {
            return extractExpiration(token).before(new Date());
        } catch (RuntimeException e) {
            return true;
        }
    }
    
    /**
     * Valida el token contra los detalles del usuario
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
    
    /**
     * Valida el token (solo verifica que sea válido)
     */
    public Boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            return !isTokenExpired(token);
        } catch (RuntimeException e) {
            log.error("Error al validar el token JWT: {}", e.getMessage());
            return false;
        }
    }
}
