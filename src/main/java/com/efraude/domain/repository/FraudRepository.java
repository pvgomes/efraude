package com.efraude.domain.repository;

import com.efraude.domain.model.Fraud;
import com.efraude.domain.model.enums.FraudStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface FraudRepository extends JpaRepository<Fraud, UUID> {

    Page<Fraud> findByStatus(FraudStatus status, Pageable pageable);

    @Query("SELECT f FROM Fraud f WHERE f.status = :status AND " +
           "(LOWER(f.title) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(f.description) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Fraud> searchByStatusAndKeyword(@Param("status") FraudStatus status,
                                          @Param("search") String search,
                                          Pageable pageable);

    @Query("SELECT f FROM Fraud f WHERE f.status = :status AND f.country = :country")
    Page<Fraud> findByStatusAndCountry(@Param("status") FraudStatus status,
                                        @Param("country") String country,
                                        Pageable pageable);

    @Query("SELECT f FROM Fraud f WHERE f.status = :status AND f.country = :country AND " +
           "(LOWER(f.title) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(f.description) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Fraud> searchByStatusAndCountryAndKeyword(@Param("status") FraudStatus status,
                                                     @Param("country") String country,
                                                     @Param("search") String search,
                                                     Pageable pageable);
}
