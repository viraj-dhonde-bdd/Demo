package com.example.demo.service;

import java.util.Properties;

import org.springframework.stereotype.Service;

import jakarta.mail.Authenticator;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

@Service
public class FreeSmsSenderService {
    	
    public static void sendFreeSms() {
        String host = "smtp.gmail.com";
        final String user = "virtej104@gmail.com";
        final String password = "Tejvir@13";

        // Example carrier gateway: number@vtext.com (Verizon)
        String recipient = "7977578930@vtext.com"; 

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(user));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
            message.setSubject(""); // Keep subject empty for SMS
            message.setText("Hello, this is a free SMS from Java!");

            Transport.send(message);
            System.out.println("SMS sent successfully!");

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}