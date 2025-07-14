package com.chidimma.image_verification_system.service;

import com.chidimma.image_verification_system.dto.*;
import com.chidimma.image_verification_system.model.EmailToken;
import com.chidimma.image_verification_system.model.KycRecord;
import com.chidimma.image_verification_system.model.WhatsAppToken;
import com.chidimma.image_verification_system.model.User;
import com.chidimma.image_verification_system.repository.EmailTokenRepository;
import com.chidimma.image_verification_system.repository.KycRepository;
import com.chidimma.image_verification_system.repository.WhatsAppTokenRepository;
import com.chidimma.image_verification_system.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;


@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final KycRepository kycRepository;
    private final EmailTokenRepository emailTokenRepository;
    private final WhatsAppTokenRepository whatsAppTokenRepository;
    private final FaceVerificationService faceVerificationService;
    private final EmailSenderService emailSenderService;
    private final WhatsAppSenderService whatsAppSenderService;
    private final PasswordEncoder passwordEncoder;

    @Value("${file.upload-dir}")
    private String uploadDir;  // Path to save uploaded images



    // Method to save the face image and return the image path (or filename)
    /*private String saveImage(MultipartFile faceImage) throws IOException {
        // Generate a unique filename
        String originalFilename = faceImage.getOriginalFilename(); //Extracts the original filename.
        String fileName = UUID.randomUUID().toString() + "-" + originalFilename; //

        // Define the path to save the image file
        Path path = Paths.get(uploadDir, fileName);

        // Ensure the directories exist
        Files.createDirectories(path.getParent());

        // Save the file to the server
        Files.write(path, faceImage.getBytes());

        return fileName;  // Return the filename to store in the database
    }*/


    //SIGN-UP
    public UserResponse signupUserOnly(SignupRequest signupRequest) {
        String encodedPassword = passwordEncoder.encode(signupRequest.getPassword());

        User user = new User();
        user.setUserName(signupRequest.getName());
        user.setEmail(signupRequest.getEmail());
        user.setDob(signupRequest.getDob());
        user.setPhone(signupRequest.getPhone());
        user.setPassword(encodedPassword);


        User savedUser = userRepository.save(user);


        return new UserResponse(
                savedUser.getUserName(), // maps to `name` in response
                savedUser.getEmail(),
                savedUser.getPhone(),
                savedUser.getDob()
        );
    }

    /*public UserResponse saveUserFaceImage(String email, MultipartFile faceImage) throws IOException {
        User user = userRepository.findByUserName(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String imagePath = saveImage(faceImage);
        user.setImagePath(imagePath);

        User updatedUser = userRepository.save(user);

        return new UserResponse(
                updatedUser.getUserName(),
                updatedUser.getEmail(),
                updatedUser.getPhone(),
                updatedUser.getDob()
        );
    }*/

    public void submitKyc(KycRequest kycRequest){
        User user = userRepository.findByUserName(kycRequest.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        KycRecord record = new KycRecord(kycRequest.getImageUrl(), user);
        kycRepository.save(record);
    }


    public DashboardResponse login1(LoginRequest loginRequest) throws IOException {

        User user = userRepository.findByUserName(loginRequest.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean passwordMatch = passwordEncoder.matches(
                loginRequest.getPassword(),   // raw password from request
                user.getPassword()            // encoded password from DB
        );

        Optional<KycRecord> kycRecord = kycRepository.findTopByUserOrderBySubmittedAtDesc(user);
        String imageUrl = kycRecord.map(KycRecord::getImageUrl).orElse(null);

        if (passwordMatch) {
            return new DashboardResponse(
                    user.getUserName(),
                    user.getEmail(),
                    user.getPhone(),
                    user.getDob(),
                    imageUrl
            );
        } else throw new RuntimeException("Invalid password");
    }

    /*public UserResponse login2(LoginRequest loginRequest, MultipartFile faceImage) throws IOException {
        //log username and file received
        System.out.println("Received username = " + loginRequest.getName());
        System.out.println("Received face image = " + faceImage.getOriginalFilename());

        User user = userRepository.findByUserName(loginRequest.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        System.out.println("Stored image path from database = " + user.getImagePath());

        Path storedImagePath = Paths.get(uploadDir, user.getImagePath());
        File storedImage = storedImagePath.toFile();

        if (!storedImage.exists()){
            System.out.println("Stored image file not found at path: " + storedImage.getAbsolutePath());
            throw new RuntimeException("Stored face image not found");
        }

        System.out.println("Found stored image file = " + storedImage.getAbsolutePath());

        System.out.println("Sending images to python service...");
        boolean isFaceMatch = faceVerificationService.verifyFaceWithPython(user.getImagePath(), faceImage);

        if (!isFaceMatch){
            System.out.println("Face verification failed.");
        }

        System.out.println("Successful.");

        return new UserResponse(
                user.getUserName(),
                user.getEmail(),
                user.getPhone(),
                user.getDob()
        );
    }*/

    public UserResponse login2(FaceLoginRequest faceLoginRequest) {
        User user = userRepository.findByUserName(faceLoginRequest.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        KycRecord kycRecord = kycRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("KYC record not found"));

        String storedFaceUrl = kycRecord.getImageUrl();
        String loginFaceUrl = faceLoginRequest.getImageUrl();

        boolean verified = faceVerificationService.verifyFaceUrls(storedFaceUrl, loginFaceUrl);
        if (!verified) throw new RuntimeException("Face Verification Failed");

        return new UserResponse(
                user.getUserName(),
                user.getEmail(),
                user.getPhone(),
                user.getDob()
        );


    }

    public GenerateEmailOtpResponse generateEmailOtp(GenerateEmailOtpRequest generateEmailOtpRequest){

        String otp = String.format("%06d", new Random().nextInt(999999));

        String hashedOtp = passwordEncoder.encode(otp);

        User user = userRepository.findByEmail(generateEmailOtpRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));


        EmailToken token = new EmailToken();
        token.setUser(user);
        token.setToken(hashedOtp);
        token.setUsed(false);
        token.setCreatedAt(LocalDateTime.now());
        token.setUpdatedAt(token.getCreatedAt());
        token.setExpiresAt(LocalDateTime.now().plusHours(1));
        emailTokenRepository.save(token);

        //send email with email sender service? Do later!!
        emailSenderService.sendOtpEmail(generateEmailOtpRequest.getEmail(), otp);

        System.out.println("OTP: " + otp);

        return new GenerateEmailOtpResponse(true, "OTP successfully sent to email.");
    }

    public VerifyEmailOtpResponse verifyEmailOtp(VerifyEmailOtpRequest verifyEmailOtpRequest){

        Optional<EmailToken> optionalEmailToken = emailTokenRepository
                .findTopByUserEmailAndUsedFalseOrderByCreatedAtDesc(verifyEmailOtpRequest.getEmail());

        if(optionalEmailToken.isEmpty()){
            return new VerifyEmailOtpResponse(false, "No OTP found or already used.");
        }

        EmailToken token = optionalEmailToken.get();

        if (token.getExpiresAt(). isBefore(LocalDateTime.now())){
            return new VerifyEmailOtpResponse(false, "OTP has expired.");
        }

        boolean isMatch = passwordEncoder.matches(verifyEmailOtpRequest.getOtp(), token.getToken());

        if(!isMatch){
            return new VerifyEmailOtpResponse(false,"Invalid OTP");
        }

        token.setUsed(true);
        token.setUpdatedAt(LocalDateTime.now());
        emailTokenRepository.save(token);

        return new VerifyEmailOtpResponse(true, "OTP verified successfully.");
    }


    public GenerateWhatsAppOtpResponse generateWhatsAppOtp(GenerateWhatsAppOtpRequest generateWhatsAppOtpRequest){
        String otp = String.format("%06d", new Random().nextInt(999999));
        String hashedOtp = passwordEncoder.encode(otp);

        WhatsAppToken token =  new WhatsAppToken();
        token.setPhoneNumber(generateWhatsAppOtpRequest.getPhoneNumber());
        token.setToken(hashedOtp);
        token.setUsed(false);
        token.setCreatedAt(LocalDateTime.now());
        token.setUpdatedAt(token.getCreatedAt());
        token.setExpiresAt(LocalDateTime.now().plusHours(1));


        whatsAppTokenRepository.save(token);

        // To Send OTP via WhatsApp API service: Finish later.
        // whatsAppSenderService.sendWhatsAppOtp(generateWhatsAppOtpRequest.getPhoneNumber(), otp);

        return new GenerateWhatsAppOtpResponse(true, "OTP sent via WhatsApp.");
    }

    public VerifyWhatsAppOtpResponse verifyWhatsAppOtp(VerifyWhatsAppOtpRequest request) {
        Optional<WhatsAppToken> optionalToken = whatsAppTokenRepository
                .findTopByPhoneNumberOrderByCreatedAtDesc(request.getPhoneNumber());

        if (optionalToken.isEmpty()) {
            return new VerifyWhatsAppOtpResponse(false, "No OTP found for this phone number.");
        }

        WhatsAppToken token = optionalToken.get();

        if (token.isUsed()) {
            return new VerifyWhatsAppOtpResponse(false, "OTP has already been used.");
        }

        if (!passwordEncoder.matches(request.getOtp(), token.getToken())) {
            return new VerifyWhatsAppOtpResponse(false, "Invalid OTP.");
        }

        // Mark as used
        token.setUsed(true);
        token.setUpdatedAt(LocalDateTime.now());
        whatsAppTokenRepository.save(token);

        return new VerifyWhatsAppOtpResponse(true, "OTP verified successfully.");
    }

}
