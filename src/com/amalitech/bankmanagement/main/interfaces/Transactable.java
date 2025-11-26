package com.amalitech.bankmanagement.main.interfaces;

public interface Transactable {
    public boolean processTransaction(double amount, String type);
}
