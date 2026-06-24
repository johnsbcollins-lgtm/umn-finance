package com.example.finance_backend;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;


@RestController
@RequestMapping("/expenses")
public class ExpenseController {

    //final cause we don't want variable name to be reassigned
    private final ExpenseService expenseService;

    //Spring Boot dependecy injection--still not 100% on what the benefits of this are
    public ExpenseController(ExpenseService expenseService){
        this.expenseService = expenseService;
    }

    //Get all expenses
    @GetMapping
    public List<Expense> getExpenses(){
        return expenseService.getExpenses();
    }
    @GetMapping("/date")
    public String getDate() {
        return expenseService.getDate();
    }
    @PostMapping("/upload")
    public ResponseEntity<String> uploadCSV(@RequestParam("file") MultipartFile file){
        expenseService.deleteAllExpenses();
        try {
        expenseService.parseAndSaveCSV(file);
        return ResponseEntity.ok("CSV uploaded successfully");
    } catch (Exception e) {
        return ResponseEntity.status(500).body("Upload failed: " + e.getMessage());
    }
    }

    @GetMapping("/search")
    public Expense searchByStore(@RequestParam String store) {
        return expenseService.getExpensesByStore(store);
    }

    @DeleteMapping("/all")
    public ResponseEntity<String> deleteAllExpenses() {
        expenseService.deleteAllExpenses();
        return ResponseEntity.ok("All expenses deleted");
    }
}
