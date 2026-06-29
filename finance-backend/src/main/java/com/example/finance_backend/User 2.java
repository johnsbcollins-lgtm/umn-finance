package com.example.finance_backend;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_seq")
    @SequenceGenerator(name = "users_seq", sequenceName = "users_seq", allocationSize = 1)
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
