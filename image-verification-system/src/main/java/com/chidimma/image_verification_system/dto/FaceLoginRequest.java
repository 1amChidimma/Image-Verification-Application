package com.chidimma.image_verification_system.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FaceLoginRequest {
    private String name;
    private String imageUrl;
}
