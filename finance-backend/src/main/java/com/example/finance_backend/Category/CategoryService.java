package com.example.finance_backend.Category;

import com.example.finance_backend.Finances.Expense;
import com.example.finance_backend.Finances.ExpenseRepository;
import com.example.finance_backend.Finances.ExpenseService;
import com.example.finance_backend.User.User;
import com.example.finance_backend.User.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private final ExpenseRepository expenseRepository;
    private final CategoryRepository categoryRepository;
    private final String[] categories;

    public CategoryService(ExpenseRepository expenseRepository, CategoryRepository categoryRepository) {
        this.expenseRepository = expenseRepository;
        this.categoryRepository = categoryRepository;
        this.categories = new String[]{"food", "personal", "social"};
    }
    public List<Category> getCategories(User owner){
        return categoryRepository.findByOwner(owner);
    }

    public void handleCategories(User owner) {
        List<Expense> expense = expenseRepository.findAllByOwner(owner);
        expense.removeIf(e -> e.getCategory() == null);

        //Collectors groups by category
        //then sums the amount of each category
        Map<String, Double> totals = expense.stream()
                .collect(Collectors.groupingBy(
                        Expense::getCategory,
                        Collectors.summingDouble(Expense::getAmount)
                ));
        Map<String, Integer> numPurchases = expense.stream()
                .collect(Collectors.groupingBy(
                        Expense::getCategory,
                        Collectors.summingInt(Expense::getNumPurchases)
                ));

        //then saves the totals to the database--default value there in case there are no expenses
        for (String category : categories) {
            double total = totals.getOrDefault(category, 0.0);
            int numPurchase = numPurchases.getOrDefault(category, 0);
            categoryRepository.save(new Category(category, total, numPurchase, owner));
        }
    }
}

