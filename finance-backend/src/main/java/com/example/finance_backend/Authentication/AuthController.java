package com.example.finance_backend.Authentication;

import com.example.finance_backend.Accounts.EmailService;
import com.example.finance_backend.User.User;
import com.example.finance_backend.User.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final EmailService emailService;

    public AuthController(UserRepository userRepository,
                          PasswordEncoder passwordEncoder, JwtService jwtService, EmailService emailService){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.emailService = emailService;
    }
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            return ResponseEntity.badRequest().body("Email is already registered");
        }

        //PasswordEncoder is injected by spring
        User user = new User(
                request.email(),
                passwordEncoder.encode(request.password())
        );
        user.setVerified(false);
        user.setEmailToken(UUID.randomUUID().toString());

        userRepository.save(user);
        String token = jwtService.generateToken(user);
        emailService.sendVerificationEmail(user.getEmail(), user.getEmailToken());
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verify(@RequestParam String token) {
        User user = userRepository.findByEmailToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        user.setVerified(true);
        user.setEmailToken(null);
        userRepository.save(user);
        return ResponseEntity.ok("Email verified");
    }
    @PostMapping("/resend-verification")
    public ResponseEntity<?> resendVerification(@RequestParam String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (user.isVerified()) {
            return ResponseEntity.badRequest().body("Email is already verified");
        }
        user.setEmailToken(UUID.randomUUID().toString());
        userRepository.save(user);
        emailService.sendVerificationEmail(user.getEmail(), user.getEmailToken());
        return ResponseEntity.ok("Verification email resent, check your inbox");
    }
    //request body takes json from front end and sends it to java method
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElse(null);

        //match has to be used becasue of salt in encoders
        if (user == null || !passwordEncoder.matches(request.password(), user.getPassword())) {
            return ResponseEntity.status(401).body("Invalid email or password");
        }

        String token = jwtService.generateToken(user);
        return ResponseEntity.ok(new AuthResponse(token));
    }
}

