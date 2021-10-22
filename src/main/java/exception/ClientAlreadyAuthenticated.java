package exception;

public class ClientAlreadyAuthenticated extends ATMException{
    public ClientAlreadyAuthenticated() {
        super(new String("Client has been already authenticated"));
    }
}
