package com.desarrollo.adopcion.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.userdetails.UserDetails;
import com.desarrollo.adopcion.response.AuthResponse;
import com.desarrollo.adopcion.Correo.ForgotPasswordRequest;
import com.desarrollo.adopcion.exception.UserException;
import com.desarrollo.adopcion.request.LoginRequest;
import com.desarrollo.adopcion.security.jwt.JwtUtils;
import com.desarrollo.adopcion.service.UserService;
import com.desarrollo.adopcion.modelo.User;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
	
	private final UserService userService;
	private final AuthenticationManager authenticationManager;
	private final JwtUtils jwtUtils;
	
	/*
	@Autowired
	private UserService userService;
	*/
	
	@PostMapping("/login")
	public ResponseEntity<AuthResponse> authenticateUser(@Valid @RequestBody LoginRequest request) {
		
		Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.getCorreo(), request.getClave()));
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        
		String jwt = jwtUtils.generateJwtTokenForUser(authentication);
		
		AuthResponse authResponse = new AuthResponse(userDetails.getUsername(), jwt);

        return ResponseEntity.ok(authResponse);
	}

	@PostMapping("/registro")
	public ResponseEntity<?> registroUsuario(@Valid @RequestBody User user){
		try {
			userService.saveUser(user);
			return ResponseEntity.ok("Usuario registrado con exito");
		}catch(UserException ex) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
		}		
	}
	
	@PostMapping("/recuperar-clave")
	public ResponseEntity<String> olvidoClave(@RequestBody ForgotPasswordRequest request) throws MessagingException {
		userService.procesoOlvidoClave(request.getCorreo());
		return ResponseEntity.ok("Se ha enviado un enlace de recuperación a tu correo");
	}

}
