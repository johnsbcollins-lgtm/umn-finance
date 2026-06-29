package com.example.finance_backend;

import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


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
    public List<Expense> getExpenses(Authentication auth){
        return expenseService.getExpenses(auth.getName());
    }
    @GetMapping("/date")
    public String getDate(Authentication auth) {
        return expenseService.getDate(auth.getName());
    }
    @PostMapping("/upload")
    public ResponseEntity<String> uploadCSV(@RequestParam("file") MultipartFile file, Authentication auth){
        try {
        expenseService.parseAndSaveCSV(file, auth.getName());
        return ResponseEntity.ok("CSV uploaded successfully");
    } catch (Exception e) {
        return ResponseEntity.status(500).body("Upload failed: " + e.getMessage());
        }
    }

    @PostMapping("/change-vendor-totals")
    public ResponseEntity<String> changeVendorTotals(@RequestBody ChangeVendorRequest request, Authentication auth) {
        expenseService.changeVendorTotals(request.vendor(), request.type(), request.amount(), auth.getName());
        return ResponseEntity.ok("Vendor totals updated");
    }

    @GetMapping("/search")
    public Expense searchByStore(@RequestParam String store, Authentication auth) {
        return expenseService.getExpensesByStore(store, auth.getName());
    }

    @DeleteMapping("/all")
    public ResponseEntity<String> deleteAllExpenses(Authentication auth) {
        expenseService.deleteAllExpenses(auth.getName());
        return ResponseEntity.ok("All expenses deleted");
    }
}
