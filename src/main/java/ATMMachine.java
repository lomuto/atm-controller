package com.lomuto;

import com.lomuto.exception.ATMException;
import com.lomuto.exception.ATMFullException;
import com.lomuto.exception.NotEnoughRemianCashException;
import com.lomuto.states.ATMState;
import com.lomuto.states.AccountSelected;
import com.lomuto.states.HasCard;
import com.lomuto.states.HasCorrectPin;
import com.lomuto.states.NoCard;

import java.util.ArrayList;

public class ATMMachine {
    public static ArrayList<Bank> banks = new ArrayList<>();
    private static int MAX_CASH = 10000;
    private static double FEE = 0.1;

    private ATMState hasCard;
    private ATMState noCard;
    private ATMState hasCorrectPin;
    private ATMState accountSelected;

    private int remainCash;
    private String clientCardNumber;
    private Bank clientBank;
    private ArrayList<Account> clientAccounts;
    private Account selectedAccount;
    private ATMState atmState;

    public ATMMachine(int remainCash) {
        this.remainCash = remainCash;

        hasCard = new HasCard(this);
        noCard = new NoCard(this);
        hasCorrectPin = new HasCorrectPin(this);
        accountSelected = new AccountSelected(this);

        atmState = noCard;
    }

    public ATMMachine() {
        this(2000);
    }

    public ATMState getAtmState() {
        return this.atmState;
    }

    public String getCardNumberOrNull() {
        return this.clientCardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.clientCardNumber = cardNumber;
    }

    public Bank getClientBankOrNull() {
        return this.clientBank;
    }

    public void setClientBank(Bank clientBank) {
        this.clientBank = clientBank;
    }

    public ArrayList<Account> getClientAccountsOrNull() {
        return clientAccounts;
    }

    public void setClientAccounts(ArrayList<Account> clientAccounts) {
        this.clientAccounts = clientAccounts;
    }

    public Account getSelectedAccountOrNull() {
        return selectedAccount;
    }

    public void setSelectedAccount(Account selectedAccount) {
        this.selectedAccount = selectedAccount;
    }

    public void setATMState(ATMState newATMState) {
        this.atmState = newATMState;
    }

    public void insertCard(String cardNumber) throws ATMException {
        atmState.insertCard(cardNumber);
    }

    public void enterPin(String pin) throws ATMException {
        atmState.enterPin(pin);
    }

    public void selectAccount(int index) throws ATMException {
        atmState.selectAccount(index);
    }

    public int getBalance() throws ATMException {
        return atmState.getBalance();
    }

    public ATMState getNoCardState() {
        return this.noCard;
    }

    public ATMState getHasCardState() {
        return this.hasCard;
    }

    public ATMState getHasCorrectPinState() {
        return this.hasCorrectPin;
    }

    public ATMState getAccountSelectedState() {
        return this.accountSelected;
    }

    public int getRemainCash() {
        return this.remainCash;
    }

    public int withdraw(int amount) throws NotEnoughRemianCashException {
        int totalAmountOfCashToWithDraw = (int) (amount * (1 + FEE));
        if (this.remainCash - totalAmountOfCashToWithDraw < 0) {
            throw new NotEnoughRemianCashException();
        }

        this.selectedAccount.withdraw(totalAmountOfCashToWithDraw);
        this.remainCash -= amount;

        return totalAmountOfCashToWithDraw;
    }

    public void deposit(int amount) throws ATMFullException {
        if (this.remainCash + amount > ATMMachine.MAX_CASH) {
            throw new ATMFullException();
        }

        this.selectedAccount.deposit(amount);
        this.remainCash += amount;
    }
}
