package com.chidimma.image_verification_system.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VerifyEmailOtpResponse {

    private boolean success;
    private String message;

}
