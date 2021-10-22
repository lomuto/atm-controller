package exception;

public class NoCardException extends ATMException{
    public NoCardException() {
        super("Card hasn't been inserted to machine");
    }
}
