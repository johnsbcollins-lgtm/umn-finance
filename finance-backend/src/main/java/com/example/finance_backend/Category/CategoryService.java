package com.example.finance_backend.Category;

import com.example.finance_backend.Finances.Expense;
import com.example.finance_backend.Finances.ExpenseRepository;
import com.example.finance_backend.Finances.ExpenseService;
import com.example.finance_backend.Income.Income;
import com.example.finance_backend.Income.IncomeRepository;
import com.example.finance_backend.User.User;
import com.example.finance_backend.User.UserService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private final ExpenseRepository expenseRepository;
    private final CategoryRepository categoryRepository;
    private final IncomeRepository incomeRepository;
    private final String[] categories;

    public CategoryService(ExpenseRepository expenseRepository, CategoryRepository categoryRepository,
                           IncomeRepository incomeRepository) {
        this.expenseRepository = expenseRepository;
        this.categoryRepository = categoryRepository;
        this.incomeRepository = incomeRepository;
        this.categories = new String[]{"food", "personal", "social", "other", "money", "subscriptions"};
    }
    public List<Category> getCategories(User owner){
        return categoryRepository.findByOwner(owner);
    }
    public double getCategoryTotal(User owner){
        List<Category> categories = categoryRepository.findByOwner(owner);
        double total = Math.round(categories.stream().mapToDouble(Category::getAmount).sum() * 100.0) / 100.0;
        return total;
    }

    @Transactional
    public void deleteCategories(User owner){
        categoryRepository.deleteAllByOwner(owner);
    }

    public void handleCategories(User owner) {
        List<Expense> expense = expenseRepository.findAllByOwner(owner);
        List<Income> income = incomeRepository.findAllByOwner(owner);
        expense.removeIf(e -> e.getCategory() == null);
        income.removeIf(i -> i.getCategory() == null);
        //Collectors groups by category
        //then sums the amount of each category
        Map<String, Double> expenseTotals = expense.stream()
                .collect(Collectors.groupingBy(
                        Expense::getCategory,
                        Collectors.summingDouble(Expense::getAmount)
                ));
        /*
        Map<String, Double> incomeTotals = income.stream()
                .collect(Collectors.groupingBy(
                        Income::getCategory,
                        Collectors.summingDouble(Income::getAmount)
                ));
        Map<String, Double> totals = new HashMap<>(expenseTotals);
        incomeTotals.forEach((category, amount) ->
                totals.merge(category, -amount, Double::sum)
        );
        */
        Map<String, Integer> numPurchases = expense.stream()
                .collect(Collectors.groupingBy(
                        Expense::getCategory,
                        Collectors.summingInt(Expense::getNumPurchases)
                ));
        /*
        Map<String, Integer> numDeposits = income.stream()
                .collect(Collectors.groupingBy(
                        Income::getCategory,
                        Collectors.summingInt(Income::getNumDeposits)
                ));
        Map<String, Integer> num = new HashMap<>(numPurchases);
        numDeposits.forEach((category, amount) ->
                num.merge(category, amount, Integer::sum)
        );
        */
        //then saves the totals to the database--default value there in case there are no expenses
        for (String category : categories) {
            double total = Math.round(expenseTotals.getOrDefault(category, 0.0) * 100.0) / 100.0;
            int nums = numPurchases.getOrDefault(category, 0);
            categoryRepository.save(new Category(category, total, nums, owner));
        }
    }
}

