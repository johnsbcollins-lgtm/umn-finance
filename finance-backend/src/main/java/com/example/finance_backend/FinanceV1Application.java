package com.example.finance_backend;

import com.example.finance_backend.Accounts.EmailService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class FinanceV1Application {

	public static void main(String[] args) {
		SpringApplication.run(FinanceV1Application.class, args);
		/*
		ApplicationContext context = SpringApplication.run(FinanceV1Application.class, args);
		EmailService emailService = context.getBean(EmailService.class);
		emailService.testEmail();
		System.out.println("Email attempted send");
		*/
	}
}
