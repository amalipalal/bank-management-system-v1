package com.amalitech.bankmanagement.main.util;

import com.amalitech.bankmanagement.main.base.Customer;
import com.amalitech.bankmanagement.main.domain.PremiumCustomer;
import com.amalitech.bankmanagement.main.domain.RegularCustomer;
import com.amalitech.bankmanagement.main.service.BankingService;

public class DataSeeder {
    private final BankingService bankingService;

    public DataSeeder(BankingService bankingService) {
        this.bankingService = bankingService;
    }

    public void seed() {
        createSavingsAccount(
                new RegularCustomer("Alice Johnson", 28, "0101010101", "Accra"),
                1200
        );

        createCheckingAccount(
                new PremiumCustomer("Michael Mensah", 45, "0202020202", "Kumasi"),
                20000
        );

        createCheckingAccount(
                new RegularCustomer("Sarah Boateng", 34, "0303030303", "Tema"),
                850
        );

        createSavingsAccount(
                new PremiumCustomer("Kwame Frimpong", 50, "0404040404", "Cape Coast"),
                15000
        );

        createSavingsAccount(
                new RegularCustomer("John Doe", 22, "0505050505", "Takoradi"),
                750
        );
    }

    private void createSavingsAccount(Customer customer, double initialDeposit) {
        bankingService.createSavingsAccount(customer, initialDeposit);
    }

    private void createCheckingAccount(Customer customer, double initialDeposit) {
        bankingService.createCheckingAccount(customer, initialDeposit);
    }
}
