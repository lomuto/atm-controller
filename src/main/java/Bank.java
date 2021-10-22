import java.util.ArrayList;
import java.util.HashMap;

public class Bank {
    private final HashMap<String, ArrayList<Account>> accountsByCardNumber;
    private final HashMap<String, String> hashedPinNumberByCardNumber;

    public Bank(HashMap<String, ArrayList<Account>> accountsByCardNumber, HashMap<String, String> hashedPinNumberByCardNumber) {
        this.accountsByCardNumber = accountsByCardNumber;
        this.hashedPinNumberByCardNumber = hashedPinNumberByCardNumber;
    }

    public ArrayList<Account> getAccounts(String cardNumber) {
        return accountsByCardNumber.get(cardNumber);
    }

    public boolean hasCard(String cardNumber) {
        return accountsByCardNumber.containsKey(cardNumber);
    }

    public boolean isPinValid(String cardNumber, String pin) {
        // Assume that pin has been hashed
        return hashedPinNumberByCardNumber.get(cardNumber).equals(pin);
    }
}
