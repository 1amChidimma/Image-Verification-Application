package com.chidimma.image_verification_system.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**Carries all data needed to register a new user, including the uploaded face image*/
@Getter
@Setter
@NoArgsConstructor
public class LoginRequest {
    private String userName;
    private String password;
}
