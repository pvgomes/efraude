package com.efraude.api.controller;

import com.efraude.api.dto.ApiResponse;
import com.efraude.api.dto.CreateFraudRequest;
import com.efraude.domain.model.Fraud;
import com.efraude.domain.model.User;
import com.efraude.domain.service.FraudService;
import com.efraude.domain.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/frauds")
@RequiredArgsConstructor
public class FraudController {

    private final FraudService fraudService;
    private final UserService userService;

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<Fraud>>> search(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String country,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir
    ) {
        Sort.Direction direction = sortDir.equalsIgnoreCase("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Page<Fraud> frauds = fraudService.search(q, country, pageable);
        return ResponseEntity.ok(ApiResponse.success(frauds));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Fraud>> getById(@PathVariable UUID id) {
        return fraudService.findById(id)
                .map(fraud -> ResponseEntity.ok(ApiResponse.success(fraud)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Fraud>> create(
            @Valid @RequestBody CreateFraudRequest request,
            Authentication authentication
    ) {
        User user = userService.findByEmail(authentication.getName())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Fraud fraud = fraudService.createFraud(
                request.getTitle(),
                request.getDescription(),
                request.getCaution(),
                request.getUrl(),
                request.getCountry(),
                user
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Fraud reported successfully", fraud));
    }
}
