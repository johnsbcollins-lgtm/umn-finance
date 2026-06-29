package com.example.finance_backend.Income;

import com.example.finance_backend.Finances.ChangeVendorRequest;
import com.example.finance_backend.User.User;
import com.example.finance_backend.User.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.util.List;

@RestController
@RequestMapping("/income")
public class IncomeController {
    private final IncomeService incomeService;
    private final UserService userService;
    public IncomeController(IncomeService incomeService, UserService userService) {
        this.incomeService = incomeService;
        this.userService = userService;
    }

    @GetMapping
    public List<Income> getAllIncome(Authentication auth){
        User owner = userService.getOwner(auth.getName());
        return incomeService.getAllIncome(owner);}

    @DeleteMapping("/all")
    public void deleteAllIncome(Authentication auth){
        incomeService.deleteAllIncome(auth.getName());
    }
    @PostMapping("change-vendor-totals")
    public ResponseEntity<String> changeIncomeTotals(@RequestBody ChangeVendorRequest request, Authentication auth){
        incomeService.changeVendorTotals(request.vendor(), request.type(), request.amount(), auth.getName());
        return ResponseEntity.ok("Income totals updated");
    }
}
