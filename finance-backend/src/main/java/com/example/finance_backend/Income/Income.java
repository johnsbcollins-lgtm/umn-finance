package com.example.finance_backend.Income;

import com.example.finance_backend.User.User;
import jakarta.persistence.*;

@Entity
@Table(name = "income")
public class Income {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "income_seq")
    @SequenceGenerator(name = "income_seq", sequenceName = "income_seq", allocationSize = 1)
    private Long id;

    private String store;
    private double amount;
    private int numDeposits;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    public Income(){}

    public Income(String store, double amount) {
        this.store = store;
        this.amount = amount;
    }

    public Income(String store, double amount, User owner, int numDeposits) {
        this.store = store;
        this.amount = amount;
        this.owner = owner;
        this.numDeposits = numDeposits;
    }
    //getters
    public Long getId() { return id;}
    public String getStore() { return store;}
    public double getAmount() {return amount;}
    public User getOwner() { return owner;}

    public void setId(Long id) { this.id = id; }
    public void setStore(String store) { this.store = store; }
    public void setAmount(double amount) { this.amount = amount; }
    public void setOwner(User owner) { this.owner = owner; }

}
