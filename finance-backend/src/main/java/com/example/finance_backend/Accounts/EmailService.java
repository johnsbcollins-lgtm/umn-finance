package com.example.finance_backend.Accounts;

import com.example.finance_backend.User.User;
import com.example.finance_backend.User.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EmailService {

    @Value("${resend.api.key}")
    private String apiKey;

    @Value("${app.frontend.ur1}")
    private String url;

    private final UserService userService;
    private final RestTemplate restTemplate = new RestTemplate();

    public EmailService(UserService userService) {
        this.userService = userService;
    }


        public void sendVerificationEmail(String to, String token) {

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + apiKey);
            headers.setContentType(MediaType.APPLICATION_JSON);
            System.out.println("Got past headers");

            Map<String, Object> body = new HashMap<>();
            body.put("from", "noreply@gopherbudget.com");
            body.put("to", List.of(to));
            body.put("subject", "Email Verification");
            body.put("text", "Please click the following link to verify your email:\n\n"
                    + url + "/auth/verify?token=" + token);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

            try {
                restTemplate.postForObject(
                        "https://api.resend.com/emails",
                        request,
                        String.class
                );
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                throw new RuntimeException("Failed to send verification email: " + e.getMessage());
            }
        }


    public boolean emailAuth(String email){
        User user = userService.getOwner(email);
        return user.isVerified();
    }
}
