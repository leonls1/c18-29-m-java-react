package com.desarrollo.adopcion.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.desarrollo.adopcion.exception.UserException;
import com.desarrollo.adopcion.modelo.User;
import com.desarrollo.adopcion.repository.IUserRepository;
import com.desarrollo.adopcion.Correo.ClaveResetToken;
import com.desarrollo.adopcion.Correo.ClaveResetTokenRepository;
import com.desarrollo.adopcion.modelo.Coincidencia;
import com.desarrollo.adopcion.modelo.Message;
import com.desarrollo.adopcion.modelo.Pet;
import com.desarrollo.adopcion.repository.ICoincidenciaRepository;
import com.desarrollo.adopcion.repository.IMessageRepository;
import com.desarrollo.adopcion.request.MessageRequest;

import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ClaveResetTokenRepository tokenRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private ICoincidenciaRepository conincidenciaRepository;

    @Autowired
    private IMessageRepository messageRepository;

    @Override
    public User saveUser(User user) {
        if (userRepository.existsByCorreo(user.getCorreo())) {
            throw new UserException(user.getCorreo() + " ya está registrado!");
        }
        user.setClave(passwordEncoder.encode(user.getClave()));
        user.setCreadoEn(LocalDateTime.now());
        return userRepository.save(user);
    }

    public User updateUser(String correo, User user) {
        if (userRepository.existsByCorreo(correo)) {
            System.out.println("Consiguio al usuario");
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
        if (user != null) {
            userRepository.deleteByCorreo(correo);
        }

    }

    @Override
    public User getUserByCorreo(String correo) {
        return userRepository.findByCorreo(correo).orElseThrow();
    }

    public void procesoOlvidoClave(String correo) throws MessagingException {

        if (userRepository.existsByCorreo(correo)) {
            System.out.println("procesoOlvidoClave ");
            User user = userRepository.findByCorreo(correo).orElseThrow();
            String token = UUID.randomUUID().toString();
            ClaveResetToken claveResetToken = new ClaveResetToken(token, user);
            tokenRepository.save(claveResetToken);
            emailService.sendRecoverPasswordEmail(correo, token);
            emailService.sendCorreo();
        }

    }

    public boolean sendMessage(MessageRequest request) {
        Message message = new Message();

        //setting the match to the current message
        Coincidencia coincidencia = conincidenciaRepository.findById(request.getId_match()).orElseThrow();
        if (coincidencia == null) {
            return false;
        }
        message.setMatch(coincidencia);

        //setting the pets to the message
        Pet petSender, petReceiver;

        if (coincidencia.getPet1().getId() == request.getIdPetSender()) {
            petSender = coincidencia.getPet1();
            petReceiver = coincidencia.getPet2();

        } else {
            petReceiver = coincidencia.getPet1();
            petSender = coincidencia.getPet2();
        }

        //getting the receiver user info
        User userSender = petSender.getUser();
        
        String receiverEmail = userSender.getCorreo();

        //setting all left atributes to the message
        message.setSend_date(new Date());
        message.setMessage(request.getContent());
        message.setSender(petSender.getUser());

        //saving the message
        messageRepository.save(message);

        //sending the message
        try {
            emailService.sendMessageEmail( petReceiver.getUser().getCorreo(),
                    request.getContent(),
                    userSender.getNombre(),
                    petSender.getNombre());

        } catch (Exception e) {
            return false;
        }

        return true;
    }

}
