package com.lomuto;

import com.lomuto.atm_exception.ATMErrorCode;
import com.lomuto.atm_exception.ATMException;

import java.util.ArrayList;

public class Controller {
    private static final int MAX_CASH = 10000;
    private static ArrayList<Bank> banks = new ArrayList<>();
    private int remainCash;
    private final String currentProcessingCardNumber;

    private Controller(String cardNumber) {
        this.currentProcessingCardNumber = cardNumber;
    }

    public static Controller getControllerOrNull(String cardNumber) {
        if (Controller.isCardValid(cardNumber)) {
            return new Controller(cardNumber);
        }

        return null;
    }

    public boolean setRemainCash(int amount) {
        if (amount < 0) {
            return false;
        }

        this.remainCash = amount;
        return true;
    }

    public boolean depositCash(int amount) {
        if (this.remainCash + amount > MAX_CASH) {
            return false;
        }

        this.remainCash += amount;
        return true;
    }

    public boolean withdrawCash(int amount) {
        if (this.remainCash - amount < 0) {
            return false;
        }

        this.remainCash -= amount;
        return true;
    }

    private static boolean isCardValid(String cardNumber) {
        if (!isCardNumberValid(cardNumber)) {
            return false;
        }

        for (Bank bank : banks) {
            if (bank.hasCard(cardNumber)) {
                return true;
            }
        }

        return false;
    }

    /*
        Luhn algorithm
     */
    private static boolean isCardNumberValid(String cardNumber) {
        int sum = 0;

        for (int i = 0; i < cardNumber.length(); i++) {
            int digit = cardNumber.charAt(i);

            if (i % 2 == 0) {
                digit *= 2;
            }

            sum += digit / 10;
            sum += digit % 10;
        }

        return (sum % 10 == 0);
    }

    public boolean checkPin(String)
}
