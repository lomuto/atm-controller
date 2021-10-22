package com.lomuto.states;

import com.lomuto.ATMMachine;
import com.lomuto.exception.CardAlreadyInsertedException;
import com.lomuto.exception.IncorrectPinException;
import com.lomuto.exception.NoAuthenticationException;

public class HasCard implements ATMState {
    private ATMMachine atmMachine;

    public HasCard(ATMMachine atmMachine) {
        this.atmMachine = atmMachine;
    }

    @Override
    public void insertCard(String cardNumber) throws CardAlreadyInsertedException {
        throw new CardAlreadyInsertedException();
    }

    @Override
    public void enterPin(String pin) throws IncorrectPinException {
        if (!atmMachine.getClientBankOrNull().isPinValid(atmMachine.getCardNumberOrNull(), pin)) {
            throw new IncorrectPinException();
        }

        atmMachine.setATMState(atmMachine.getHasCorrectPinState());
        atmMachine.setClientAccounts(atmMachine.getClientBankOrNull().getAccounts(atmMachine.getCardNumberOrNull()));
    }

    @Override
    public void selectAccount(int index) throws NoAuthenticationException {
        throw new NoAuthenticationException();
    }

    @Override
    public int getBalance() throws NoAuthenticationException {
        throw new NoAuthenticationException();
    }

    @Override
    public int withdraw(int amount) throws NoAuthenticationException {
        throw new NoAuthenticationException();
    }

    @Override
    public void deposit(int amount) throws NoAuthenticationException {
        throw new NoAuthenticationException();
    }
}
