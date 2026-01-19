package com.efraude.domain.service;

import com.efraude.domain.model.Fraud;
import com.efraude.domain.model.User;
import com.efraude.domain.model.enums.FraudStatus;
import com.efraude.domain.repository.FraudRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.Normalizer;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FraudService {

    private final FraudRepository fraudRepository;

    @Transactional(readOnly = true)
    public Optional<Fraud> findById(UUID id) {
        return fraudRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Page<Fraud> findActive(Pageable pageable) {
        return fraudRepository.findByStatus(FraudStatus.ACTIVE, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Fraud> search(String query, String country, Pageable pageable) {
        FraudStatus status = FraudStatus.ACTIVE;

        if (query == null || query.trim().isEmpty()) {
            if (country == null || country.trim().isEmpty()) {
                return fraudRepository.findByStatus(status, pageable);
            } else {
                return fraudRepository.findByStatusAndCountry(status, country, pageable);
            }
        } else {
            if (country == null || country.trim().isEmpty()) {
                return fraudRepository.searchByStatusAndKeyword(status, query.trim(), pageable);
            } else {
                return fraudRepository.searchByStatusAndCountryAndKeyword(status, country, query.trim(), pageable);
            }
        }
    }

    @Transactional
    public Fraud createFraud(String title, String description, String caution, String url, String country, User createdBy) {
        String slug = generateSlug(title);

        Fraud fraud = Fraud.builder()
                .title(title)
                .slug(slug)
                .description(description)
                .caution(caution)
                .url(url)
                .country(country != null && !country.trim().isEmpty() ? country : "GLOBAL")
                .status(FraudStatus.ACTIVE)
                .createdBy(createdBy)
                .build();

        return fraudRepository.save(fraud);
    }

    @Transactional
    public Fraud archiveFraud(UUID fraudId) {
        Fraud fraud = fraudRepository.findById(fraudId)
                .orElseThrow(() -> new IllegalArgumentException("Fraud not found"));

        fraud.setStatus(FraudStatus.ARCHIVED);
        return fraudRepository.save(fraud);
    }

    @Transactional(readOnly = true)
    public Page<Fraud> findAll(Pageable pageable) {
        return fraudRepository.findAll(pageable);
    }

    private String generateSlug(String title) {
        String normalized = Normalizer.normalize(title, Normalizer.Form.NFD);
        String slug = normalized.replaceAll("\\p{M}", "");
        slug = slug.toLowerCase()
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-")
                .replaceAll("-+", "-")
                .replaceAll("^-|-$", "");

        if (slug.length() > 100) {
            slug = slug.substring(0, 100);
        }

        return slug;
    }
}
