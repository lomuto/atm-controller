package com.lomuto;

import java.util.ArrayList;
import java.util.HashMap;

public class Bank {
    private final HashMap<String, ArrayList<Account>> accountsByCardNumber;
    private final HashMap<String, String> hashedPinByCardNumber;

    public Bank(HashMap<String, ArrayList<Account>> accountsByCardNumber, HashMap<String, String> hashedPinByCardNumber) {
        this.accountsByCardNumber = accountsByCardNumber;
        this.hashedPinByCardNumber = hashedPinByCardNumber;
    }

    public ArrayList<Account> getAccountsOrNull(String cardNumber) {
        return accountsByCardNumber.get(cardNumber);
    }

    public boolean hasCard(String cardNumber) {
        return accountsByCardNumber.containsKey(cardNumber);
    }

    public boolean isPinValid(String cardNumber, String pin) {
        // Assume that pin has been hashed
        return hashedPinByCardNumber.get(cardNumber).equals(pin);
    }
}
