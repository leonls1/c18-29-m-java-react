package com.desarrollo.adopcion.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.mailersend.sdk.emails.Email;
import com.mailersend.sdk.MailerSend;
import com.mailersend.sdk.MailerSendResponse;
import com.mailersend.sdk.exceptions.MailerSendException;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendRecoverPasswordEmail(String correo, String token) throws MessagingException {

        System.out.println("sendEmail!");
        String url = "http://localhost:9192/api/auth/recuperar-clave?token=" + token;

        MimeMessage message = mailSender.createMimeMessage();

        message.setFrom(new InternetAddress("MS_gjQsaN@trial-351ndgwyq0xlzqx8.mlsender.net"));
        /*message.setFrom(new InternetAddress("zoocial@demomailtrap.com"));*/
        message.setRecipients(MimeMessage.RecipientType.TO, "reynaldojblancom@gmail.com");
        message.setSubject("Zoocial, Restablecer la contraseña ");

        String htmlContent = "<h1>Zoocial</h1>"
                + "<p>Para restablecer tu contraseña, haz clic en el siguiente enlace:\n</p>" + url;
        message.setContent(htmlContent, "text/html; charset=utf-8");

        mailSender.send(message);

    }
    
    public void sendMessageEmail(String toEmail, String content, String fromUser, String fromPet) throws AddressException, MessagingException{
        MimeMessage message = mailSender.createMimeMessage();
        
        message.setFrom(new InternetAddress("MS_gjQsaN@trial-351ndgwyq0xlzqx8.mlsender.net"));
        message.setRecipients(MimeMessage.RecipientType.TO, toEmail);
        
        message.setSubject("Mensaje del usuario "+ fromUser + " dueño de la mascota "+ fromPet);
        message.setContent(content,"text/html; charset=utf-8");
          
        
    }

    public void sendCorreo() {

        System.out.println("sendCorreo!");
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

}
