package com.chidimma.image_verification_system.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.beans.factory.annotation.Value;

//spring-managed bean, can now be autowired into other classes
@Service
public class FaceVerificationService {

    @Value("${file.upload-dir}")
    private String uploadDir;// inject upload directory path from application.properties

    public boolean verifyFaceWithPython(String storedImageFileName, MultipartFile faceImage) {
        try {
            // Join upload dir + filename to form the full path
            Path fullImagePath = Paths.get(uploadDir, storedImageFileName);
            File storedImage = fullImagePath.toFile();
            //log:
            System.out.println("Full path to stored image: " + storedImage.getAbsolutePath());
            System.out.println("File exists? " + storedImage.exists());


            //to read image into byte arrays
            byte[] img1Bytes = Files.readAllBytes(storedImage.toPath());
            byte[] img2Bytes = faceImage.getBytes();


            //to prepare multipart form body: boundary to separate the 2 image parts
            String boundary = "Boundary-" + System.currentTimeMillis();

            //initializes stream to build the HTTP POST body
            var byteStream = new ByteArrayOutputStream();
            var writer = new PrintWriter(new OutputStreamWriter(byteStream, StandardCharsets.UTF_8), true);

            // Part 1: img1 - writes header and data for stored image as a form field
            writer.append("--").append(boundary).append("\r\n");
            writer.append("Content-Disposition: form-data; name=\"img1\"; filename=\"stored.jpg\"\r\n");
            writer.append("Content-Type: image/jpeg\r\n\r\n");
            writer.flush();
            byteStream.write(img1Bytes);
            byteStream.write("\r\n".getBytes(StandardCharsets.UTF_8));

            // Part 2: img2 - same
            writer.append("--").append(boundary).append("\r\n");
            writer.append("Content-Disposition: form-data; name=\"img2\"; filename=\"login.jpg\"\r\n");
            writer.append("Content-Type: image/jpeg\r\n\r\n");
            writer.flush();
            byteStream.write(img2Bytes);
            byteStream.write("\r\n".getBytes(StandardCharsets.UTF_8));

            //end form data
            writer.append("--").append(boundary).append("--").append("\r\n");
            writer.close();

            //build POST http request to the python API with the multipart body
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:5000/verify"))
                    .header("Content-Type", "multipart/form-data; boundary=" + boundary)
                    .POST(HttpRequest.BodyPublishers.ofByteArray(byteStream.toByteArray()))
                    .build();

            //send request and receive response
            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            //log:
            System.out.println("Python verification response: " + response.body());

            return response.body().contains("\"verified\": true");

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to verify face with Python service", e);
        }
    }
}
