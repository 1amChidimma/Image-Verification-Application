package com.chidimma.image_verification_system.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DashboardResponse {

    private String name;
    private String email;
    private String phone;
    private LocalDate dob;
    private String imageUrl;


}
