package com.example.finance_backend;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.GenerationType;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String password;

    protected User() {}

    public User(String email, String password){
        this.email = email;
        this.password = password;
    }

    public Long getID(){return id;}
    public String getEmail(){return email;}
    public String getPassword(){return password;}

}
