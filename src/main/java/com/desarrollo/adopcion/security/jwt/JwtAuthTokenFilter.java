package com.desarrollo.adopcion.security.jwt;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import com.desarrollo.adopcion.security.user.UserDetailService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthTokenFilter extends OncePerRequestFilter {

	@Autowired
	private JwtUtils jwtUtils;
	@Autowired
	private UserDetailService userDetailService;
	@Autowired
	private static final Logger logger = LoggerFactory.getLogger(JwtAuthTokenFilter.class);
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		try {
			String token = getTokenFromRequest(request);
			if(token !=null && jwtUtils.validateToken(token)) {
				 String correo = jwtUtils.getUserNameFromToken(token);
				 UserDetails userDetails = userDetailService.loadUserByUsername(correo);
				 var authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
				 authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				 SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		}catch(Exception e) {
			logger.error("No puedo establecer la autenticación del usuario : {} ", e.getMessage());
		}
		filterChain.doFilter(request, response);
	}
	
	
	private String getTokenFromRequest(HttpServletRequest request) {
		
        String headerAuth = request.getHeader(HttpHeaders.AUTHORIZATION);
        
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")){
            return headerAuth.substring(7);
        }
        
        return null;
    }

}
