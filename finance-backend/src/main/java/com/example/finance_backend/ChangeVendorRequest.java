package com.example.finance_backend;

public record ChangeVendorRequest(String vendor, String type, double amount) {
}
