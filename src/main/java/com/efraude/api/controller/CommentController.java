package com.efraude.api.controller;

import com.efraude.api.dto.ApiResponse;
import com.efraude.api.dto.CommentRequest;
import com.efraude.domain.model.Comment;
import com.efraude.domain.model.Fraud;
import com.efraude.domain.model.User;
import com.efraude.domain.service.CommentService;
import com.efraude.domain.service.FraudService;
import com.efraude.domain.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/frauds/{fraudId}/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final FraudService fraudService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<Comment>>> getComments(
            @PathVariable UUID fraudId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Fraud fraud = fraudService.findById(fraudId)
                .orElseThrow(() -> new IllegalArgumentException("Fraud not found"));

        Pageable pageable = PageRequest.of(page, size);
        Page<Comment> comments = commentService.findByFraud(fraud, pageable);

        return ResponseEntity.ok(ApiResponse.success(comments));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Comment>> createComment(
            @PathVariable UUID fraudId,
            @Valid @RequestBody CommentRequest request,
            Authentication authentication
    ) {
        Fraud fraud = fraudService.findById(fraudId)
                .orElseThrow(() -> new IllegalArgumentException("Fraud not found"));

        User user = userService.findByEmail(authentication.getName())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Comment comment = commentService.createComment(fraud, user, request.getMessage());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Comment posted successfully", comment));
    }
}
