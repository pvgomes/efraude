package com.efraude.domain.service;

import com.efraude.domain.model.Fraud;
import com.efraude.domain.model.User;
import com.efraude.domain.model.Vote;
import com.efraude.domain.model.enums.VoteType;
import com.efraude.domain.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VoteService {

    private final VoteRepository voteRepository;

    @Transactional(readOnly = true)
    public Optional<Vote> findUserVote(Fraud fraud, User user) {
        return voteRepository.findByFraudAndUser(fraud, user);
    }

    @Transactional
    public Vote upsertVote(Fraud fraud, User user, VoteType voteType) {
        Optional<Vote> existingVote = voteRepository.findByFraudAndUser(fraud, user);

        if (existingVote.isPresent()) {
            Vote vote = existingVote.get();
            vote.setType(voteType);
            return voteRepository.save(vote);
        } else {
            Vote vote = Vote.builder()
                    .fraud(fraud)
                    .user(user)
                    .type(voteType)
                    .build();
            return voteRepository.save(vote);
        }
    }

    @Transactional
    public void removeVote(Fraud fraud, User user) {
        voteRepository.findByFraudAndUser(fraud, user)
                .ifPresent(voteRepository::delete);
    }
}
