package com.chidimma.image_verification_system.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VerifyWhatsAppOtpRequest {

    private String phoneNumber;
    private String otp;
}
