package main.java.exception;

public class InvalidCardException extends ATMException{
    public InvalidCardException() {
        super(new String("Inserted Card is Invalid"));
    }
}
