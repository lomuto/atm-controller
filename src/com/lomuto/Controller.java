package com.lomuto;

import com.lomuto.atm_exception.ATMErrorCode;
import com.lomuto.atm_exception.ATMException;

import java.util.ArrayList;

public class Controller {
    private static final int MAX_CASH = 1000;
    private static Controller controllerInstance;
    private int remainCash;
    private ArrayList<Bank> banks = new ArrayList<>();

    public static Controller getInstance() {
        if(controllerInstance == null) {
            controllerInstance = new Controller();
        }

        return controllerInstance;
    }

    public boolean setRemainCash(int amount) {
        if(amount < 0) {
            return false;
        }

        this.remainCash = amount;
        return true;
    }

    public boolean depositCash(int amount) {
        if(this.remainCash + amount > MAX_CASH) {
            return false;
        }

        this.remainCash += amount;
        return true;
    }

    public boolean withdrawCash(int amount) {
        if(this.remainCash - amount < 0) {
            return false;
        }

        this.remainCash -= amount;
        return true;
    }

    public boolean readCard(String cardNumber) {
        if(!isCardValid(cardNumber)) {
            return false;
        }

        for(Bank bank : banks) {
            if(bank.hasCard(cardNumber)) {
                return true;
            }
        }

        return false;
    }

    /*
        Luhn algorithm
     */
    private boolean isCardValid(String cardNumber) {
        int sum = 0;

        for(int i=0; i<cardNumber.length(); i++) {
            int digit = cardNumber.charAt(i);

            if(i%2 == 0) {
                digit *= 2;
            }

            sum += digit / 10;
            sum += digit % 10;
        }

        return (sum % 10 == 0);
    }

    private Controller(){}
}
