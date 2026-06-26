package com.example.finance_backend;

import jakarta.persistence.*;
import jakarta.persistence.GenerationType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.SequenceGenerator;


//tells JPA this class maps to a database table
@Entity
@Table(name = "expenses")
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_seq")
    @SequenceGenerator(name = "users_seq", sequenceName = "users_seq", allocationSize = 1)
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

    public Expense(String store, double amount, User owner) {
        this.store = store;
        this.amount = amount;
        this.owner = owner;
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
