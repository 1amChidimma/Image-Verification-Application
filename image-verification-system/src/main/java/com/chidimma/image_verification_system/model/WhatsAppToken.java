package com.chidimma.image_verification_system.model;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "whatsapp-token")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WhatsAppToken {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    private String phoneNumber;

    private String token;

    private boolean used;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime expiresAt;

}
