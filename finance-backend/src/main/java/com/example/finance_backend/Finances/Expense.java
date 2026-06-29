package com.example.finance_backend.Finances;

import com.example.finance_backend.User.User;
import jakarta.persistence.*;


//tells JPA this class maps to a database table
@Entity
@Table(name = "expenses")
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "expense_seq")
    @SequenceGenerator(name = "expense_seq", sequenceName = "expense_seq", allocationSize = 1)
    private Long id;

    private String store;
    private double amount;
    private int numPurchases;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    public Expense(){}

    public Expense(String store, double amount) {
        this.store = store;
        this.amount = amount;
    }

    public Expense(String store, double amount, User owner, int numPurchases) {
        this.store = store;
        this.amount = amount;
        this.owner = owner;
        this.numPurchases = numPurchases;
    }
    //getters
    public Long getId() { return id;}
    public String getStore() { return store;}
    public double getAmount() {return amount;}
    public User getOwner() { return owner;}
    public int getNumPurchases() {return numPurchases;}

    public void setNumPurchases(int numPurchases) {this.numPurchases = numPurchases;}
    public void setId(Long id) { this.id = id; }
    public void setStore(String store) { this.store = store; }
    public void setAmount(double amount) { this.amount = amount; }
    public void setOwner(User owner) { this.owner = owner; }

    }
