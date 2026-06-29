package com.example.finance_backend;

import jakarta.persistence.*;

@Entity
@Table(name = "time")
public class Time {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_seq")
    @SequenceGenerator(name = "users_seq", sequenceName = "users_seq", allocationSize = 1)
    private Long id;

    private double months;
    private String date;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;
    public Time(){}
    public Time(double months, String date, User owner){
        this.months = months;
        this.date = date;
        this.owner = owner;
    }
    public Long getId() { return id;}
    public double getMonths() { return months;}
    public String getDate() { return date;}
    public User getOwner() { return owner;}

}

