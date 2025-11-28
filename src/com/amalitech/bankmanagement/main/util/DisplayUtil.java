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
    private static final int DISPLAY_STROKE_LENGTH = 110;

    public static void displayMainMenu() {
        System.out.println();
        System.out.println("1. Create Account");
        System.out.println("2. View Accounts");
        System.out.println("3. Process Transaction");
        System.out.println("4. View Transaction History");
        System.out.println("5. Exit");
        System.out.println();
    }

    public static void displayNewSavingsAccount(SavingsAccount account) {
        Customer customer = account.getCustomer();
        float interestPercentage = (float) account.getINTEREST_RATE() * 100;

        System.out.println("Account Number: " + account.getAccountNumber());
        System.out.println("Customer: " + displayCustomerDetails(customer));
        System.out.println("Account Type: " + account.getAccountType());
        System.out.println("Initial Balance: " + displayAmount(account.getBalance()));
        System.out.println("Interest Rate: " + displayDecimal(interestPercentage));
        System.out.println("Minimum Balance: " + displayAmount(account.getMINIMUM_BALANCE()));
        System.out.println("Status: " + formatStatus(account.getStatus()));
    }

    private static float calculateInterestPercentage(double interestRate) {
        return (float) interestRate * 100;
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
        String monthlyFeeMetadata = generateMonthlyFeeMetadata(account);

        System.out.println("Account Number: " + account.getAccountNumber());
        System.out.println("Customer: " + displayCustomerDetails(customer));
        System.out.println("Account Type: " + account.getAccountType());
        System.out.println("Initial Balance: " + displayAmount(account.getBalance()));
        System.out.println("Overdraft Limit: " + displayAmount(account.getOVERDRAFT_LIMIT()));
        System.out.println("Monthly Fee: " + displayAmount(account.getMonthlyFee()) + monthlyFeeMetadata);
        System.out.println("Status: " + formatStatus(account.getStatus()));
    }

    private static String generateMonthlyFeeMetadata (CheckingAccount account) {
        Customer customer = account.getCustomer();

        if(account.getMonthlyFee() == 0)
            return " (" + "WAIVED - " + customer.getCustomerType() + " Customer )";

        return " (" + "NOT WAIVED - " + customer.getCustomerType() + " Customer )";
    }

    private static String formatStatus(String status) {
        return status.substring(0, 1).toUpperCase() + status.substring(1);
    }

    public static void displayAccountDetails(Account account) {
        Customer customer = account.getCustomer();

        System.out.println("Customer: " + customer.getName());
        System.out.println("Account type: " + account.getAccountType());
        System.out.println("Current Balance: " + displayAmount(account.getBalance()));
    }

    public static void displayAccountListing(Account[] accounts) {
        String columnFormat = "| %-15s | %-25s | %-25s | %-15s | %-10s |%n";

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
        String overDraftLimit = displayAmount(account.getOVERDRAFT_LIMIT());
        String customerName = account.getCustomer().getName();
        String accountType = account.getAccountType();
        String monthlyFee = displayAmount(account.getMonthlyFee());
        String balance = displayAmount(account.getBalance());
        String status = formatStatus(account.getStatus());

        System.out.printf(columnFormat, accountNumber, customerName, accountType, balance, status);

        // Display additional details in the next line of the same row for overdraft and monthly fee
        // in order to prevent the use of \n in the main row which would misalign the table
        String overDraftDisplay = "Overdraft Limit: " + overDraftLimit;
        String monthlyFeeDisplay = "Monthly Fee: " + monthlyFee;
        System.out.printf(columnFormat, "", overDraftDisplay, monthlyFeeDisplay, "", "");
    }

    private static void displaySavingsAccountRow(SavingsAccount account, String columnFormat) {
        float interestPercentage = calculateInterestPercentage(account.getINTEREST_RATE());

        String accountNumber = account.getAccountNumber();
        String interestRate = displayDecimal(interestPercentage);
        String customerName = account.getCustomer().getName();
        String accountType = account.getAccountType();
        String minimumBalance = displayAmount(account.getMINIMUM_BALANCE());
        String balance = displayAmount(account.getBalance());
        String status = formatStatus(account.getStatus());

        System.out.printf(columnFormat, accountNumber, customerName, accountType, balance, status);

        // Display additional details in the next line of the same row for interest rate and min balance
        // in order to prevent the use of \n in the main row which would misalign the table
        String interestRateDisplay = "Interest Rate: " + interestRate;
        String minimumBalanceDisplay = "Min Balance: " + minimumBalance;
        System.out.printf(columnFormat, "", interestRateDisplay, minimumBalanceDisplay, "", "");
    }

    public static void displayTransaction(Transaction transaction) {
        double previousAccountBalance = computePreviousBalance(transaction);

        System.out.println("Transaction ID: " + transaction.getTransactionId());
        System.out.println("Account: " + transaction.getAccountNumber());
        System.out.println("Type: " + transaction.getTransactionType());
        System.out.println("Amount: " + displayAmount(transaction.getAmount()));
        System.out.println("Previous Balance: " + displayAmount(previousAccountBalance));
        System.out.println("New Balance: " + displayAmount(transaction.getBalanceAfter()));
        System.out.println("Date/Time: " + displayTimestamp(transaction.getTimestamp()));
    }

    private static double computePreviousBalance (Transaction transaction) {
        if ("withdraw".equals(transaction.getTransactionType())) {
            return transaction.getBalanceAfter() + transaction.getAmount();
        } else if ("deposit".equals(transaction.getTransactionType())) {
            return transaction.getBalanceAfter() - transaction.getAmount();
        } else {
            return 0;
        }
    }

    public static void displayMultipleTransactions(Transaction[] transactions) {
        String columnFormat = "| %-15s | %-20s | %-10s | %-15s | %-15s |%n";

        System.out.println("-".repeat(DISPLAY_STROKE_LENGTH));
        System.out.printf(columnFormat, "TXN ID", "DATE/TIME", "TYPE", "AMOUNT", "BALANCE");
        System.out.println("-".repeat(DISPLAY_STROKE_LENGTH));

        for (Transaction transaction : transactions) {
            String dateTime = displayTimestamp(transaction.getTimestamp());
            String type = transaction.getTransactionType().toUpperCase();

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
        System.out.println();
    }

    public static  void displayHeading(String title) {
        System.out.println(title.toUpperCase());
        System.out.println("-".repeat(DISPLAY_STROKE_LENGTH));
    }
}
