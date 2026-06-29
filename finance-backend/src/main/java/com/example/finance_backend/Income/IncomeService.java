package com.example.finance_backend.Income;

import com.example.finance_backend.User.User;
import com.example.finance_backend.User.UserService;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.List;


@Service
public class IncomeService {
    private final IncomeRepository incomeRepository;
    private final UserService userService;

    public IncomeService(IncomeRepository incomeRepository, UserService userService) {
        this.userService = userService;
        this.incomeRepository = incomeRepository;
    }
    @Transactional
    public void deleteAllIncome(String email){
        User owner = userService.getOwner(email);
        incomeRepository.deleteAllByOwner(owner);}

    public void changeVendorTotals(String store, String type, double amount, String email){
        User owner = userService.getOwner(email);
        Income income = incomeRepository.findFirstByStoreAndOwner(store, owner);
        if(type.equals("positive")){
            income.setAmount(income.getAmount() + amount);
        }
        else{
            income.setAmount(income.getAmount() - amount);
        }
        incomeRepository.save(income);
    }

    public List<Income> getAllIncome(User owner){return incomeRepository.findAllByOwner(owner);}
}
