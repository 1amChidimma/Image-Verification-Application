package com.chidimma.image_verification_system.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WhatsAppSenderService {
    
    private static final String PHONE_NUMBER_ID = "PHONE_NUMBER_ID";
    private static final String ACCESS_TOKEN = "ACCESS_TOKEN";
    private static final String WHATSAPP_API_URL = "https://graph.facebook.com/v18.0/" + PHONE_NUMBER_ID + "/messages";

    public void sendWhatsAppOtp(String phoneNumber, String otp){

        if (ACCESS_TOKEN.startsWith("ACCESS_")) {
            System.out.println("WhatsApp API not yet configured. Skipping actual send.");
            return;
        } //checks if the API is properly configured

        String messageBody = "Your verification code is: " + otp;
        //This constructs the actual message content sent via whatsApp.

        // a JSON string that follows the structure expected by the whatsApp cloud API
        String jsonPayload = """
        {
            "messaging_product": "whatsapp",
            "to": "%s", //the recipient's phone no
            "type": "text",
            "text": {
                "body": "%s"
            }
        }
        """.formatted(phoneNumber, messageBody);//replaces the '%s' with the phone no and message

        //to set http headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);// tells server that JSON is being sent
        headers.setBearerAuth(ACCESS_TOKEN);

        HttpEntity<String> request = new HttpEntity<>(jsonPayload, headers); //combines the jsonPayload and headers into one request object

        //sends html post request
        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(WHATSAPP_API_URL, request, String.class);
            System.out.println("WhatsApp message sent successfully: " + response.getBody());
        } catch (Exception e) {
            System.err.println("Failed to send WhatsApp message: " + e.getMessage());
        }

    }
}
