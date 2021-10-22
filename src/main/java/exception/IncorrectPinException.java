package main.java.exception;

public class IncorrectPinException extends ATMException{
    public IncorrectPinException() {
        super(new String("Entered pin number is incorrect"));
    }
}
