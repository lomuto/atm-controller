import exception.AccountNotSelectedException;
import exception.CardAlreadyInsertedException;
import exception.ClientAlreadyAuthenticated;

public class HasCorrectPin implements ATMState {
    private ATMMachine atmMachine;

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
        atmMachine.setSelectedAccount(atmMachine.getClientAccountsOrNull().get(index));
        atmMachine.setATMState(atmMachine.getAccountSelectedState());
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
