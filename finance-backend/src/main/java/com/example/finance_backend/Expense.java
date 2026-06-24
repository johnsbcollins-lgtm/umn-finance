package com.example.finance_backend;

import jakarta.persistence.*;

//tells JPA this class maps to a database table
@Entity
@Table(name = "expenses")
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String store;
    private double amount;

    public Expense() { }

    public Expense(String store, double amount) {
        this.store = store;
        this.amount = amount;
    }
    //getters
    public Long getId() { return id;}
    public String getStore() { return store;}
    public double getAmount() {return amount;}

    public void setId(Long id) { this.id = id; }
    public void setStore(String store) { this.store = store; }
    public void setAmount(double amount) { this.amount = amount; }

    }
