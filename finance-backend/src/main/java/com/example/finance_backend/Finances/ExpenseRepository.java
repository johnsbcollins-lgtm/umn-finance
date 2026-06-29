package com.example.finance_backend.Finances;
//talks to database
import com.example.finance_backend.User.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
//jpa takes care of making all the methods for me
@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long>{
    List<Expense> findAllByOwner(User owner);
    Expense findFirstByStoreAndOwner(String store, User owner);
    void deleteAllByOwner(User owner);
}

