package com.chidimma.image_verification_system.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "kyc_records")
public class KycRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imageUrl;

    private String status = "PENDING"; // e.g., PENDING, APPROVED, REJECTED

    private LocalDateTime submittedAt = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public KycRecord(String imageUrl, User user) {
        this.imageUrl = imageUrl;
        this.user = user;
        this.status = "PENDING";
        this.submittedAt = LocalDateTime.now();
    }



}
