package com.lomuto;

import com.lomuto.atm_exception.ATMErrorCode;
import com.lomuto.atm_exception.ATMException;

import java.util.ArrayList;

public class Controller {
    private static final int MAX_CASH = 10000;
    private static ArrayList<Bank> banks = new ArrayList<>();
    private int remainCash;
    private final String clientCardNumber;
    private Bank clientBank;

    private Controller(String cardNumber, Bank bank) {
        this.clientCardNumber = cardNumber;
        this.clientBank = bank;
    }

    public static Controller getControllerOrNull(String cardNumber) {
        if (!isCardNumberValid(cardNumber)) {
            return null;
        }

        for (Bank bank : banks) {
            if (bank.hasCard(cardNumber)) {
                return new Controller(cardNumber, bank);
            }
        }

        return null;
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

    public static boolean depositCash(Controller controller, Account account, int amount) {
        if(controller.depositCash(amount)) {
            account.deposit(amount);
            return true;
        }

        return false;
    }

    public static boolean withdrawCash(Controller controller, Account account, int amount) {
        if(controller.withdrawCash(amount)) {
            account.withdraw(amount);
            return true;
        }

        return false;
    }

    public boolean setRemainCash(int amount) {
        if (amount < 0) {
            return false;
        }

        this.remainCash = amount;
        return true;
    }


    public ArrayList<Account> getAccountsOrNull(String pinNumber) {
        if(clientBank.isPinValid(this.clientCardNumber, pinNumber)) {
            return clientBank.getAccounts(clientCardNumber);
        }

        return null;
    }

    private boolean depositCash(int amount) {
        if (this.remainCash + amount > MAX_CASH) {
            return false;
        }

        this.remainCash += amount;
        return true;
    }

    private boolean withdrawCash(int amount) {
        if (this.remainCash - amount < 0) {
            return false;
        }

        this.remainCash -= amount;
        return true;
    }
}
