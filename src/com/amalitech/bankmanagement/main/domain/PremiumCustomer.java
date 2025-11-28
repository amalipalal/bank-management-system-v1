package com.amalitech.bankmanagement.main.domain;

import com.amalitech.bankmanagement.main.base.Customer;

public class PremiumCustomer extends Customer {

    public PremiumCustomer(String name, int age, String contact, String address) {
        super(name, age, contact, address);
    }

    @Override
    public String displayCustomerDetails() {
        return "";
    }

    @Override
    public String getCustomerType() {
        return "Premium";
    }

}
