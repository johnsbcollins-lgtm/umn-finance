package com.example.finance_backend.Category;

import com.example.finance_backend.User.User;
import com.example.finance_backend.User.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    private final UserService userService;
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService, UserService userService){
        this.categoryService = categoryService;
        this.userService = userService;
    }

    @GetMapping
    public List<Category> getCategories(Authentication auth){
        User owner = userService.getOwner(auth.getName());
    return categoryService.getCategories(owner);}

    @DeleteMapping("/all")
    public void deleteAllCategories(Authentication auth){
        User owner = userService.getOwner(auth.getName());
        categoryService.deleteCategories(owner);
    }
    @GetMapping("/total")
    public double getCategoryTotal(Authentication auth){
        User owner = userService.getOwner(auth.getName());
        return categoryService.getCategoryTotal(owner);
    }
}
