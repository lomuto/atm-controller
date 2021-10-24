import exception.ATMException;
import exception.ATMFullException;
import exception.AccountAlreadySelectedException;
import exception.AccountNotSelectedException;
import exception.CardAlreadyInsertedException;
import exception.ClientAlreadyAuthenticated;
import exception.IncorrectPinException;
import exception.InvalidCardException;
import exception.NoAuthenticationException;
import exception.NoCardException;
import exception.NotEnoughRemianCashException;

import java.util.ArrayList;

public class ATMMachine {
    public static ArrayList<Bank> banks = new ArrayList<>();
    private static int MAX_CASH = 10000;
    private static double FEE = 0.1;

    /*
        Interface for ATM State
     */
    private static interface ATMState {
        void insertCard(String cardNumber) throws ATMException;

        void enterPin(String pin) throws ATMException;

        void selectAccount(int index) throws ATMException;

        int getBalance() throws ATMException;

        int withdraw(int amount) throws ATMException;

        int deposit(int amount) throws ATMException;
    }

    /*
        Has no card state
     */
    private static class NoCard implements ATMState {
        private final ATMMachine atmMachine;

        public NoCard(ATMMachine atmMachine) {
            this.atmMachine = atmMachine;
        }

        /*
            Luhn algorithm
         */
        private static boolean isCardNumberValid(String cardNumber) {
            int sum = 0;
            boolean isSecond = false;

            for (int i = cardNumber.length() - 1; i >= 0; i--) {
                int digit = cardNumber.charAt(i) - '0';

                if (isSecond) {
                    digit *= 2;
                }

                sum += digit / 10;
                sum += digit % 10;

                isSecond ^= true;
            }
            return (sum % 10 == 0);
        }

        @Override
        public void insertCard(String cardNumber) throws InvalidCardException {
            if (!isCardNumberValid(cardNumber)) {
                throw new InvalidCardException();
            }

            atmMachine.clientCardNumber = cardNumber;
            for (Bank bank : ATMMachine.banks) {
                if (bank.hasCard(cardNumber)) {
                    atmMachine.clientBank = bank;
                    atmMachine.atmState = atmMachine.getHasCardState();
                    return;
                }
            }

            throw new InvalidCardException();
        }

        @Override
        public void enterPin(String pin) throws NoCardException {
            throw new NoCardException();
        }

        @Override
        public void selectAccount(int index) throws NoCardException {
            throw new NoCardException();
        }

        @Override
        public int getBalance() throws NoCardException {
            throw new NoCardException();
        }

        @Override
        public int withdraw(int amount) throws NoCardException {
            throw new NoCardException();
        }

        @Override
        public int deposit(int amount) throws NoCardException {
            throw new NoCardException();
        }
    }

    /*
        Has Card state
     */
    private static class HasCard implements ATMState {
        private final ATMMachine atmMachine;

        public HasCard(ATMMachine atmMachine) {
            this.atmMachine = atmMachine;
        }

        @Override
        public void insertCard(String cardNumber) throws CardAlreadyInsertedException {
            atmMachine.revertAtm();
            throw new CardAlreadyInsertedException();
        }

        @Override
        public void enterPin(String pin) throws IncorrectPinException {
            if (!atmMachine.clientBank.isPinValid(atmMachine.clientCardNumber, pin)) {
                atmMachine.attemptPinCount++;
                if (atmMachine.getAttemptPinCount() == 5) {
                    atmMachine.revertAtm();
                }
                throw new IncorrectPinException();
            }

            atmMachine.atmState = atmMachine.getHasCorrectPinState();
            atmMachine.clientAccounts = atmMachine.clientBank.getAccounts(atmMachine.clientCardNumber);
        }

        @Override
        public void selectAccount(int index) throws NoAuthenticationException {
            atmMachine.revertAtm();
            throw new NoAuthenticationException();
        }

        @Override
        public int getBalance() throws NoAuthenticationException {
            atmMachine.revertAtm();
            throw new NoAuthenticationException();
        }

        @Override
        public int withdraw(int amount) throws NoAuthenticationException {
            atmMachine.revertAtm();
            throw new NoAuthenticationException();
        }

        @Override
        public int deposit(int amount) throws NoAuthenticationException {
            atmMachine.revertAtm();
            throw new NoAuthenticationException();
        }
    }

    /*
        Has Correct Pin State
     */
    private static class HasCorrectPin implements ATMState {
        private final ATMMachine atmMachine;

        public HasCorrectPin(ATMMachine atmMachine) {
            this.atmMachine = atmMachine;
        }

        @Override
        public void insertCard(String cardNumber) throws CardAlreadyInsertedException {
            atmMachine.revertAtm();
            throw new CardAlreadyInsertedException();
        }

        @Override
        public void enterPin(String pin) throws ClientAlreadyAuthenticated {
            throw new ClientAlreadyAuthenticated();
        }

        @Override
        public void selectAccount(int index) {
            atmMachine.selectedAccount = atmMachine.clientAccounts.get(index);
            atmMachine.atmState = atmMachine.getAccountSelectedState();
        }

        @Override
        public int getBalance() throws AccountNotSelectedException {
            atmMachine.revertAtm();
            throw new AccountNotSelectedException();
        }

        @Override
        public int withdraw(int amount) throws AccountNotSelectedException {
            atmMachine.revertAtm();
            throw new AccountNotSelectedException();
        }

        @Override
        public int deposit(int amount) throws AccountNotSelectedException {
            atmMachine.revertAtm();
            throw new AccountNotSelectedException();
        }
    }

    /*
        Account Selected State
     */
    private static class AccountSelected implements ATMState {
        private final ATMMachine atmMachine;

        public AccountSelected(ATMMachine atmMachine) {
            this.atmMachine = atmMachine;
        }

        @Override
        public void insertCard(String cardNumber) throws CardAlreadyInsertedException {
            atmMachine.revertAtm();
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
            int balance = atmMachine.selectedAccount.getBalance();
            atmMachine.revertAtm();
            return balance;
        }

        @Override
        public int withdraw(int amount) throws NotEnoughRemianCashException {
            int totalAmountOfCashToWithDraw = (int) (amount * (1 + atmMachine.getFee()));
            if (atmMachine.remainCash - totalAmountOfCashToWithDraw < 0) {
                atmMachine.revertAtm();
                throw new NotEnoughRemianCashException();
            }

            atmMachine.selectedAccount.withdraw(totalAmountOfCashToWithDraw);
            atmMachine.revertAtm();
            return totalAmountOfCashToWithDraw;
        }

        @Override
        public int deposit(int amount) throws ATMFullException {
            if (atmMachine.remainCash + amount > MAX_CASH) {
                atmMachine.revertAtm();
                throw new ATMFullException();
            }

            atmMachine.selectedAccount.deposit(amount);
            atmMachine.revertAtm();
            return amount;
        }
    }


    private final ATMState hasCard;
    private final ATMState noCard;
    private final ATMState hasCorrectPin;
    private final ATMState accountSelected;

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

    public String getClientCardNumberOrNull() {
        return this.clientCardNumber;
    }

    public Bank getClientBankOrNull() {
        return this.clientBank;
    }

    public ArrayList<Account> getClientAccountsOrNull() {
        return clientAccounts;
    }

    public Account getSelectedAccountOrNull() {
        return selectedAccount;
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
        atmState.deposit(amount);
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
