package com.chidimma.image_verification_system.repository;

import com.chidimma.image_verification_system.model.EmailToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailTokenRepository extends JpaRepository<EmailToken, Long> {

    Optional<EmailToken> findTopByEmailAndUsedFalseOrderByCreatedAtDesc (String email);

}
