package com.efraude.api.controller;

import com.efraude.api.dto.ApiResponse;
import com.efraude.api.dto.SignupRequest;
import com.efraude.domain.model.User;
import com.efraude.domain.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<User>> signup(@Valid @RequestBody SignupRequest request) {
        try {
            User user = authService.registerUser(request.getName(), request.getEmail(), request.getPassword());
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("User registered successfully. Please check your email to confirm.", user));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/confirm")
    public ResponseEntity<ApiResponse<Void>> confirmEmail(@RequestParam String token) {
        boolean confirmed = authService.confirmEmail(token);
        if (confirmed) {
            return ResponseEntity.ok(ApiResponse.success("Email confirmed successfully. You can now login.", null));
        } else {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Invalid or expired token"));
        }
    }

    @PostMapping("/resend-confirmation")
    public ResponseEntity<ApiResponse<Void>> resendConfirmation(@RequestParam String email) {
        try {
            authService.resendConfirmationEmail(email);
            return ResponseEntity.ok(ApiResponse.success("Confirmation email resent successfully.", null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
}
