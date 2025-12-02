package com.amalitech.bankmanagement.main.service;

import com.amalitech.bankmanagement.main.base.Account;
import com.amalitech.bankmanagement.main.base.Customer;
import com.amalitech.bankmanagement.main.domain.CheckingAccount;
import com.amalitech.bankmanagement.main.domain.PremiumCustomer;
import com.amalitech.bankmanagement.main.domain.SavingsAccount;
import com.amalitech.bankmanagement.main.domain.Transaction;
import com.amalitech.bankmanagement.main.manager.AccountManager;
import com.amalitech.bankmanagement.main.manager.TransactionManager;

public class BankingService {
    private final AccountManager accountManager;
    private final TransactionManager transactionManager;

    public BankingService(AccountManager accountManager, TransactionManager transactionManager) {
        this.accountManager = accountManager;
        this.transactionManager = transactionManager;
    }

    public Transaction processDeposit(Account account, double amount) {
        double balanceAfterTransaction = account.getBalance() + amount;
        return new Transaction("deposit", account.getAccountNumber(), amount, balanceAfterTransaction);
    }

    public Transaction processWithdrawal(Account account, double amount) {
        double balanceAfterTransaction = account.getBalance() - amount;
        return new Transaction("withdraw", account.getAccountNumber(), amount, balanceAfterTransaction);
    }

    ///  Updates the bank account balance and adds transaction to transaction store
    public boolean confirmTransaction(Account account, Transaction transaction) {
        if ("withdraw".equals(transaction.getTransactionType())) {
            account.withdraw(transaction.getAmount());
        } else {
            account.deposit(transaction.getAmount());
        }

        // Record the transaction only after successful account update
        this.transactionManager.addTransaction(transaction);

        return true;
    }

    public Account createSavingsAccount(Customer customer) {
        // Account object's initial deposit will be processed as a transaction
        SavingsAccount newAccount = new SavingsAccount(customer, 0, "active");
        accountManager.addAccount(newAccount);
        return newAccount;
    }

    public Account createCheckingAccount(Customer customer) {
        // Account object's initial deposit will be processed as a transaction
        CheckingAccount newAccount = new CheckingAccount(customer, 0, "active");

        if(customer instanceof PremiumCustomer) newAccount.setMonthlyFee(0);

        accountManager.addAccount(newAccount);

        return newAccount;
    }

    public Account[] viewAllAccounts() {
        return accountManager.getAllAccounts();
    }

    public Transaction[] getTransactionsByAccount(String accountNumber) {
        return transactionManager.viewTransactionsByAccount(accountNumber);
    }

    public double getTotalDeposit(String accountNumber) {
        return transactionManager.calculateTotalDeposits(accountNumber);
    }

    public double getTotalWithdrawals(String accountNumber) {
        return transactionManager.calculateTotalWithdrawals(accountNumber);
    }

    public double getTotalBankBalance() {
        return accountManager.getTotalBalance();
    }

    public int getAccountCount() {
        return accountManager.getAccountCount();
    }

    public Account getAccountByNumber(String accountNumber) {
        return accountManager.findAccount(accountNumber);
    }
}
