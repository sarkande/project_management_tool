package com.codesolution.projectmanagement.services.impl;

import com.codesolution.projectmanagement.services.MailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class MailServiceImpl implements MailService {
        @Autowired
        private JavaMailSender mailSender;

        public boolean sendNotification(String email, String message) {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom("noreply@demomailtrap.co");
            mailMessage.setTo(email);
            mailMessage.setSubject("Notification");
            mailMessage.setText(message);
            mailSender.send(mailMessage);
            return true;
        }
}
