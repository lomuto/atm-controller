package com.lomuto;

public class NoCard implements ATMState {
    private ATMMachine atmMachine;

    public NoCard(ATMMachine atmMachine) {
        this.atmMachine = atmMachine;
    }

    @Override
    public boolean insertCard(String cardNumber) {
        if (!isCardNumberValid(cardNumber)) {
            return false;
        }

        atmMachine.setCardNumber(cardNumber);
        return true;
    }

    @Override
    public boolean enterPin(int pin) {
        return false;
    }

    @Override
    public void selectAccount() {

    }

    @Override
    public int getBalance() {
        return 0;
    }

    @Override
    public int withdraw() {
        return 0;
    }

    @Override
    public void deposit(int amount) {

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
