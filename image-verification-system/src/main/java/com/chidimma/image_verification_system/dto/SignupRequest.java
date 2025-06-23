package com.chidimma.image_verification_system.dto;

import org.springframework.web.multipart.MultipartFile;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Carries all data needed to register a new user, including the uploaded face image*/
@Getter
@Setter
@NoArgsConstructor
public class SignupRequest {
    private String userName;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private MultipartFile faceImage;
}
