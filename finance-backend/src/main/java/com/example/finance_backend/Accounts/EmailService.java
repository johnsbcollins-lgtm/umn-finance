package com.example.finance_backend.Accounts;

import com.example.finance_backend.User.User;
import com.example.finance_backend.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Value("${spring.mail.url}")
    private String url;

    private final UserService userService;
    private final JavaMailSender mailSender;

    public EmailService(UserService userService, JavaMailSender mailSender) {
        this.userService = userService;
        this.mailSender = mailSender;
    }


    public void sendVerificationEmail(String to, String token){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setFrom("noreply@gopherbudget.com");
        message.setSubject("Email Verification");
        message.setText("Please click on the following link to verify your email: \n\n" +
                url + "/auth/verify?token=" + token);
        mailSender.send(message);
    }

    public boolean emailAuth(String email){
        User user = userService.getOwner(email);
        return user.isVerified();
    }
    public void testEmail(){

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo("john.sb.collins@gmail.com");
        message.setSubject("Test Email");
        message.setText("This is a test email sent from the backend.");
        mailSender.send(message);
    }
}
