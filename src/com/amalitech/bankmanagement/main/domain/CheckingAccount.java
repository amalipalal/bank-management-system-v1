package com.amalitech.bankmanagement.main.domain;

import com.amalitech.bankmanagement.main.base.Account;
import com.amalitech.bankmanagement.main.base.Customer;
import com.amalitech.bankmanagement.main.interfaces.Transactable;

public class CheckingAccount extends Account implements Transactable {
    private final double overDraftLimit;
    private final double monthlyFee;

    public CheckingAccount(Customer customer, double balance, String status) {
        super(customer, balance, status);
        this.overDraftLimit = 1000;
        this.monthlyFee = 10;
    }

    @Override
    public String displayAccountDetails() {
        return "";
    }

    @Override
    public String getAccountType() {
        return "Checking";
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
        double newAccountBalance = currentAccountBalance - amount;

        if(newAccountBalance < -this.overDraftLimit) {
            throw new IllegalStateException("Withdrawal not allowed: overdraft limit is exceeded");
        }

        super.setBalance(newAccountBalance);
    }

    public void applyMonthlyFee() {
        double newAccountBalance = super.getBalance() - this.monthlyFee;
        if(newAccountBalance < -this.overDraftLimit) {
            throw new IllegalStateException("Monthly fee cannot be applied: overdraft limit exceeded");
        }
        super.setBalance(newAccountBalance);
    }

    public double getOverDraftLimit() {
        return this.overDraftLimit;
    }

    public double getMonthlyFee() {
        return this.monthlyFee;
    }
}
