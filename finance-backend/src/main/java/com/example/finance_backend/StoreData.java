package com.example.finance_backend;

public class StoreData {
    private int numPurchases;
    private String vendor;
    private double income;
    private double expenses;
    public StoreData(String vendor) {
        this.vendor = vendor;
        this.expenses = 0;
        this.numPurchases = 0;
        this.income = 0;
    }

    public String getVendor() {return vendor;}
    public double getincome() {return income;}
    public Double getExpenses() {return expenses;}
    public int getNumPurchases() {return numPurchases;}
    public void setExpenses(double expenses) {this.expenses = expenses;}
    public void setIncome(double income) {this.income = income;}
    public void setNumPurchases(int numPurchases) {this.numPurchases = numPurchases;}
}
