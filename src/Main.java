import com.amalitech.bankmanagement.main.base.Account;
import com.amalitech.bankmanagement.main.base.Customer;
import com.amalitech.bankmanagement.main.domain.CheckingAccount;
import com.amalitech.bankmanagement.main.domain.PremiumCustomer;
import com.amalitech.bankmanagement.main.domain.RegularCustomer;
import com.amalitech.bankmanagement.main.domain.SavingsAccount;
import com.amalitech.bankmanagement.main.manager.AccountManager;
import com.amalitech.bankmanagement.main.manager.TransactionManager;
import com.amalitech.bankmanagement.main.service.BankingService;
import com.amalitech.bankmanagement.main.util.DisplayUtil;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        BankingService bankingService = new BankingService(new AccountManager(), new TransactionManager());
        boolean userIsActive = true;

        while(userIsActive) {
            DisplayUtil.displayMainMenu();

            System.out.print("Enter Choice: ");
            int userSelection = Integer.parseInt(scanner.nextLine());
            System.out.println();

            switch(userSelection) {
                case 1:
                    handleAccountCreationFlow(scanner, bankingService);
                    break;
                case 2:
                    handleAccountListingFlow(scanner, bankingService);
                    break;
                case 5:
                    userIsActive = false;
                    break;
                default:
                    DisplayUtil.displayNotice("Wrong number selection");
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
}