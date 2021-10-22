package com.lomuto.states;

import com.lomuto.ATMMachine;
import com.lomuto.exception.ATMFullException;
import com.lomuto.exception.AccountAlreadySelectedException;
import com.lomuto.exception.CardAlreadyInsertedException;
import com.lomuto.exception.ClientAlreadyAuthenticated;
import com.lomuto.exception.NotEnoughRemianCashException;

public class AccountSelected implements ATMState {
    private ATMMachine atmMachine;

    public AccountSelected(ATMMachine atmMachine) {
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
    public void selectAccount(int index) throws AccountAlreadySelectedException {
        throw new AccountAlreadySelectedException();
    }

    @Override
    public int getBalance() {
        int balance = atmMachine.getSelectedAccountOrNull().getBalance();
        atmMachine.setATMState(atmMachine.getNoCardState());
        return balance;
    }

    @Override
    public int withdraw(int amount) throws NotEnoughRemianCashException {
        int withdrawn = atmMachine.withdraw(amount);
        atmMachine.setATMState(atmMachine.getNoCardState());
        return withdrawn;
    }

    @Override
    public void deposit(int amount) throws ATMFullException {
        atmMachine.deposit(amount);
        atmMachine.setATMState(atmMachine.getNoCardState());
    }
}
