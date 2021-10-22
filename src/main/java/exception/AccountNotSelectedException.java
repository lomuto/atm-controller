package exception;

public class AccountNotSelectedException extends ATMException{
    public AccountNotSelectedException() {
        super(new String("Account has not been selected yet"));
    }
}
