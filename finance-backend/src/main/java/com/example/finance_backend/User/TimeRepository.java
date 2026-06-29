package com.example.finance_backend.User;
import org.springframework.data.jpa.repository.JpaRepository;
public interface TimeRepository extends JpaRepository<Time, Long>{
    Time findFirstByOwner(User owner);
    void deleteAllByOwner(User owner);
}
