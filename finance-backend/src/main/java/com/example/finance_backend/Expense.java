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

    @ManyToOne
    private User owner;

    public Expense(){}

    public Expense(String store, double amount) {
        this.store = store;
        this.amount = amount;
    }

    public Expense(String store, double amount, User user) {
        this.store = store;
        this.amount = amount;
        this.owner = user;
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
