package com.amalitech.bankmanagement.main.util;

import com.amalitech.bankmanagement.main.base.Account;
import com.amalitech.bankmanagement.main.base.Customer;
import com.amalitech.bankmanagement.main.domain.CheckingAccount;
import com.amalitech.bankmanagement.main.domain.SavingsAccount;
import com.amalitech.bankmanagement.main.domain.Transaction;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DisplayUtil {
    private static final int DISPLAY_STROKE_LENGTH = 59;

    public static void displayMainMenu() {
        System.out.println("1. Create Account");
        System.out.println("2. View Accounts");
        System.out.println("3. Process Transaction");
        System.out.println("4. View Transaction History");
        System.out.println("5. Exit");
    }

    public static void displayNewSavingsAccount(SavingsAccount account) {
        Customer customer = account.getCustomer();
        float interestPercentage = (float) account.getInterestRate() * 100;

        System.out.println("Account Number: " + account.getAccountNumber());
        System.out.println("Customer: " + displayCustomerDetails(customer));
        System.out.println("Account Type: " + account.getAccountType());
        System.out.println("Initial Balance: " + displayAmount(account.getBalance()));
        System.out.println("Interest Rate: " + displayDecimal(interestPercentage));
        System.out.println("Minimum Balance: " + displayAmount(account.getMinimumBalance()));
        System.out.println("Status: " + account.getStatus());
    }

    private static String displayCustomerDetails(Customer customer) {
        return customer.getName() + " (" + customer.getCustomerType() + ")";
    }

    public static String displayAmount(double amount) {
        return String.format("$%,.2f", amount);
    }

    private static String displayDecimal(double decimal) {
        return String.format("%.1f%%", decimal);
    }

    public static void displayNewCheckingAccount(CheckingAccount account) {
        Customer customer = account.getCustomer();
        String monthlyFeeMetadata = " (" + "WAIVED - " + customer.getCustomerType() + " Customer )";

        System.out.println("Account Number: " + account.getAccountNumber());
        System.out.println("Customer: " + displayCustomerDetails(customer));
        System.out.println("Account Type: " + account.getAccountType());
        System.out.println("Initial Balance: " + displayAmount(account.getBalance()));
        System.out.println("Overdraft Limit: " + displayAmount(account.getOverDraftLimit()));
        System.out.println("Monthly Fee: " + displayAmount(account.getMonthlyFee()) + monthlyFeeMetadata);
        System.out.println("Status: " + account.getStatus());
    }

    public static void displayAccountDetails(Account account) {
        Customer customer = account.getCustomer();

        System.out.println("Customer: " + customer.getName());
        System.out.println("Account type: " + account.getAccountType());
        System.out.println("Current Balance: " + displayAmount(account.getBalance()));
    }

    public static void displayAccountListing(Account[] accounts) {
        String columnFormat = "| %-15s | %-25s | %-10s | %-15s | %-10s |%n";

        System.out.println("-".repeat(DISPLAY_STROKE_LENGTH));
        System.out.printf(columnFormat, "ACC NO", "CUSTOMER NAME", "TYPE", "BALANCE", "STATUS");
        System.out.println("-".repeat(DISPLAY_STROKE_LENGTH));

        for (Account account : accounts) {
            if (account instanceof SavingsAccount) {
                displaySavingsAccountRow((SavingsAccount) account, columnFormat);
            } else if (account instanceof CheckingAccount) {
                displayCheckingAccountRow((CheckingAccount) account, columnFormat);
            }

            System.out.println("-".repeat(DISPLAY_STROKE_LENGTH));
        }
    }

    private static void displayCheckingAccountRow(CheckingAccount account, String columnFormat) {
        String accountNumber = account.getAccountNumber();
        String overDraftLimit = displayDecimal(account.getOverDraftLimit());
        String customerName = account.getCustomer().getName();
        String accountType = account.getAccountType();
        String monthlyFee = displayAmount(account.getMonthlyFee());
        String balance = displayAmount(account.getBalance());
        String status = account.getStatus();

        String customerDetails = customerName + "\n" + overDraftLimit;
        String typeDetails = accountType + "\n" + monthlyFee;

        System.out.printf(columnFormat, accountNumber, customerDetails, typeDetails, balance, status);
    }

    private static void displaySavingsAccountRow(SavingsAccount account, String columnFormat) {
        String accountNumber = account.getAccountNumber();
        String interestRate = displayDecimal(account.getInterestRate());
        String customerName = account.getCustomer().getName();
        String accountType = account.getAccountType();
        String minimumBalance = displayAmount(account.getMinimumBalance());
        String balance = displayAmount(account.getBalance());
        String status = account.getStatus();

        String customerDetails = customerName + "\n" + "Interest Rate" + interestRate;
        String typeDetails = accountType + "\n" + "Min Balance: " + minimumBalance;

        System.out.printf(columnFormat, accountNumber, customerDetails, typeDetails, balance, status);
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
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm a");
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
