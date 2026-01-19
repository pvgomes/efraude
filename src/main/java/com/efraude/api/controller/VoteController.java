package com.efraude.api.controller;

import com.efraude.api.dto.ApiResponse;
import com.efraude.api.dto.VoteRequest;
import com.efraude.domain.model.Fraud;
import com.efraude.domain.model.User;
import com.efraude.domain.model.Vote;
import com.efraude.domain.service.FraudService;
import com.efraude.domain.service.UserService;
import com.efraude.domain.service.VoteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/frauds/{fraudId}/votes")
@RequiredArgsConstructor
public class VoteController {

    private final VoteService voteService;
    private final FraudService fraudService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<ApiResponse<Vote>> upsertVote(
            @PathVariable UUID fraudId,
            @Valid @RequestBody VoteRequest request,
            Authentication authentication
    ) {
        Fraud fraud = fraudService.findById(fraudId)
                .orElseThrow(() -> new IllegalArgumentException("Fraud not found"));

        User user = userService.findByEmail(authentication.getName())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Vote vote = voteService.upsertVote(fraud, user, request.getType());

        return ResponseEntity.ok(ApiResponse.success("Vote recorded successfully", vote));
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> removeVote(
            @PathVariable UUID fraudId,
            Authentication authentication
    ) {
        Fraud fraud = fraudService.findById(fraudId)
                .orElseThrow(() -> new IllegalArgumentException("Fraud not found"));

        User user = userService.findByEmail(authentication.getName())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        voteService.removeVote(fraud, user);

        return ResponseEntity.ok(ApiResponse.success("Vote removed successfully", null));
    }
}
