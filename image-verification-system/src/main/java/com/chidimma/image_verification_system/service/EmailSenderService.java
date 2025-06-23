package com.chidimma.image_verification_system.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailSenderService  {

    private final JavaMailSender mailSender;

    public void sendOtpEmail(String toEmail, String otp){
        try{
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(toEmail);
            helper.setSubject("Your OTP Code");
            helper.setText("Your OTP is: " + otp + "\nIt will expire in an hour.");

            mailSender.send(message);
        } catch (MessagingException e){
            throw new RuntimeException("Failed to send otp email", e);
        }
    }
}
