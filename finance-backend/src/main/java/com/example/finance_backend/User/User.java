package com.example.finance_backend.User;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_seq")
    @SequenceGenerator(name = "users_seq", sequenceName = "users_seq", allocationSize = 1)
    private Long id;

    @NotBlank(message =  "Email is Required")
    @Email(message = "Please enter a valid email address")
    private String email;

    @NotBlank(message = "Password is Required")
    private String password;

    private boolean verified;
    private String emailToken;

    protected User() {}

    public User(String email, String password){
        this.email = email;
        this.password = password;
    }

    public boolean isVerified(){return verified;}
    public Long getID(){return id;}
    public String getEmail(){return email;}
    public String getPassword(){return password;}
    public String getEmailToken(){return emailToken;}

    public void setEmailToken(String emailToken){this.emailToken = emailToken;}
    public void setVerified(boolean verified){this.verified = verified;}
    public void setPassword(String password){this.password = password;}
    public void setEmail(String email){this.email = email;}
    public void setID(Long id){this.id = id;}

}
