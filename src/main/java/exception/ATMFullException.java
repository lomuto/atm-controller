package main.java.exception;

public class ATMFullException extends ATMException {
    public ATMFullException() {
        super(new String("ATM is full, can't deposit money"));
    }
}
