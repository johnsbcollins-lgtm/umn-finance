package com.example.finance_backend.Income;

import com.example.finance_backend.User.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface IncomeRepository extends JpaRepository<Income, Long> {
    List<Income> findAllByOwner(User owner);
    void deleteAllByOwner(User owner);
    Income findFirstByStoreAndOwner(String store, User owner);
    void delete(Income income);

}
