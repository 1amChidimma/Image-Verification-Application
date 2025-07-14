package com.chidimma.image_verification_system.repository;

import com.chidimma.image_verification_system.model.KycRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.chidimma.image_verification_system.model.User;

import java.util.Optional;

@Repository
public interface KycRepository extends JpaRepository<KycRecord, Long> {
    Optional<KycRecord> findByUser(User user);
    Optional<KycRecord> findTopByUserOrderBySubmittedAtDesc(User user);
}
