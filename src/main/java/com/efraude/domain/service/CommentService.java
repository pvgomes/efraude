package com.efraude.domain.service;

import com.efraude.domain.model.Comment;
import com.efraude.domain.model.Fraud;
import com.efraude.domain.model.User;
import com.efraude.domain.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    @Transactional(readOnly = true)
    public Page<Comment> findByFraud(Fraud fraud, Pageable pageable) {
        return commentRepository.findByFraudOrderByCreatedAtDesc(fraud, pageable);
    }

    @Transactional
    public Comment createComment(Fraud fraud, User user, String message) {
        Comment comment = Comment.builder()
                .fraud(fraud)
                .user(user)
                .message(message)
                .build();

        return commentRepository.save(comment);
    }

    @Transactional(readOnly = true)
    public long countByFraud(Fraud fraud) {
        return commentRepository.countByFraud(fraud);
    }
}
