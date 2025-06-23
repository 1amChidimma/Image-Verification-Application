package com.chidimma.image_verification_system.repository;

import com.chidimma.image_verification_system.model.WhatsAppToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WhatsAppTokenRepository extends JpaRepository<WhatsAppToken, Long> {
    Optional<WhatsAppToken> findTopByPhoneNumberOrderByCreatedAtDesc(String phoneNumber);
}
