package com.example.finance_backend.User;

public class StoreData {
    private int numPurchases;
    private String vendor;
    private double income;
    private double expenses;
    private int numDeposits;
    public StoreData(String vendor) {
        this.vendor = vendor;
        this.expenses = 0;
        this.numPurchases = 0;
        this.income = 0;
        this.numDeposits = 0;
    }

    public String getVendor() {return vendor;}
    public double getIncome() {return income;}
    public double getExpenses() {return expenses;}
    public int getNumPurchases() {return numPurchases;}
    public int getNumDeposits() {return numDeposits;}

    public void setNumDeposits(int numDeposits) {this.numDeposits = numDeposits;}
    public void setExpenses(double expenses) {this.expenses = expenses;}
    public void setIncome(double income) {this.income = income;}
    public void setNumPurchases(int numPurchases) {this.numPurchases = numPurchases;}
}
