package com.example.finance_backend.Income;

import com.example.finance_backend.User.User;
import com.example.finance_backend.User.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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


}
