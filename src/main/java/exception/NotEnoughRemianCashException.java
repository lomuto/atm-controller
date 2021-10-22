package exception;

public class NotEnoughRemianCashException extends ATMException {
    public NotEnoughRemianCashException() {
        super(new String("Not enough cash remained in ATM, can't withdraw money"));
    }
}
