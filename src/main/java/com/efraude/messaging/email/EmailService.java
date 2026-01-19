package com.efraude.messaging.email;

public interface EmailService {
    void sendEmail(String to, String subject, String htmlBody);
}
