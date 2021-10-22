package com.lomuto.states;

import com.lomuto.exception.ATMException;
import com.lomuto.exception.InvalidCardException;

public interface ATMState {
    void insertCard(String cardNumber) throws ATMException;
    void enterPin(String pin) throws ATMException;
    void selectAccount(int index) throws ATMException;
    int getBalance() throws ATMException;
    int withdraw(int amount) throws ATMException;
    void deposit(int amount) throws ATMException;
}
