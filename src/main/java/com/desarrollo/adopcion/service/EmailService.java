package com.desarrollo.adopcion.service;

import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.mailersend.sdk.emails.Email;
import com.desarrollo.adopcion.modelo.Pet;
import com.desarrollo.adopcion.modelo.PetPhotos;
import com.desarrollo.adopcion.modelo.User;
import com.mailersend.sdk.MailerSend;
import com.mailersend.sdk.MailerSendResponse;
import com.mailersend.sdk.exceptions.MailerSendException;

import java.sql.SQLException;
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
		
		message.setFrom(new InternetAddress("MS_gjQsaN@trial-351ndgwyq0xlzqx8.mlsender.net"));
		/*message.setFrom(new InternetAddress("zoocial@demomailtrap.com"));*/
	    message.setRecipients(MimeMessage.RecipientType.TO, "reynaldojblancom@gmail.com");
	    message.setSubject("Zoocial, Restablecer la contraseña ");

	    String htmlContent = "<h1>Zoocial</h1>" +
	                         "<p>Para restablecer tu contraseña, haz clic en el siguiente enlace:\n</p>"+url;
	    message.setContent(htmlContent, "text/html; charset=utf-8");

	    mailSender.send(message);

	}
	
	public void sendCorreo() {

	    Email email = new Email();

	    email.setFrom("Reynaldo Blanco", "MS_gjQsaN@trial-351ndgwyq0xlzqx8.mlsender.net");
	    email.addRecipient("Reinaldo Blanco", "reynaldojblancom@gmail.com");

	    email.setSubject("Recuperacion de Clave");

	    email.setPlain("This is the text content");
	    email.setHtml("This is the HTML content");

	    MailerSend ms = new MailerSend();

	    ms.setToken("mlsn.70f07fd60d9105fdaa799d627de1e64a468ce5a7cac61e854b8b1059cac81dfb");

	    try {

	        MailerSendResponse response = ms.emails().send(email);
	        System.out.println(response.messageId);
	    } catch (MailerSendException e) {
	        e.printStackTrace();
	    }
	}
	
	public void sendLikeNotification(String to, User fromUser, Pet fromPet, Pet toPet) {
		System.out.println("Llega a notificar a "+to);
        try {
            MimeMessage message = mailSender.createMimeMessage();

            message.setFrom(new InternetAddress("MS_gjQsaN@trial-351ndgwyq0xlzqx8.mlsender.net"));
            message.setRecipients(MimeMessage.RecipientType.TO, to);
            message.setSubject("¡Tu mascota ha recibido un Me Gusta!");

            String htmlContent = "<p>Hola,</p>" +
                             "<p>Tu mascota " + toPet.getNombre() + " ha recibido un Me Gusta de " + fromUser.getNombre() + ".</p>" +
                             "<p>Aquí están las fotos de la mascota del usuario:</p>";

            for (PetPhotos foto : fromPet.getPhotos()) {
                String base64Image = Base64.getEncoder().encodeToString(foto.getPhoto().getBytes(1, (int) foto.getPhoto().length()));
                htmlContent += "<img src='data:image/jpeg;base64," + base64Image + "' alt='Foto de mascota' />";
            }

            message.setContent(htmlContent, "text/html; charset=utf-8");

            mailSender.send(message);
        } catch (MessagingException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void sendMatchNotification(String to1, String to2, Pet fromPet, Pet toPet) {
    	System.out.println("Llega a notificar a "+to1+" y "+to2);
        try {
            MimeMessage message1 = mailSender.createMimeMessage();

            message1.setFrom(new InternetAddress("MS_gjQsaN@trial-351ndgwyq0xlzqx8.mlsender.net"));
            message1.setRecipients(MimeMessage.RecipientType.TO, to1);
            message1.setSubject("¡Coincidencia encontrada!");

            String content1 = "<p>Hola,</p>" +
                             "<p>Tu mascota " + fromPet.getNombre() + " ha hecho match con " + toPet.getNombre() + ".</p>";


            MimeMessage message2 = mailSender.createMimeMessage();

            message2.setFrom(new InternetAddress("MS_gjQsaN@trial-351ndgwyq0xlzqx8.mlsender.net"));
            message2.setRecipients(MimeMessage.RecipientType.TO, to2);
            message2.setSubject("¡Coincidencia encontrada!");

            String content2 = "<p>Hola,</p>" +
                             "<p>Tu mascota " + toPet.getNombre() + " ha hecho match con " + fromPet.getNombre() + ".</p>";

            message1.setContent(content1, "text/html; charset=utf-8");
            message2.setContent(content2, "text/html; charset=utf-8");

            mailSender.send(message1);
            mailSender.send(message2);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

}
