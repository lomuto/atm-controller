import exception.CardAlreadyInsertedException;
import exception.IncorrectPinException;
import exception.NoAuthenticationException;

public class HasCard implements ATMState {
    private ATMMachine atmMachine;

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
        if (!atmMachine.getClientBankOrNull().isPinValid(atmMachine.getClientCardNumberOrNull(), pin)) {
            atmMachine.increaseAttempt();
            if (atmMachine.getAttemptPinCount() == 5) {
                atmMachine.revertAtm();
            }
            throw new IncorrectPinException();
        }

        atmMachine.setATMState(atmMachine.getHasCorrectPinState());
        atmMachine.setClientAccounts(atmMachine.getClientBankOrNull().getAccounts(atmMachine.getClientCardNumberOrNull()));
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
