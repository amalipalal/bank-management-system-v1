package com.amalitech.bankmanagement.main.base;

public abstract class Account {
    private final String accountNumber;
    private final Customer customer;
    private double balance;
    private final String status;
    private static int accountCounter = 0;

    public Account(String accountNumber, Customer customer, double balance, String status) {
        this.accountNumber = accountNumber;
        this.customer = customer;
        this.balance = balance;
        this.status = status;

        increaseAccountCount();
    }

    private void increaseAccountCount() {
        accountCounter++;
    }

    public abstract String displayAccountDetails();

    public abstract String getAccountType();

    public abstract void withdraw(double amount);

    public void deposit(double amount) {
        this.balance += amount;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public Customer getCustomer() {
        return customer;
    }

    public double getBalance() {
        return balance;
    }

    public String getStatus() {
        return status;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

}
