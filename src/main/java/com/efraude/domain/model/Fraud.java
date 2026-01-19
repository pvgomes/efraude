package com.efraude.domain.model;

import com.efraude.domain.model.enums.FraudStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "frauds", indexes = {
    @Index(name = "idx_frauds_created_at", columnList = "created_at"),
    @Index(name = "idx_frauds_status_created_at", columnList = "status, created_at")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Fraud {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 300)
    private String slug;

    @Column(nullable = false, length = 300)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String caution;

    @Column(length = 2048)
    private String url;

    @Column(nullable = false, length = 64)
    @Builder.Default
    private String country = "GLOBAL";

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private FraudStatus status = FraudStatus.ACTIVE;

    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;
}
