package com.example.finance_backend.Category;

import com.example.finance_backend.User.User;
import jakarta.persistence.*;

@Entity
@Table(name = "category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "category_seq")
    @SequenceGenerator(name = "category_seq", sequenceName = "category_seq", allocationSize = 1)
    private Long id;

    private String category;
    private double amount;
    private int numPurchases;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    protected Category() {
    }

    public Category(String category, double amount, int numPurchases, User owner){
        this.category = category;
        this.amount = amount;
        this.owner = owner;
        this.numPurchases = numPurchases;
    }

    public Long getId() { return id;}
    public String getCategory() { return category;}
    public double getAmount() {return amount;}
    public User getOwner() { return owner;}
    public int getNumPurchases() {return numPurchases;}

    public void setNumPurchases(int numPurchases) {this.numPurchases = numPurchases;}
    public void setId(Long id) { this.id = id; }
    public void setCategory(String category) { this.category = category; }
    public void setAmount(double amount) { this.amount = amount; }
    public void setOwner(User owner) { this.owner = owner; }
}
