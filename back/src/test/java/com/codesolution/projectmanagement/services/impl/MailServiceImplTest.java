package com.codesolution.projectmanagement.services.impl;

import com.codesolution.projectmanagement.services.MailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;

class MailServiceImplTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private MailServiceImpl mailService;

    @Captor
    private ArgumentCaptor<SimpleMailMessage> mailMessageCaptor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSendNotification() {
        // Arrange
        String email = "test@example.com";
        String message = "This is a test notification";

        // Act
        boolean result = mailService.sendNotification(email, message);

        // Assert
        assertTrue(result);
        verify(mailSender).send(mailMessageCaptor.capture());
        SimpleMailMessage capturedMessage = mailMessageCaptor.getValue();
        assertEquals("noreply@demomailtrap.co", capturedMessage.getFrom());
        assertEquals(email, capturedMessage.getTo()[0]);
        assertEquals("Notification", capturedMessage.getSubject());
        assertEquals(message, capturedMessage.getText());
    }
}
