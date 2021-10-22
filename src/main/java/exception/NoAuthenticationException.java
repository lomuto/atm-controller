package exception;

public class NoAuthenticationException extends ATMException{
    public NoAuthenticationException() {
        super(new String("Enter pin number first"));
    }
}
