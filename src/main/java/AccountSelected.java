import exception.ATMFullException;
import exception.AccountAlreadySelectedException;
import exception.CardAlreadyInsertedException;
import exception.ClientAlreadyAuthenticated;
import exception.NotEnoughRemianCashException;

public class AccountSelected implements ATMState {
    private ATMMachine atmMachine;

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
        int balance = atmMachine.getSelectedAccountOrNull().getBalance();
        atmMachine.revertAtm();
        return balance;
    }

    @Override
    public int withdraw(int amount) throws NotEnoughRemianCashException {
        int totalAmountOfCashToWithDraw = (int) (amount * (1 + atmMachine.getFee()));
        if (atmMachine.getRemainCash() - totalAmountOfCashToWithDraw < 0) {
            atmMachine.revertAtm();
            throw new NotEnoughRemianCashException();
        }

        atmMachine.getSelectedAccountOrNull().withdraw(totalAmountOfCashToWithDraw);
        atmMachine.revertAtm();
        return totalAmountOfCashToWithDraw;
    }

    @Override
    public int deposit(int amount) throws ATMFullException {
        if (atmMachine.getRemainCash() + amount > atmMachine.getMaxCash()) {
            atmMachine.revertAtm();
            throw new ATMFullException();
        }

        atmMachine.getSelectedAccountOrNull().deposit(amount);
        atmMachine.revertAtm();
        return amount;
    }
}
