package com.example.finance_backend.Income;

import com.example.finance_backend.User.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IncomeService {
    private final IncomeRepository incomeRepository;
    public IncomeService(IncomeRepository incomeRepository) {
        this.incomeRepository = incomeRepository;
    }

    public List<Income> getAllIncome(User owner){return incomeRepository.findAllByOwner(owner);}
}
