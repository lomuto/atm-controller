import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    private static Thread getThread(ATMMachine atmMachine) {
        return new Thread(() -> {
            System.out.printf("%s Thread starts%s", atmMachine.getClass().getName(), System.lineSeparator());
            while(true) {
                // waiting for stream
                ;
            }
        });
    }
    public static void main(String[] args) {
        ATMMachine atm0 = new ATMMachine();
        ATMMachine atm1 = new ATMMachine();
        ATMMachine atm2 = new ATMMachine();

        ExecutorService threadPool = Executors.newFixedThreadPool(3);
        threadPool.submit(getThread(atm0));
        threadPool.submit(getThread(atm1));
        threadPool.submit(getThread(atm2));
    }
}
