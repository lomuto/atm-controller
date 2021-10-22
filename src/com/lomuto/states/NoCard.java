package com.lomuto.states;

import com.lomuto.ATMMachine;
import com.lomuto.Bank;
import com.lomuto.exception.InvalidCardException;
import com.lomuto.exception.NoCardException;

public class NoCard implements ATMState {
    private ATMMachine atmMachine;

    public NoCard(ATMMachine atmMachine) {
        this.atmMachine = atmMachine;
    }

    @Override
    public void insertCard(String cardNumber) throws InvalidCardException {
        if (!isCardNumberValid(cardNumber)) {
            throw new InvalidCardException();
        }

        atmMachine.setCardNumber(cardNumber);
        for (Bank bank : ATMMachine.banks) {
            if (bank.hasCard(cardNumber)) {
                atmMachine.setClientBank(bank);
                atmMachine.setATMState(atmMachine.getHasCardState());
                return;
            }
        }

        throw new InvalidCardException();
    }

    @Override
    public void enterPin(String pin) throws NoCardException {
        throw new NoCardException();
    }

    @Override
    public void selectAccount(int index) throws NoCardException {
        throw new NoCardException();
    }

    @Override
    public int getBalance() throws NoCardException {
        throw new NoCardException();
    }

    @Override
    public int withdraw(int amount) throws NoCardException {
        throw new NoCardException();
    }

    @Override
    public void deposit(int amount) throws NoCardException {
        throw new NoCardException();
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
}
