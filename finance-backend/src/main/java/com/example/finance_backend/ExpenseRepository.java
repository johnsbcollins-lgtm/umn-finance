package com.example.finance_backend;
//talks to database
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//jpa takes care of making all the methods for me
@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long>{
    Expense findFirstByStore(String store);
    Expense findFirstByStoreContaining(String keyword);
}

