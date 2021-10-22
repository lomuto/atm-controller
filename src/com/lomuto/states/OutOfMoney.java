package com.lomuto.states;

import com.lomuto.exception.ATMException;

public class OutOfMoney implements ATMState {
    @Override
    public void insertCard(String cardNumber) throws ATMException {
        throw new ATMOutOfMoneyException();
    }

    @Override
    public void enterPin(String pin) throws ATMException {
        throw new ATMOutOfMoneyException();
    }

    @Override
    public void selectAccount(int index) throws ATMException {
        throw new ATMOutOfMoneyException();
    }

    @Override
    public int getBalance() throws ATMException {
        throw new ATMOutOfMoneyException();
    }

    @Override
    public int withdraw(int amount) throws ATMException {
        throw new ATMOutOfMoneyException();
    }

    @Override
    public void deposit(int amount) throws ATMException {
        throw new ATMOutOfMoneyException();
    }
}
