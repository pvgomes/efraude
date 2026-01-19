package com.efraude.domain.repository;

import com.efraude.domain.model.Fraud;
import com.efraude.domain.model.User;
import com.efraude.domain.model.Vote;
import com.efraude.domain.model.enums.VoteType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface VoteRepository extends JpaRepository<Vote, UUID> {

    Optional<Vote> findByFraudAndUser(Fraud fraud, User user);

    long countByFraudAndType(Fraud fraud, VoteType type);

    @Query("SELECT COUNT(v) FROM Vote v WHERE v.fraud = :fraud AND v.type = 'UP'")
    long countUpvotesByFraud(@Param("fraud") Fraud fraud);

    @Query("SELECT COUNT(v) FROM Vote v WHERE v.fraud = :fraud AND v.type = 'DOWN'")
    long countDownvotesByFraud(@Param("fraud") Fraud fraud);

    @Query("SELECT COUNT(v) FROM Vote v WHERE v.fraud = :fraud")
    long countTotalVotesByFraud(@Param("fraud") Fraud fraud);
}
