package com.desarrollo.adopcion.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {
	
	@Autowired
	private JavaMailSender mailSender;
	
	public void sendEmail(String correo, String token) throws MessagingException {
		
		String url = "http://localhost:9192/api/auth/recuperar-clave?token=" + token;
        
		MimeMessage message = mailSender.createMimeMessage();
		
		
		message.setFrom(new InternetAddress("reynaldojblancom@gmail.com"));
	    message.setRecipients(MimeMessage.RecipientType.TO, "rey.blanco@yahoo.com");
	    message.setSubject("Zoocial, Restablecer la contraseña ");

	    String htmlContent = "<h1>Zoocial</h1>" +
	                         "<p>Para restablecer tu contraseña, haz clic en el siguiente enlace:\n</p>"+url;
	    message.setContent(htmlContent, "text/html; charset=utf-8");

	    mailSender.send(message);

	}

}
