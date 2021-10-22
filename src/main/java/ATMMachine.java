import exception.ATMException;
import exception.ATMFullException;
import exception.NotEnoughRemianCashException;

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
    private int attemptPinCount = 0;
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

    public static double getFee() {
        return ATMMachine.FEE;
    }

    public static int getMaxCash() {
        return ATMMachine.MAX_CASH;
    }

    public int getAttemptPinCount() {
        return attemptPinCount;
    }

    public void increaseAttempt() {
        this.attemptPinCount++;
    }

    public String getClientCardNumberOrNull() {
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

    public int getBalance() throws ATMException {
        return atmState.getBalance();
    }

    public int withdraw(int amount) throws ATMException {
        int withdrawn = atmState.withdraw(amount);
        remainCash -= amount;
        return withdrawn;
    }

    public void deposit(int amount) throws ATMException {
        int deposited = atmState.deposit(amount);
        remainCash += amount;
    }

    public void revertAtm() {
        this.attemptPinCount = 0;
        this.clientCardNumber = null;
        this.clientBank = null;
        this.clientAccounts = null;
        this.selectedAccount = null;
        this.atmState = noCard;
    }
}
