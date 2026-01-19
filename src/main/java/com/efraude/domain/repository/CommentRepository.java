package com.efraude.domain.repository;

import com.efraude.domain.model.Comment;
import com.efraude.domain.model.Fraud;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CommentRepository extends JpaRepository<Comment, UUID> {

    Page<Comment> findByFraudOrderByCreatedAtDesc(Fraud fraud, Pageable pageable);

    long countByFraud(Fraud fraud);
}
