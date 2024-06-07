package com.desarrollo.adopcion.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.desarrollo.adopcion.exception.UserException;
import com.desarrollo.adopcion.modelo.Estado;
import com.desarrollo.adopcion.modelo.Role;
import com.desarrollo.adopcion.modelo.User;
import com.desarrollo.adopcion.repository.IUserRepository;
import com.desarrollo.adopcion.Correo.ClaveResetToken;
import com.desarrollo.adopcion.Correo.ClaveResetTokenRepository;

import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
	
	private final IUserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final ClaveResetTokenRepository tokenRepository;

	@Autowired
	private EmailService emailService;

	@Override
	public User saveUser(User user) {
		if(userRepository.existsByCorreo(user.getCorreo())) {
			throw new UserException(user.getCorreo()+" ya está registrado!");
		}
		user.setEstado(Estado.ACTIVO);
		user.setRole(Role.USER);
		user.setClave(passwordEncoder.encode(user.getClave()));
		user.setCreadoEn(LocalDateTime.now());
		return userRepository.save(user);
	}
	
	public User updateUser(String correo, User user) {
		if (userRepository.existsByCorreo(correo)) {
			User u = userRepository.findByCorreo(correo).orElse(null);
			u.setNombre(user.getNombre());
			u.setApellido(user.getApellido());
			u.setCorreo(user.getCorreo());
			return userRepository.save(u);
		}
		return null;
	}

	@Override
	public List<User> getUsers() {
		return userRepository.findAll();
	}

	@Transactional
	@Override
	public void deleteUser(String correo) {
		User user = getUserByCorreo(correo);
		if(user!=null) {
			userRepository.deleteByCorreo(correo);
		}
		
	}

	@Override
	public User getUserByCorreo(String correo) {
		return userRepository.findByCorreo(correo).orElseThrow();
	}
	
	public void procesoOlvidoClave(String correo) throws MessagingException {

		if (userRepository.existsByCorreo(correo)) {
			User user = userRepository.findByCorreo(correo).orElseThrow();
			String token = UUID.randomUUID().toString();
			ClaveResetToken claveResetToken = new ClaveResetToken(token, user);
			tokenRepository.save(claveResetToken);
			emailService.sendEmail(correo, token);
			emailService.sendCorreo();
		}
	}

}
