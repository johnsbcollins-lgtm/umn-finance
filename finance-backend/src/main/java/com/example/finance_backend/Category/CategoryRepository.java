package com.example.finance_backend.Category;

import com.example.finance_backend.User.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByOwner(User owner);
    void deleteAllByOwner(User owner);
}
