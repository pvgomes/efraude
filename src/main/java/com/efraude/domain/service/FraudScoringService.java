package com.efraude.domain.service;

import com.efraude.domain.model.Fraud;
import com.efraude.domain.model.enums.VoteType;
import com.efraude.domain.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FraudScoringService {

    private final VoteRepository voteRepository;

    @Value("${efraude.fraud.score-threshold:10}")
    private int scoreThreshold;

    @Value("${efraude.fraud.min-votes-threshold:5}")
    private int minVotesThreshold;

    @Transactional(readOnly = true)
    public FraudScore calculateScore(Fraud fraud) {
        long upvotes = voteRepository.countByFraudAndType(fraud, VoteType.UP);
        long downvotes = voteRepository.countByFraudAndType(fraud, VoteType.DOWN);
        long totalVotes = upvotes + downvotes;
        long score = upvotes - downvotes;

        boolean likelyFraud = score >= scoreThreshold && totalVotes >= minVotesThreshold;

        return FraudScore.builder()
                .score(score)
                .upvotes(upvotes)
                .downvotes(downvotes)
                .totalVotes(totalVotes)
                .likelyFraud(likelyFraud)
                .build();
    }

    @lombok.Data
    @lombok.Builder
    public static class FraudScore {
        private long score;
        private long upvotes;
        private long downvotes;
        private long totalVotes;
        private boolean likelyFraud;
    }
}
