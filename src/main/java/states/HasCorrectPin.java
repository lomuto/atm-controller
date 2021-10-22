package com.lomuto.states;

import com.lomuto.ATMMachine;
import com.lomuto.exception.AccountNotSelectedException;
import com.lomuto.exception.CardAlreadyInsertedException;
import com.lomuto.exception.ClientAlreadyAuthenticated;

public class HasCorrectPin implements ATMState {
    private ATMMachine atmMachine;

    public HasCorrectPin(ATMMachine atmMachine) {
        this.atmMachine = atmMachine;
    }

    @Override
    public void insertCard(String cardNumber) throws CardAlreadyInsertedException {
        throw new CardAlreadyInsertedException();
    }

    @Override
    public void enterPin(String pin) throws ClientAlreadyAuthenticated {
        throw new ClientAlreadyAuthenticated();
    }

    @Override
    public void selectAccount(int index) {
        atmMachine.setSelectedAccount(atmMachine.getClientAccountsOrNull().get(index));
        atmMachine.setATMState(atmMachine.getAccountSelectedState());
    }

    @Override
    public int getBalance() throws AccountNotSelectedException {
        throw new AccountNotSelectedException();
    }

    @Override
    public int withdraw(int amount) throws AccountNotSelectedException {
        throw new AccountNotSelectedException();
    }

    @Override
    public void deposit(int amount) throws AccountNotSelectedException {
        throw new AccountNotSelectedException();
    }
}
