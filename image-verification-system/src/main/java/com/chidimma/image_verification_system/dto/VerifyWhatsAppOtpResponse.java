package com.chidimma.image_verification_system.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VerifyWhatsAppOtpResponse {

    private boolean success;
    private String message;
}
