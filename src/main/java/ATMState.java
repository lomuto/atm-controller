import exception.ATMException;

public interface ATMState {
    void insertCard(String cardNumber) throws ATMException;
    void enterPin(String pin) throws ATMException;
    void selectAccount(int index) throws ATMException;
    int getBalance() throws ATMException;
    int withdraw(int amount) throws ATMException;
    int deposit(int amount) throws ATMException;
}
