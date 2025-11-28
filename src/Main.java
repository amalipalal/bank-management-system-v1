import com.amalitech.bankmanagement.main.base.Account;
import com.amalitech.bankmanagement.main.base.Customer;
import com.amalitech.bankmanagement.main.domain.*;
import com.amalitech.bankmanagement.main.manager.AccountManager;
import com.amalitech.bankmanagement.main.manager.TransactionManager;
import com.amalitech.bankmanagement.main.service.BankingService;
import com.amalitech.bankmanagement.main.util.DataSeeder;
import com.amalitech.bankmanagement.main.util.DisplayUtil;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        BankingService bankingService = new BankingService(new AccountManager(), new TransactionManager());

        // Populate the program with already existing customer accounts
        // defined within seed method
        DataSeeder seeder = new DataSeeder(bankingService);
        seeder.seed();

        boolean userIsActive = true;

        while(userIsActive) {
            try {
                DisplayUtil.displayMainMenu();

                int userSelection = readInt(scanner, "Select an option (1-5)", 1, 5);
                System.out.println();

                switch(userSelection) {
                    case 1:
                        handleAccountCreationFlow(scanner, bankingService);
                        break;
                    case 2:
                        handleAccountListingFlow(scanner, bankingService);
                        break;
                    case 3:
                        handleTransactionFlow(scanner, bankingService);
                        break;
                    case 4:
                        handleTransactionListingFlow(scanner, bankingService);
                        break;
                    case 5:
                        userIsActive = false;
                        break;
                    default:
                        DisplayUtil.displayNotice("Wrong number selection");
                }
            } catch (Exception e) {
                DisplayUtil.displayNotice(e.getMessage());
            }
        }
    }

    public static void handleAccountCreationFlow(Scanner scanner, BankingService service) {
        DisplayUtil.displayHeading("Account Creation");

        Customer newCustomer = createCustomerFlow(scanner, service);

        System.out.println();
        System.out.println("Account type:");
        System.out.println("1. Savings Account (Interest: 3.5%, Min Balance: $500)");
        System.out.println("2. Checking Account (Overdraft: $1,000, Monthly Fee: $10)");
        System.out.println();

        Account newAccount = createAccountFlow(scanner, service, newCustomer);

        System.out.println();

        showUserAccount(newAccount);

        System.out.println();
    }

    private static Customer createCustomerFlow(Scanner scanner, BankingService bankingService) {
        String customerName = readNonEmptyString(scanner, "Enter customer name");

        int customerAge = readInt(scanner, "Enter customer age", 1, 120);

        String customerContact = readNonEmptyString(scanner, "Enter customer contact");

        String customerAddress = readNonEmptyString(scanner, "Enter customer address");

        System.out.println();
        System.out.println("Customer type:");
        System.out.println("1. Regular Customer (Standard banking services)");
        System.out.println("2. Premium Customer (Enhanced benefits, min balance $10,000)");
        System.out.println();

        int customerType = readInt(scanner, "Select type (1-2)", 1, 2);

        return customerType == 1
                ? new RegularCustomer(customerName, customerAge, customerContact, customerAddress)
                : new PremiumCustomer(customerName, customerAge, customerContact, customerAddress);
    }

    private static Account createAccountFlow(Scanner scanner, BankingService service, Customer customer) {
        int accountType = readInt(scanner, "Select type (1-2)", 1, 2);

        double initialDeposit = readDouble(scanner, "Enter initial deposit amount", 0);

        Account newAccount;
        if(accountType == 1) {
            newAccount = service.createSavingsAccount(customer, initialDeposit);
        } else {
            newAccount = service.createCheckingAccount(customer, initialDeposit);
        }

        return newAccount;
    }

    private static void showUserAccount(Account account) {
        System.out.println("Account Created successfully!");
        if(account instanceof SavingsAccount) {
            DisplayUtil.displayNewSavingsAccount((SavingsAccount) account);
        } else if (account instanceof CheckingAccount) {
            DisplayUtil.displayNewCheckingAccount((CheckingAccount) account);
        }
    }

    private static int readInt(Scanner scanner, String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt + ": ");

            String input = scanner.nextLine();

            try {
                int value = Integer.parseInt(input);
                if(value < min || value > max) {
                    System.out.println("Please enter a number between " + min + " and " + max + ".");
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }

    private static double readDouble(Scanner scanner, String prompt, double min) {
        while (true) {
            System.out.print(prompt + ": ");

            String input = scanner.nextLine();

            try {
                double value = Double.parseDouble(input);
                if(value < min) {
                    System.out.println("Please enter a number more than " + min + ".");
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }

        }
    }

    private static String readNonEmptyString(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt + ": ");
            String input = scanner.nextLine().trim();

            if(!input.isEmpty()) {
                return input;
            }

            System.out.println("Input cannot be empty");
        }
    }

    public static void handleAccountListingFlow(Scanner scanner, BankingService service) {
        System.out.println("ACCOUNT LISTING");

        Account[] allAccounts = service.viewAllAccounts();
        double totalBalance = service.getTotalBankBalance();

        DisplayUtil.displayAccountListing(allAccounts);

        System.out.println();

        System.out.println("Total Accounts: " + allAccounts.length);
        System.out.println("Total Bank Balance: " + DisplayUtil.displayAmount(totalBalance));
        System.out.println();
    }

    public static void handleTransactionFlow(Scanner scanner, BankingService service) {
        DisplayUtil.displayHeading("Process Transaction");

        System.out.println();

        Account customerAccount = handleAccountValidationFlow(scanner, service);

        Transaction newTransaction = handleTransactionTypeFlow(scanner, service, customerAccount);

        handleTransactionConfirmation(scanner, service, customerAccount, newTransaction);
    }

    private static Account handleAccountValidationFlow(Scanner scanner, BankingService service) {
        String accountNumber = readNonEmptyString(scanner, "Enter Account Number");

        Account customerAccount = service.getAccountByNumber(accountNumber);

        System.out.println("Account Details:");
        DisplayUtil.displayAccountDetails(customerAccount);

        System.out.println();

        return customerAccount;
    }

    private static Transaction handleTransactionTypeFlow(Scanner scanner, BankingService service, Account customerAccount) {
        System.out.println("Transaction type:");
        System.out.println("1. Deposit \n2. Withdrawal");
        System.out.println();

        int transactionType = readInt(scanner, "Select type (1-2)", 1, 2);

        double transactionAmount = readDouble(scanner, "Enter amount", 0);

        System.out.println();

        return transactionType == 1
                ? service.processDeposit(customerAccount, transactionAmount)
                : service.processWithdrawal(customerAccount, transactionAmount);
    }

    private static void handleTransactionConfirmation(Scanner scanner, BankingService service, Account customerAccount, Transaction newTransaction) {
        DisplayUtil.displayHeading("Transaction Confirmation");

        DisplayUtil.displayTransaction(newTransaction);

        System.out.println();

        boolean isConfirmed = readYesOrNo(scanner, "Confirm transaction? (Y/N)");

        if (isConfirmed) {
            boolean isSuccessful = service.confirmTransaction(customerAccount, newTransaction);

            if (isSuccessful) {
                System.out.println("Transaction completed successful!");
            } else {
                System.out.println("Transaction failed. Please try again.");
            }
        } else {
            System.out.println("Transaction not confirmed. Aborting.");
        }
    }

    private static boolean readYesOrNo(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt + ": ");

            String input = scanner.nextLine();

            switch (input.toLowerCase()) {
                case "y":
                    return true;
                case "n":
                    return false;
                default:
                    System.out.println("Invalid input: Please enter Y or N.");
            }
        }
    }

    public static void handleTransactionListingFlow(Scanner scanner, BankingService service) {
        DisplayUtil.displayHeading("View Transaction history");

        String accountNumber = readNonEmptyString(scanner, "Enter Account Number");

        Account customerAccount = service.getAccountByNumber(accountNumber);

        DisplayUtil.displayAccountDetails(customerAccount);

        Transaction[] customerTransactions = service.getTransactionsByAccount(accountNumber);

        if (customerTransactions.length == 0) {
            DisplayUtil.displayNotice("No transactions recorded for this account.");
        } else {
            DisplayUtil.displayMultipleTransactions(customerTransactions);
            displayTransactionTotals(customerTransactions, service);
        }
    }

    private static void displayTransactionTotals(Transaction[] transactions, BankingService service) {
        String accountNumber = transactions[0].getAccountNumber();

        double totalDeposits = service.getTotalDeposit(accountNumber);
        double totalWithdrawals = service.getTotalWithdrawals(accountNumber);

        double netChange = totalDeposits - totalWithdrawals;

        System.out.println("Total Transactions: " + transactions.length);
        System.out.println("Total Deposits: " + DisplayUtil.displayAmount(totalDeposits));
        System.out.println("Total Withdrawals: " + DisplayUtil.displayAmount(totalWithdrawals));
        System.out.println("Net Change: " + DisplayUtil.displayAmount(netChange));
    }
}