package com.amalitech.bankmanagement.main.domain;

import com.amalitech.bankmanagement.main.base.Account;
import com.amalitech.bankmanagement.main.base.Customer;
import com.amalitech.bankmanagement.main.interfaces.Transactable;

public class SavingsAccount extends Account implements Transactable {
    private final double interestRate;
    private final double minimumBalance;

    public SavingsAccount(Customer customer, double balance, String status) {
        super(customer, balance, status);
        this.interestRate = 0.035;
        this.minimumBalance = 500;
    }

    @Override
    public String displayAccountDetails() {
        return "";
    }

    @Override
    public boolean processTransaction(double amount, String type) {
        if(amount <= 0) return false;

        switch (type.toLowerCase()) {
            case "deposit":
                super.deposit(amount);
                break;
            case "withdraw":
                withdraw(amount);
                break;
            default:
                return false;
        }

        return true;
    }

    @Override
    public void withdraw(double amount) {

        if(amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive");
        }

        double currentAccountBalance = super.getBalance();

        if(currentAccountBalance < minimumBalance) {
            throw new IllegalStateException("Withdrawal not allowed: balance is at minimum");
        }

        double newAccountBalance = currentAccountBalance - amount;
        if(newAccountBalance < 0) {
            throw new IllegalArgumentException("Withdrawal amount exceeds available balance");
        }

        super.setBalance(newAccountBalance);
    }

    public double calculateInterest() {
        return super.getBalance() * this.interestRate;
    }

    public String getAccountType() {
        return "Savings";
    }

    public double getInterestRate() {
        return interestRate;
    }

    public double getMinimumBalance() {
        return minimumBalance;
    }
}
