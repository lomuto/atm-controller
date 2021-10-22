package test.java;

import main.java.ATMMachine;
import main.java.Account;
import main.java.Bank;
import main.java.exception.ATMException;
import main.java.exception.NoCardException;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static org.junit.Assert.*;

public class ATMMachineTest {
    private Bank bank;
    private String user0CardNumber = "4012888888881881";
    private String user0Pin = "1234";


    @Before
    public void setUp() throws Exception {
        Account account0 = new Account("0", 20);
        Account account1 = new Account("1", 890);
        Account account2 = new Account("2", 290);
        ArrayList<Account> accountList = new ArrayList(Arrays.asList(account0, account1, account2));

        HashMap<String, ArrayList<Account>> bank0Accounts = new HashMap<>();
        bank0Accounts.put(user0CardNumber, accountList);

        HashMap<String, String> bank0pin = new HashMap<>();
        bank0pin.put(user0CardNumber, user0Pin);

        bank = new Bank(bank0Accounts, bank0pin);

        ATMMachine.banks.add(bank);
    }

    @Test
    public void NoCardInserted() throws ATMException {
        ATMMachine atm = new ATMMachine(200);

        Exception exception = assertThrows(NoCardException.class, ()->{
            atm.enterPin("1234");
        });

        assertEquals("Card hasn't been inserted to machine", exception.getMessage());
    }
}