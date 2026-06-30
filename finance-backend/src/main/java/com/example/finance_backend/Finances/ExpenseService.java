package com.example.finance_backend.Finances;

import com.example.finance_backend.User.TimeRepository;
import com.example.finance_backend.User.User;
import com.example.finance_backend.User.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ExpenseService {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;
    private final TimeRepository timeRepository;

    public ExpenseService(ExpenseRepository expenseRepository, UserRepository userRepository, TimeRepository timeRepository) {
        this.expenseRepository = expenseRepository;
        this.userRepository = userRepository;
        this.timeRepository = timeRepository;
    }

    public List<Expense> getExpenses(String email) {
        User owner = getOwner(email);
        List<Expense> expense = expenseRepository.findAllByOwner(owner);
        expense.removeIf(e -> e.getAmount() == 0);
        return expense;
    }

    public void changeVendorTotals(String store, String type, double amount, String email) {
        User owner = getOwner(email);
        Expense expense = expenseRepository.findFirstByStoreAndOwner(store, owner);
        if(type.equals("positive")){
            expense.setAmount(expense.getAmount() + amount);
        }
        else{
            expense.setAmount(expense.getAmount() - amount);
        }
        expenseRepository.save(expense);
    }

    public Expense getExpensesByStore(String store, String email) {
        User owner = getOwner(email);
        return expenseRepository.findFirstByStoreAndOwner(store, owner);
    }

    @Transactional
    public void deleteAllExpenses(String email) {
        User owner = getOwner(email);
        System.out.println("ExpenseService1");
        expenseRepository.deleteAllByOwner(owner);
        timeRepository.deleteAllByOwner(owner);
    }

    public User getOwner(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}

