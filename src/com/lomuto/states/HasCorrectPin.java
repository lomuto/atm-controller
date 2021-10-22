package com.lomuto;

public class HasCorrectPin implements ATMState {
    private ATMMachine atmMachine;

    public HasCorrectPin(ATMMachine atmMachine) {
        this.atmMachine = atmMachine;
    }

    @Override
    public boolean insertCard(String cardNumber) {
        return false;
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
}
