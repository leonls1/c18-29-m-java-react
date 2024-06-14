package com.desarrollo.adopcion.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;

@Component
public class JwtUtils {
	
	private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
	
	@Value("${auth.token.jwtSecret}")
	private String jwtSecret;
	
	@Value("${auth.token.expirationInMills}")
	private int jwtExpirationMs;
	
	public String generateJwtTokenForUser(Authentication authentication) {
		UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
		String role = userPrincipal.getAuthorities().toString();
				
		return Jwts.builder()
				.setSubject(userPrincipal.getUsername())
				.claim("role",role)
				.setIssuedAt(new Date())
				.setExpiration(new Date((new Date()).getTime()+jwtExpirationMs))
				.signWith(key(), SignatureAlgorithm.HS256).compact();
	}
		
	private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }
	
	public String getUserNameFromToken(String token){
        return Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token).getBody().getSubject();
    }

	public boolean validateToken(String token){
        try{
            Jwts.parserBuilder().setSigningKey(key()).build().parse(token);
            return true;
        }catch(MalformedJwtException e){
            logger.error("Jwt token Invalido: {} ", e.getMessage());
        }catch (ExpiredJwtException e){
            logger.error("Token expiro : {} ", e.getMessage());
        }catch (UnsupportedJwtException e){
            logger.error("Este token no es soportado : {} ", e.getMessage());
        }catch (IllegalArgumentException e){
            logger.error("Reclamo no encontrado : {} ", e.getMessage());
        }
        return false;
    }

}
