package com.desarrollo.adopcion.service;

import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.desarrollo.adopcion.DTO.MatchDto;
import com.desarrollo.adopcion.repository.ICoincidenciaRepository;
import com.desarrollo.adopcion.repository.IPetRepository;
import com.desarrollo.adopcion.repository.IUserRepository;
import com.desarrollo.adopcion.utils.Utilidades;
import com.desarrollo.adopcion.modelo.Coincidencia;
import com.desarrollo.adopcion.modelo.Pet;
import com.desarrollo.adopcion.modelo.User;

@Service
public class CoincidenciaService {
	
	@Autowired
	private ICoincidenciaRepository matchRepository;
	@Autowired
	private IUserRepository userRepository;
	@Autowired
	private IPetRepository petRepository;
	@Autowired
	private EmailService emailService;
	@Autowired
	private Utilidades utilidades;
	
	public List<Pet> getMatchByUsersPet(String correo) {
		Optional<User> user = userRepository.findByCorreo(correo);
		
		List<Pet> userPets = petRepository.findByUser(user); // listado de mascotas de usuario en linea
		List<Coincidencia> coincidencias = matchRepository.findAll(); // listado de todas las coincidencias
		
		List<Pet> matchUser = new ArrayList<>();
		for (Coincidencia match : coincidencias) {
			boolean matchPet1 = userPets.contains(match.getPet1());
			boolean matchPet2 = userPets.contains(match.getPet2());
			if(matchPet1) {
				matchUser.add(match.getPet2());
			}else if(matchPet2){
				matchUser.add(match.getPet1());
			}
		}
		return matchUser;
	}
	
	public void sendMessage(Long matchId, String correo, String message) {
        Coincidencia coincide = matchRepository.findById(matchId).orElseThrow(() -> new RuntimeException("Match no encontrado"));
        Optional<User> sender = userRepository.findByCorreo(correo);
        System.out.println(coincide+" "+sender);
        //User recipient = coincide.getPet1().getUser().getId().equals(sender.get().getId()) ? coincide.getPet2().getUser() : coincide.getPet1().getUser();

        // Add the message to the match's messages list
        //match.getMessages().add(message);
        //matchRepository.save(match);

        // Send email notification
        //emailService.sendEmail(recipient.getCorreo(), "Nuevo mensaje de " + sender.get().getCorreo(), message);
    }
	
	private MatchDto convertToMatchDto(Coincidencia match) throws SQLException {
        MatchDto matchDto = new MatchDto();
        matchDto.setId(match.getId());
        matchDto.setPet1(utilidades.convertToDto(match.getPet1()));
        matchDto.setPet2(utilidades.convertToDto(match.getPet2()));
        matchDto.setFecha_match(match.getFecha_match());
        matchDto.setMessages(match.getMessages());
        return matchDto;
    }


}

