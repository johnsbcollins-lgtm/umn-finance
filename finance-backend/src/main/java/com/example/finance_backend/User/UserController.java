package com.example.finance_backend.User;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }
    @PostMapping("/upload")
    public ResponseEntity<String> uploadCSV(@RequestParam("file") MultipartFile file, Authentication auth){
        try {
            userService.parseAndSaveCSV(file, auth.getName());
            return ResponseEntity.ok("CSV uploaded successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Upload failed: " + e.getMessage());
        }
    }
    @GetMapping("/date")
    public String getDate(Authentication auth) {
        return userService.getDate(auth.getName());
    }

    @GetMapping("/months")
    public ResponseEntity<Double> getMonths(Authentication auth) {
        return ResponseEntity.ok(userService.getMonth(auth.getName()));
    }
}
