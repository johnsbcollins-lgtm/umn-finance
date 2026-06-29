package com.example.finance_backend.Finances;

import com.example.finance_backend.User.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;


@RestController
@RequestMapping("/expenses")
public class ExpenseController {

    //final cause we don't want variable name to be reassigned
    private final ExpenseService expenseService;
    private final UserService userService;

    //Spring Boot dependecy injection--still not 100% on what the benefits of this are
    public ExpenseController(ExpenseService expenseService, UserService userService){
        this.expenseService = expenseService;
        this.userService = userService;
    }

    //Get all expenses
    @GetMapping
    public List<Expense> getExpenses(Authentication auth){
        return expenseService.getExpenses(auth.getName());
    }

    @PostMapping("/change-vendor-totals")
    public ResponseEntity<String> changeVendorTotals(@RequestBody ChangeVendorRequest request, Authentication auth) {
        expenseService.changeVendorTotals(request.vendor(), request.type(), request.amount(), auth.getName());
        return ResponseEntity.ok("Expense totals updated");
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
