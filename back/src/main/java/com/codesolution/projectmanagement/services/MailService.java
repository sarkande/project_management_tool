package com.codesolution.projectmanagement.services;

public interface MailService {
    boolean sendNotification(String email, String message);
}
