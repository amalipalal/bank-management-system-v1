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

    private static String displayAmount(double amount) {
        return String.format("$%,.2f", amount);
    }

    private static String displayDecimal(double decimal) {
        return String.format("%.1f%%", decimal);
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

    public static void displayTransaction(Transaction transaction) {
        double previousAccountBalance = transaction.getBalanceAfter() - transaction.getAmount();

        System.out.println("Transaction ID: " + transaction.getTransactionId());
        System.out.println("Account: " + transaction.getAccountNumber());
        System.out.println("Type: " + transaction.getTransactionType());
        System.out.println("Amount: " + transaction.getAmount());
        System.out.println("Previous Balance: " + displayAmount(previousAccountBalance));
        System.out.println("New Balance: " + displayAmount(transaction.getBalanceAfter()));
        System.out.println("Date/Time: " + displayTimestamp(transaction.getTimestamp()));
    }

    public static void displayMultipleTransactions(Transaction[] transactions) {
        String columnFormat = "| %-15s | %-20s | %-10s | %-15s | %-15s |%n";

        System.out.println("-".repeat(DISPLAY_STROKE_LENGTH));
        System.out.printf(columnFormat, "TXN ID", "DATE/TIME", "TYPE", "AMOUNT", "BALANCE");
        System.out.println("-".repeat(DISPLAY_STROKE_LENGTH));

        for (Transaction transaction : transactions) {
            String dateTime = displayTimestamp(transaction.getTimestamp());
            String type = transaction.getTransactionType();

            String amountSign = type.equalsIgnoreCase("Deposit") ? "+" : "-";
            String amount = amountSign + displayAmount(transaction.getAmount());
            String balance = displayAmount(transaction.getBalanceAfter());

            System.out.printf(columnFormat, transaction.getTransactionId(), dateTime, type, amount, balance);
            System.out.println("-".repeat(DISPLAY_STROKE_LENGTH));
        }
    }

    private static String displayTimestamp(String timestamp) {
        Instant instant = Instant.parse(timestamp);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-mm-yyyy hh:mm a");
        ZonedDateTime localDateTime = instant.atZone(ZoneId.systemDefault());
        return localDateTime.format(dateTimeFormatter);
    }

    public static void displayNotice(String message) {
        System.out.println("-".repeat(DISPLAY_STROKE_LENGTH));
        System.out.println(message);
        System.out.println("-".repeat(DISPLAY_STROKE_LENGTH));
    }

    public static  void displayHeading(String title) {
        System.out.println(title.toUpperCase());
        System.out.println("-".repeat(DISPLAY_STROKE_LENGTH));
    }
}
