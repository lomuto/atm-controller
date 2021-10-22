package main.java.states;

import main.java.ATMMachine;
import main.java.Bank;
import main.java.exception.InvalidCardException;
import main.java.exception.NoCardException;

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
        boolean isSecond = false;

        for (int i = cardNumber.length() - 1; i >= 0; i--) {
            int digit = cardNumber.charAt(i) - '0';

            if (isSecond) {
                digit *= 2;
            }

            sum += digit / 10;
            sum += digit % 10;

            isSecond ^= true;
        }
        return (sum % 10 == 0);
    }
}
