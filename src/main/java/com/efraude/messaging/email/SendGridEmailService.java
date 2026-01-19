package com.efraude.messaging.email;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
public class SendGridEmailService implements EmailService {

    @Value("${efraude.sendgrid.api-key:}")
    private String sendGridApiKey;

    @Value("${efraude.sendgrid.from-email}")
    private String fromEmail;

    @Override
    public void sendEmail(String to, String subject, String htmlBody) {
        // If no API key configured, log the email instead of failing
        if (sendGridApiKey == null || sendGridApiKey.trim().isEmpty()) {
            log.warn("SendGrid API key not configured. Email will be logged instead of sent.");
            log.info("=== EMAIL (would be sent to: {}) ===", to);
            log.info("Subject: {}", subject);
            log.info("Body: {}", htmlBody);
            log.info("=== END EMAIL ===");
            return;
        }

        try {
            Email from = new Email(fromEmail);
            Email toEmail = new Email(to);
            Content content = new Content("text/html", htmlBody);
            Mail mail = new Mail(from, subject, toEmail, content);

            SendGrid sg = new SendGrid(sendGridApiKey);
            Request request = new Request();
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());

            Response response = sg.api(request);

            if (response.getStatusCode() >= 200 && response.getStatusCode() < 300) {
                log.info("Email sent successfully to: {}", to);
            } else {
                log.error("Failed to send email. Status: {}, Body: {}", response.getStatusCode(), response.getBody());
            }
        } catch (IOException e) {
            log.error("Error sending email to: {}", to, e);
            throw new RuntimeException("Failed to send email", e);
        }
    }
}
