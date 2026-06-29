package com.example.finance_backend;

public class StoreData {
    private double amount;
    private int numPurchases;
    private String vendor;
    public StoreData(String vendor) {
        this.vendor = vendor;
        this.amount = 0;
        this.numPurchases = 0;
    }

    public String getVendor() {return vendor;}
    public double getAmount() {return amount;}
    public int getNumPurchases() {return numPurchases;}
    public void setAmount(double amount) {this.amount = amount;}
    public void setNumPurchases(int numPurchases) {this.numPurchases = numPurchases;}
}
