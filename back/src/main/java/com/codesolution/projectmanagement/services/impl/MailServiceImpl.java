package com.codesolution.projectmanagement.services.impl;

import com.codesolution.projectmanagement.services.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailServiceImpl implements MailService {

    @Autowired
    private JavaMailSender mailSender;

    public boolean sendNotification(String email, String message) {
        //Un simple service pour envoyer des notifications par mail
        //La connexion au serveur se gere via application.properties
        //Ici on va envoyer le mail en utilisant comme serveur de test Mailtrap

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("noreply@demomailtrap.co");
        mailMessage.setTo(email);
        mailMessage.setSubject("Notification");
        mailMessage.setText(message);
        mailSender.send(mailMessage);
        return true;
    }
}
