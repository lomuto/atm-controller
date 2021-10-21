package com.lomuto;

import java.util.ArrayList;
import java.util.HashMap;

public class Bank {
    private final HashMap<String, ArrayList<Account>> accountsByCardNumber;

    public Bank() {
        this.accountsByCardNumber = new HashMap<>();
    }

    public ArrayList<Account> getAccounts(String cardNumber) {
        return accountsByCardNumber.get(cardNumber);
    }
}
