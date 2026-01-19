package com.efraude.web.controller;

import com.efraude.domain.model.Comment;
import com.efraude.domain.model.Fraud;
import com.efraude.domain.model.User;
import com.efraude.domain.model.Vote;
import com.efraude.domain.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/web/frauds")
@RequiredArgsConstructor
public class FraudWebController {

    private final FraudService fraudService;
    private final FraudScoringService scoringService;
    private final VoteService voteService;
    private final CommentService commentService;
    private final UserService userService;

    @GetMapping
    public String list(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String country,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            Model model
    ) {
        Sort.Direction direction = Sort.Direction.DESC;
        Page<Fraud> frauds = fraudService.search(q, country, PageRequest.of(page, 20, Sort.by(direction, sortBy)));

        model.addAttribute("frauds", frauds);
        model.addAttribute("query", q);
        model.addAttribute("country", country);
        model.addAttribute("currentPage", page);

        return "fraud/list";
    }

    @GetMapping("/{id}-{slug}")
    public String detail(
            @PathVariable UUID id,
            @PathVariable String slug,
            @RequestParam(defaultValue = "0") int commentsPage,
            Authentication authentication,
            Model model
    ) {
        Fraud fraud = fraudService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Fraud not found"));

        // Calculate score
        FraudScoringService.FraudScore score = scoringService.calculateScore(fraud);

        // Get user's vote if authenticated
        Vote userVote = null;
        if (authentication != null && authentication.isAuthenticated()) {
            User user = userService.findByEmail(authentication.getName()).orElse(null);
            if (user != null) {
                userVote = voteService.findUserVote(fraud, user).orElse(null);
            }
        }

        // Get comments
        Page<Comment> comments = commentService.findByFraud(fraud, PageRequest.of(commentsPage, 20));

        model.addAttribute("fraud", fraud);
        model.addAttribute("score", score);
        model.addAttribute("userVote", userVote);
        model.addAttribute("comments", comments);
        model.addAttribute("commentsPage", commentsPage);

        return "fraud/detail";
    }

    @GetMapping("/create")
    public String createForm() {
        return "fraud/create";
    }

    @PostMapping("/create")
    public String create(
            @RequestParam String title,
            @RequestParam String description,
            @RequestParam(required = false) String caution,
            @RequestParam(required = false) String url,
            @RequestParam(required = false) String country,
            Authentication authentication
    ) {
        User user = userService.findByEmail(authentication.getName())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Fraud fraud = fraudService.createFraud(title, description, caution, url, country, user);

        return "redirect:/web/frauds/" + fraud.getId() + "-" + fraud.getSlug();
    }
}
