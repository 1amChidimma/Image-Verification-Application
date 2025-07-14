package com.chidimma.image_verification_system.controller;

import com.chidimma.image_verification_system.dto.*;
import com.chidimma.image_verification_system.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;


@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;


    @PostMapping("/signup1")
    public ResponseEntity<UserResponse> signup1(
            @RequestBody SignupRequest signupRequest) {
        UserResponse response = authService.signupUserOnly(signupRequest);
        return ResponseEntity.ok(response);
    }

    /*@PostMapping("/signup2")
    public ResponseEntity<?> signup2(
            @RequestParam("email") String email,
            @RequestParam("faceImage") MultipartFile faceImage) {
        try {
            UserResponse response = authService.saveUserFaceImage(email, faceImage);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }*/

    @PostMapping("/kyc")
    public ResponseEntity<?> uploadKycImage(@RequestBody KycRequest request) {
        authService.submitKyc(request);
        return ResponseEntity.ok("KYC image saved.");
    }


    @PostMapping("/login1")
    public ResponseEntity<DashboardResponse> login1(
            @RequestBody LoginRequest loginRequest){

        try {
            DashboardResponse response = authService.login1(loginRequest);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @PostMapping("/login2")
    public ResponseEntity<UserResponse> login2(@RequestBody FaceLoginRequest faceLoginRequest) {

        try {
            UserResponse response = authService.login2(faceLoginRequest);
            return ResponseEntity.ok(response);
        } catch (Exception e) {e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null); // or return an error message if using a wrapper
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