package com.chidimma.image_verification_system.controller;

import com.chidimma.image_verification_system.dto.*;
import com.chidimma.image_verification_system.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<UserResponse> signup(
            @ModelAttribute SignupRequest signupRequest,
            @RequestParam("faceImage")MultipartFile faceImage){
        try{
            UserResponse response = authService.signup(signupRequest, faceImage);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/login1")
    public boolean login1(
            @RequestBody LoginRequest loginRequest){

        try{
            boolean response = authService.login1(loginRequest);
            return response;
        }catch (Exception e){
            throw new RuntimeException(e);
        }

    }

    @PostMapping("/login2")
    public ResponseEntity<UserResponse> login2(
            @ModelAttribute LoginRequest loginRequest,
            @RequestParam("faceImage") MultipartFile faceImage) {
        System.out.println("Username: " + loginRequest.getUserName());
        System.out.println("Image file: " + faceImage.getOriginalFilename());

        try {
            UserResponse response = authService.login2(loginRequest, faceImage);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            throw new RuntimeException(e);
           }
    }

    @PostMapping("/generate-email-otp")
    public ResponseEntity<GenerateEmailOtpResponse> generateEmailOtp(
            @RequestBody GenerateEmailOtpRequest generateEmailOtpRequest
            ){
        GenerateEmailOtpResponse response = authService.generateEmailOtp(generateEmailOtpRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/verify-email-otp")
    public ResponseEntity<VerifyEmailOtpResponse> verifyEmailOtp(
            @RequestBody VerifyEmailOtpRequest verifyEmailOtpRequest
            ){
        VerifyEmailOtpResponse response = authService.verifyEmailOtp(verifyEmailOtpRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/generate-whatsApp-otp")
    public ResponseEntity<GenerateWhatsAppOtpResponse> generateWhatsAppOtp(
            @RequestBody GenerateWhatsAppOtpRequest generateWhatsAppOtpRequest
            ){
        GenerateWhatsAppOtpResponse response = authService.generateWhatsAppOtp(generateWhatsAppOtpRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/verify-whatsApp-otp")
    public ResponseEntity<VerifyWhatsAppOtpResponse> verifyWhatsAppOtp(
            @RequestBody VerifyWhatsAppOtpRequest verifyWhatsAppOtpRequest
            ){
        VerifyWhatsAppOtpResponse response = authService.verifyWhatsAppOtp(verifyWhatsAppOtpRequest);
        return ResponseEntity.ok(response);
    }
}