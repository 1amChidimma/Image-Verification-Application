package com.chidimma.image_verification_system.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


//spring-managed bean, can now be autowired into other classes
@Service
public class FaceVerificationService {
    public boolean verifyFaceUrls(String storedImageUrl, String loginImageUrl) {
        try {
            // Step 1: Prepare JSON body
            String jsonPayload = String.format(
                    "{ \"img1\": \"%s\", \"img2\": \"%s\" }",
                    storedImageUrl,
                    loginImageUrl
            );

            // Step 2: Build HTTP POST request
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:5000/verify"))  // your Python Flask server
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                    .build();

            // Step 3: Send request
            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Step 4: Log and check response
            System.out.println("Face verification response: " + response.body());

            return response.body().contains("\"verified\": true");

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to verify face using image URLs", e);
        }
    }
}
