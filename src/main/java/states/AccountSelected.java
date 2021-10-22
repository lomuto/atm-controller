package main.java.states;

import main.java.ATMMachine;
import main.java.exception.ATMFullException;
import main.java.exception.AccountAlreadySelectedException;
import main.java.exception.CardAlreadyInsertedException;
import main.java.exception.ClientAlreadyAuthenticated;
import main.java.exception.NotEnoughRemianCashException;

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
