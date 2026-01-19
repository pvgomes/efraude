package com.efraude.domain.service;

import com.efraude.domain.model.EmailVerificationToken;
import com.efraude.domain.model.User;
import com.efraude.domain.model.enums.AuthProvider;
import com.efraude.domain.model.enums.UserRole;
import com.efraude.domain.repository.EmailVerificationTokenRepository;
import com.efraude.domain.repository.UserRepository;
import com.efraude.messaging.event.UserRegisteredEvent;
import com.efraude.messaging.producer.EventProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final EmailVerificationTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final EventProducer eventProducer;

    @Transactional
    public User registerUser(String name, String email, String password) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already registered");
        }

        User user = User.builder()
                .name(name)
                .email(email)
                .passwordHash(passwordEncoder.encode(password))
                .authProvider(AuthProvider.LOCAL)
                .role(UserRole.USER)
                .emailVerified(false)
                .build();

        user = userRepository.save(user);

        // Create verification token
        String token = UUID.randomUUID().toString();
        EmailVerificationToken verificationToken = EmailVerificationToken.builder()
                .user(user)
                .token(token)
                .expiresAt(Instant.now().plus(24, ChronoUnit.HOURS))
                .build();

        tokenRepository.save(verificationToken);

        // Publish event for email sending
        UserRegisteredEvent event = UserRegisteredEvent.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .confirmationToken(token)
                .build();

        eventProducer.publishUserRegistered(event);

        return user;
    }

    @Transactional
    public boolean confirmEmail(String token) {
        Optional<EmailVerificationToken> tokenOpt = tokenRepository.findByToken(token);

        if (tokenOpt.isEmpty()) {
            return false;
        }

        EmailVerificationToken verificationToken = tokenOpt.get();

        if (verificationToken.getExpiresAt().isBefore(Instant.now())) {
            return false;
        }

        User user = verificationToken.getUser();
        user.setEmailVerified(true);
        userRepository.save(user);

        tokenRepository.delete(verificationToken);

        return true;
    }

    @Transactional
    public void resendConfirmationEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (user.getEmailVerified()) {
            throw new IllegalArgumentException("Email already verified");
        }

        // Delete old token if exists
        tokenRepository.findByUser(user).ifPresent(tokenRepository::delete);

        // Create new token
        String token = UUID.randomUUID().toString();
        EmailVerificationToken verificationToken = EmailVerificationToken.builder()
                .user(user)
                .token(token)
                .expiresAt(Instant.now().plus(24, ChronoUnit.HOURS))
                .build();

        tokenRepository.save(verificationToken);

        // Publish event
        UserRegisteredEvent event = UserRegisteredEvent.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .confirmationToken(token)
                .build();

        eventProducer.publishUserRegistered(event);
    }

    @Transactional(readOnly = true)
    public User getOrCreateOAuthUser(String email, String name, String imageUrl) {
        Optional<User> existingUser = userRepository.findByEmail(email);

        if (existingUser.isPresent()) {
            return existingUser.get();
        }

        // Create new user from OAuth
        User user = User.builder()
                .name(name)
                .email(email)
                .authProvider(AuthProvider.GOOGLE)
                .role(UserRole.USER)
                .emailVerified(true) // OAuth emails are pre-verified
                .imageUrl(imageUrl)
                .build();

        return userRepository.save(user);
    }
}
