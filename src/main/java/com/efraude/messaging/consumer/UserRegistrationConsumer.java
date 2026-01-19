package com.efraude.messaging.consumer;

import com.efraude.messaging.email.EmailService;
import com.efraude.messaging.event.UserRegisteredEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserRegistrationConsumer {

    private final EmailService emailService;

    @Value("${efraude.base-url}")
    private String baseUrl;

    @KafkaListener(topics = "user.registered", groupId = "efraude-email-service")
    public void handleUserRegistered(UserRegisteredEvent event) {
        log.info("Received user registered event for user: {}", event.getEmail());

        try {
            String confirmationUrl = baseUrl + "/web/auth/confirm?token=" + event.getConfirmationToken();

            String subject = "Welcome to efraude - Confirm Your Email";
            String body = buildEmailBody(event.getName(), confirmationUrl);

            emailService.sendEmail(event.getEmail(), subject, body);

            log.info("Confirmation email sent to: {}", event.getEmail());
        } catch (Exception e) {
            log.error("Failed to send confirmation email to: {}", event.getEmail(), e);
            // In production, you might want to retry or send to a dead letter queue
        }
    }

    private String buildEmailBody(String name, String confirmationUrl) {
        return String.format("""
                <html>
                <body style="font-family: Arial, sans-serif; line-height: 1.6; color: #333;">
                    <div style="max-width: 600px; margin: 0 auto; padding: 20px;">
                        <h1 style="color: #FF6B35;">Welcome to efraude!</h1>
                        <p>Hi %s,</p>
                        <p>Thank you for registering at efraude. Please confirm your email address by clicking the button below:</p>
                        <div style="text-align: center; margin: 30px 0;">
                            <a href="%s" style="background-color: #FF6B35; color: white; padding: 12px 30px; text-decoration: none; border-radius: 5px; display: inline-block;">
                                Confirm Email
                            </a>
                        </div>
                        <p>Or copy and paste this link into your browser:</p>
                        <p style="word-break: break-all; color: #666;"><a href="%s">%s</a></p>
                        <p>This link will expire in 24 hours.</p>
                        <hr style="border: none; border-top: 1px solid #ddd; margin: 30px 0;">
                        <p style="color: #999; font-size: 12px;">
                            If you didn't create an account on efraude, you can safely ignore this email.
                        </p>
                    </div>
                </body>
                </html>
                """, name, confirmationUrl, confirmationUrl, confirmationUrl);
    }
}
