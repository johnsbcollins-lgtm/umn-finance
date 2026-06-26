package com.example.finance_backend;
//talks to database
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
//jpa takes care of making all the methods for me
@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long>{
    List<Expense> findAllByOwner(User owner);
    Expense findFirstByStoreAndOwner(String store, User owner);
    Expense findFirstByStoreContainingAndOwner(String keyword, User owner);
    void deleteAllByOwner(User owner);
}

