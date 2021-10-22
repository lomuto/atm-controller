package com.lomuto;

public interface ATMState {
    boolean insertCard(String cardNumber);
    boolean enterPin(int pin);
    void selectAccount();
    int getBalance();
    int withdraw();
    void deposit(int amount);
}
