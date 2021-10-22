package com.lomuto.app;

import com.lomuto.ATMMachine;
import com.lomuto.Account;
import com.lomuto.Bank;
import com.lomuto.exception.ATMException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Program {
    public static void main(String[] args) {
        Account account0 = new Account("0", 20);
        Account account1 = new Account("1", 890);
        Account account2 = new Account("2", 290);
        ArrayList<Account> accountList = new ArrayList(Arrays.asList(account0, account1, account2));
        String user0CardNumber = "4012888888881881";
        String user0Pin = "1234";

        HashMap<String,ArrayList<Account>> bank0Accounts = new HashMap<>();
        bank0Accounts.put(user0CardNumber, accountList);
        HashMap<String, String> bank0pin = new HashMap<>();
        bank0pin.put(user0CardNumber, user0Pin);

        Bank bank0 = new Bank(bank0Accounts, bank0pin);

        ATMMachine.banks.add(bank0);

        // Empty atm test
        try {
            ATMMachine atm0 = new ATMMachine(0);
            atm0.insertCard(user0CardNumber);
            atm0.enterPin("1234");
            atm0.selectAccount(1);
            System.out.println(atm0.getBalance());
        } catch (ATMException e) {
            System.out.println(e);
            assert (true);
        }
    }
}
