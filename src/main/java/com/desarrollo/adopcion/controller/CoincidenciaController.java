package com.desarrollo.adopcion.controller;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.desarrollo.adopcion.DTO.PetDto;
import com.desarrollo.adopcion.modelo.Pet;
import com.desarrollo.adopcion.modelo.User;
import com.desarrollo.adopcion.service.CoincidenciaService;
import com.desarrollo.adopcion.service.EmailService;
import com.desarrollo.adopcion.service.UserService;
import com.desarrollo.adopcion.utils.Utilidades;

@RestController
@RequestMapping("/api/matches")
@CrossOrigin(origins = "*", maxAge=3600)
public class CoincidenciaController {
	
	@Autowired
	public CoincidenciaService matchService;
	@Autowired
	public UserService userService;
	@Autowired
	public EmailService emailService;
	@Autowired
	public Utilidades utilidades;
	
	@GetMapping
    public ResponseEntity<List<PetDto>> getUserMatches(Authentication authentication) {
        User user = userService.getUserByCorreo(authentication.getName());
        List<Pet> matches = matchService.getMatchByUsersPet(user.getCorreo());
        List<PetDto> petDtos = matches.stream().map(t -> {
			try {
				return utilidades.convertToDto(t);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return null;
		}).collect(Collectors.toList());
        return ResponseEntity.ok(petDtos);
    }

    @PostMapping("/message")
    public ResponseEntity<?> sendMessageToMatch(
    		@RequestParam Long matchId, 
    		@RequestParam String message, 
    		Authentication authentication) {
        
        User user = userService.getUserByCorreo(authentication.getName());

        matchService.sendMessage(matchId, user.getCorreo(), message);

        return ResponseEntity.ok("Mensaje enviado con éxito");
    }

}
