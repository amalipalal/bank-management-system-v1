package com.amalitech.bankmanagement.main.util;

import com.amalitech.bankmanagement.main.base.Account;
import com.amalitech.bankmanagement.main.base.Customer;
import com.amalitech.bankmanagement.main.domain.CheckingAccount;
import com.amalitech.bankmanagement.main.domain.SavingsAccount;
import com.amalitech.bankmanagement.main.domain.Transaction;

public class DisplayUtil {

    public static void displayNewSavingsAccount(SavingsAccount account) {
        Customer customer = account.getCustomer();
        float interestPercentage = (float) account.getInterestRate() * 100;

        System.out.println("Account Number: " + account.getAccountNumber());
        System.out.println("Customer: " + customer.getName() + " (" + customer.getCustomerType() + ")");
        System.out.println("Account Type: " + account.getAccountType());
        System.out.println("Initial Balance: $" + String.format("%,.2f", account.getBalance()));
        System.out.println("Interest Rate: " + String.format("%.1f%%", interestPercentage));
        System.out.println("Minimum Balance: $" + String.format("%,.2f", account.getBalance()));
        System.out.println("Status: " + account.getStatus());
    }

    public static void displayNewCheckingAccount(CheckingAccount account) {
        Customer customer = account.getCustomer();

        System.out.println("Account Number: " + account.getAccountNumber());
        System.out.println("Customer: " + customer.getName() + " (" + customer.getCustomerType() + ")");
        System.out.println("Account Type: " + account.getAccountType());
        System.out.println("Initial Balance: $" + String.format("%,.2f", account.getBalance()));
        System.out.println("Overdraft Limit: " + String.format("%,.2f", account.getOverDraftLimit()));
        System.out.println("Minimum Balance: $" + String.format("%,.2f", account.getBalance()));
        System.out.println("Monthly Fee: $" + account.getMonthlyFee() + " (" + "WAIVED - " + customer.getCustomerType() + " Customer");
        System.out.println("Status: " + account.getStatus());
    }

    public static void displayAccountDetails(Account account) {
        Customer customer = account.getCustomer();

        System.out.println("Customer: " + customer.getName());
        System.out.println("Account type: " + account.getAccountType());
        System.out.println("Current Balance: " + String.format("%,.2f", account.getBalance()));
    }

}
