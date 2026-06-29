package com.example.finance_backend.Finances;

public record ChangeVendorRequest(String vendor, String type, double amount) {
}
