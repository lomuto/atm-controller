package exception;

public class InvalidCardException extends ATMException{
    public InvalidCardException() {
        super(new String("Inserted Invalid Card"));
    }
}
