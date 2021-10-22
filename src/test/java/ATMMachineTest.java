import static org.assertj.core.api.Assertions.*;

import exception.ATMException;
import exception.ATMFullException;
import exception.CardAlreadyInsertedException;
import exception.IncorrectPinException;
import exception.InvalidCardException;
import exception.NoAuthenticationException;
import exception.NoCardException;
import exception.NotEnoughRemianCashException;
import exception.AccountAlreadySelectedException;
import exception.ClientAlreadyAuthenticated;
import exception.AccountNotSelectedException;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

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

        ATMMachine.banks.clear();
        ATMMachine.banks.add(bank);
    }

    /*
        Test whether NoCard stated ATM machine can reject further stated methods
     */
    @Test
    public void NoCardInsertedTest() throws ATMException {
        ATMMachine atm = new ATMMachine(200);

        assertThatThrownBy(() -> {
            atm.enterPin("1234");
        })
                .isInstanceOf(NoCardException.class)
                .hasMessageContaining("Card hasn't been inserted to machine");

        assertThatThrownBy(() -> {
            atm.selectAccount(1);
        })
                .isInstanceOf(NoCardException.class)
                .hasMessageContaining("Card hasn't been inserted to machine");

        assertThatThrownBy(atm::getBalance)
                .isInstanceOf(NoCardException.class)
                .hasMessageContaining("Card hasn't been inserted to machine");

        assertThatThrownBy(() -> {
            atm.withdraw(100);
        })
                .isInstanceOf(NoCardException.class)
                .hasMessageContaining("Card hasn't been inserted to machine");

        assertThatThrownBy(() -> {
            atm.deposit(100);
        })
                .isInstanceOf(NoCardException.class)
                .hasMessageContaining("Card hasn't been inserted to machine");
    }

    /*
        Testing Invalid card: Number of card is not valid for Luhn algorithm
     */
    @Test
    public void InvalidCardNumberTest() {
        ATMMachine atm = new ATMMachine(200);

        assertThatThrownBy(() -> {
            atm.insertCard("8421");
        })
                .isInstanceOf(InvalidCardException.class)
                .hasMessageContaining("Inserted Invalid Card");

        assertThatThrownBy(() -> {
            atm.insertCard("0001");
        })
                .isInstanceOf(InvalidCardException.class)
                .hasMessageContaining("Inserted Invalid Card");
    }

    /*
        Testing Invalid card: Card number is valid for Luhn algorithm but not exist in Bank database
     */
    @Test
    public void ExpiredCardTest() {
        ATMMachine atm = new ATMMachine(200);

        assertThatThrownBy(() -> {
            atm.insertCard("0494546708");
        })
                .isInstanceOf(InvalidCardException.class)
                .hasMessageContaining("Inserted Invalid Card");

        assertThatThrownBy(() -> {
            atm.insertCard("0034085951");
        })
                .isInstanceOf(InvalidCardException.class)
                .hasMessageContaining("Inserted Invalid Card");
    }

    /*
        Testing ATM machine state has been successfully transferred to HasCard state after inserting valid card
     */
    @Test
    public void ValidCardTest() throws ATMException {
        ATMMachine atm = new ATMMachine(200);
        atm.insertCard(user0CardNumber);

        assertThat(atm.getAtmState()).isEqualTo(atm.getHasCardState());
        assertThat(atm.getClientCardNumberOrNull()).isEqualTo(user0CardNumber);
        assertThat(atm.getClientBankOrNull()).isNotEqualTo(null);
    }

    /*
        Test DuplicatedCardInsertion situation
     */
    @Test
    public void DuplicatedCardInsertionTest() throws ATMException {
        ATMMachine atm = new ATMMachine(200);
        atm.insertCard(user0CardNumber);

        assertThatThrownBy(() -> {
            atm.insertCard("0034085951");
        })
                .isInstanceOf(CardAlreadyInsertedException.class)
                .hasMessageContaining("Card already inserted");

        assertThat(atm.getAtmState()).isEqualTo(atm.getNoCardState());
        assertThat(atm.getClientAccountsOrNull()).isEqualTo(null);
        assertThat(atm.getClientBankOrNull()).isEqualTo(null);
    }

    /*
        Testing entering wrong pin number 5 times.
     */
    @Test
    public void InvalidPinTest() throws ATMException {
        ATMMachine atm = new ATMMachine(200);
        atm.insertCard(user0CardNumber);

        for (int i = 0; i < 4; i++) {
            assertThatThrownBy(() -> {
                atm.enterPin("0000");
            })
                    .isInstanceOf(IncorrectPinException.class)
                    .hasMessageContaining("Entered pin number is incorrect");

            assertThat(atm.getAtmState()).isEqualTo(atm.getHasCardState());
        }

        assertThatThrownBy(() -> {
            atm.enterPin("0000");
        })
                .isInstanceOf(IncorrectPinException.class)
                .hasMessageContaining("Entered pin number is incorrect");

        assertThat(atm.getAtmState()).isEqualTo(atm.getNoCardState());
        assertThat(atm.getClientAccountsOrNull()).isEqualTo(null);
        assertThat(atm.getClientBankOrNull()).isEqualTo(null);
    }

    /*
        Test ATM machine state and accounts after entering valid pin number
     */
    @Test
    public void ValidPinTest() throws ATMException {
        ATMMachine atm = new ATMMachine(200);
        atm.insertCard(user0CardNumber);
        atm.enterPin(user0Pin);

        assertThat(atm.getAtmState()).isEqualTo(atm.getHasCorrectPinState());
        assertThat(atm.getClientAccountsOrNull()).isNotEqualTo(null);
    }

    /*
        Test calling accounts seleect, see balance, depost, withdraw before authenticated
     */
    @Test
    public void NotAuthenticatedTest() throws ATMException {
        {
            ATMMachine atm = new ATMMachine(200);
            atm.insertCard(user0CardNumber);

            assertThatThrownBy(() -> {
                atm.selectAccount(0);
            })
                    .isInstanceOf(NoAuthenticationException.class)
                    .hasMessageContaining("Enter pin number first");

            assertThat(atm.getAtmState()).isEqualTo(atm.getNoCardState());
            assertThat(atm.getClientAccountsOrNull()).isEqualTo(null);
            assertThat(atm.getClientBankOrNull()).isEqualTo(null);
        }

        {
            ATMMachine atm = new ATMMachine(200);
            atm.insertCard(user0CardNumber);

            assertThatThrownBy(() -> {
                atm.getBalance();
            })
                    .isInstanceOf(NoAuthenticationException.class)
                    .hasMessageContaining("Enter pin number first");

            assertThat(atm.getAtmState()).isEqualTo(atm.getNoCardState());
            assertThat(atm.getClientAccountsOrNull()).isEqualTo(null);
            assertThat(atm.getClientBankOrNull()).isEqualTo(null);
        }

        {
            ATMMachine atm = new ATMMachine(200);
            atm.insertCard(user0CardNumber);

            assertThatThrownBy(() -> {
                atm.deposit(100);
            })
                    .isInstanceOf(NoAuthenticationException.class)
                    .hasMessageContaining("Enter pin number first");

            assertThat(atm.getAtmState()).isEqualTo(atm.getNoCardState());
            assertThat(atm.getClientAccountsOrNull()).isEqualTo(null);
            assertThat(atm.getClientBankOrNull()).isEqualTo(null);
        }
        {
            ATMMachine atm = new ATMMachine(200);
            atm.insertCard(user0CardNumber);

            assertThatThrownBy(() -> {
                atm.withdraw(100);
            })
                    .isInstanceOf(NoAuthenticationException.class)
                    .hasMessageContaining("Enter pin number first");

            assertThat(atm.getAtmState()).isEqualTo(atm.getNoCardState());
            assertThat(atm.getClientAccountsOrNull()).isEqualTo(null);
            assertThat(atm.getClientBankOrNull()).isEqualTo(null);
        }
    }

    /*
        Testing select account
        Followed by precondition of UI, there won't be a IndexOutOfBounds Exception
     */
    @Test
    public void AccountSelectTest() throws ATMException {
        {
            ATMMachine atm = new ATMMachine(200);
            atm.insertCard(user0CardNumber);
            atm.enterPin(user0Pin);
            atm.selectAccount(0);
            assertThat(atm.getSelectedAccountOrNull()).isNotEqualTo(null);
        }
        {
            ATMMachine atm = new ATMMachine(200);
            atm.insertCard(user0CardNumber);
            atm.enterPin(user0Pin);
            atm.selectAccount(1);
            assertThat(atm.getSelectedAccountOrNull()).isNotEqualTo(null);
        }
        {
            ATMMachine atm = new ATMMachine(200);
            atm.insertCard(user0CardNumber);
            atm.enterPin(user0Pin);
            atm.selectAccount(2);
            assertThat(atm.getSelectedAccountOrNull()).isNotEqualTo(null);
        }
    }

    /*
        Check see balance
     */
    @Test
    public void SeeBalanceTest() throws ATMException {
        {
            ATMMachine atm = new ATMMachine(200);
            atm.insertCard(user0CardNumber);
            atm.enterPin(user0Pin);
            atm.selectAccount(0);
            assertThat(atm.getBalance()).isEqualTo(20);
        }
        {
            ATMMachine atm = new ATMMachine(200);
            atm.insertCard(user0CardNumber);
            atm.enterPin(user0Pin);
            atm.selectAccount(1);
            assertThat(atm.getBalance()).isEqualTo(890);
        }
        {
            ATMMachine atm = new ATMMachine(200);
            atm.insertCard(user0CardNumber);
            atm.enterPin(user0Pin);
            atm.selectAccount(2);
            assertThat(atm.getBalance()).isEqualTo(290);
        }
    }

    /*
        Test deposit
     */
    @Test
    public void DepositTest() throws ATMException {
        {
            ATMMachine atm = new ATMMachine(200);
            atm.insertCard(user0CardNumber);
            atm.enterPin(user0Pin);
            atm.selectAccount(0);
            atm.deposit(100);
            assertThat(atm.getRemainCash()).isEqualTo(300);

            atm.insertCard(user0CardNumber);
            atm.enterPin(user0Pin);
            atm.selectAccount(0);
            assertThat(atm.getBalance()).isEqualTo(120);
        }
        {
            ATMMachine atm = new ATMMachine(200);
            atm.insertCard(user0CardNumber);
            atm.enterPin(user0Pin);
            atm.selectAccount(1);
            atm.deposit(100);
            assertThat(atm.getRemainCash()).isEqualTo(300);

            atm.insertCard(user0CardNumber);
            atm.enterPin(user0Pin);
            atm.selectAccount(1);
            assertThat(atm.getBalance()).isEqualTo(990);
        }
        {
            ATMMachine atm = new ATMMachine(200);
            atm.insertCard(user0CardNumber);
            atm.enterPin(user0Pin);
            atm.selectAccount(2);
            atm.deposit(100);
            assertThat(atm.getRemainCash()).isEqualTo(300);

            atm.insertCard(user0CardNumber);
            atm.enterPin(user0Pin);
            atm.selectAccount(2);
            assertThat(atm.getBalance()).isEqualTo(390);
        }
    }

    /*
        Test withdraw
     */
    @Test
    public void WithdrawTest() throws ATMException {
        {
            ATMMachine atm = new ATMMachine(200);
            atm.insertCard(user0CardNumber);
            atm.enterPin(user0Pin);
            atm.selectAccount(0);
            atm.withdraw(10);
            assertThat(atm.getRemainCash()).isEqualTo(190);

            atm.insertCard(user0CardNumber);
            atm.enterPin(user0Pin);
            atm.selectAccount(0);
            assertThat(atm.getBalance()).isEqualTo(20 - 10 - (int) (10 * atm.getFee()));
        }
        {
            ATMMachine atm = new ATMMachine(200);
            atm.insertCard(user0CardNumber);
            atm.enterPin(user0Pin);
            atm.selectAccount(1);
            int withdrawn = atm.withdraw(100);
            assertThat(withdrawn).isEqualTo(100 + (int) (100 * atm.getFee()));
            assertThat(atm.getRemainCash()).isEqualTo(100);

            atm.insertCard(user0CardNumber);
            atm.enterPin(user0Pin);
            atm.selectAccount(1);
            assertThat(atm.getBalance()).isEqualTo(890 - withdrawn);
        }
        {
            ATMMachine atm = new ATMMachine(200);
            atm.insertCard(user0CardNumber);
            atm.enterPin(user0Pin);
            atm.selectAccount(2);
            atm.withdraw(100);
            assertThat(atm.getRemainCash()).isEqualTo(100);

            atm.insertCard(user0CardNumber);
            atm.enterPin(user0Pin);
            atm.selectAccount(2);
            assertThat(atm.getBalance()).isEqualTo(190 - (int) (100 * atm.getFee()));
        }
    }

    /*
        Testing depositing cash would yield remain cash of atm becomes more than its capacity
     */
    @Test
    public void ATMFullTest() throws ATMException {
        ATMMachine atm = new ATMMachine(ATMMachine.getMaxCash() - 100);
        atm.insertCard(user0CardNumber);
        atm.enterPin(user0Pin);
        atm.selectAccount(1);

        assertThatThrownBy(() -> {
            atm.deposit(101);
        })
                .isInstanceOf(ATMFullException.class)
                .hasMessageContaining("ATM is full, can't deposit money");

        assertThat(atm.getAtmState()).isEqualTo(atm.getNoCardState());
        assertThat(atm.getClientAccountsOrNull()).isEqualTo(null);
        assertThat(atm.getClientBankOrNull()).isEqualTo(null);
    }

    /*
        Testing depositing cash would yield remain cash of atm becomes more than its capacity
     */
    @Test
    public void NotEnoughRemainCashTest() throws ATMException {
        ATMMachine atm = new ATMMachine(0);
        atm.insertCard(user0CardNumber);
        atm.enterPin(user0Pin);
        atm.selectAccount(1);

        assertThatThrownBy(() -> {
            atm.withdraw(1);
        })
                .isInstanceOf(NotEnoughRemianCashException.class)
                .hasMessageContaining("Not enough cash remained in ATM, can't withdraw money");

        assertThat(atm.getAtmState()).isEqualTo(atm.getNoCardState());
        assertThat(atm.getClientAccountsOrNull()).isEqualTo(null);
        assertThat(atm.getClientBankOrNull()).isEqualTo(null);
    }

    @Test
    public void AccountAlreadySelectedTest() throws ATMException {
        ATMMachine atm = new ATMMachine(100);
        atm.insertCard(user0CardNumber);
        atm.enterPin(user0Pin);
        atm.selectAccount(1);

        assertThatThrownBy(() -> {
            atm.selectAccount(2);
        })
                .isInstanceOf(AccountAlreadySelectedException.class)
                .hasMessageContaining("Account has been already selected");

        assertThat(atm.getAtmState()).isEqualTo(atm.getAccountSelectedState());
        assertThat(atm.getClientAccountsOrNull()).isNotEqualTo(null);
        assertThat(atm.getClientBankOrNull()).isNotEqualTo(null);
    }

    @Test
    public void ClientAlreadyAuthTest() throws ATMException {
        {
            ATMMachine atm = new ATMMachine(100);
            atm.insertCard(user0CardNumber);
            atm.enterPin(user0Pin);

            assertThatThrownBy(() -> {
                atm.enterPin("1010");
            })
                    .isInstanceOf(ClientAlreadyAuthenticated.class)
                    .hasMessageContaining("Client has been already authenticated");

            assertThat(atm.getAtmState()).isEqualTo(atm.getHasCorrectPinState());
            assertThat(atm.getClientAccountsOrNull()).isNotEqualTo(null);
            assertThat(atm.getClientBankOrNull()).isNotEqualTo(null);
        }
        {
            ATMMachine atm = new ATMMachine(100);
            atm.insertCard(user0CardNumber);
            atm.enterPin(user0Pin);
            atm.selectAccount(2);

            assertThatThrownBy(() -> {
                atm.enterPin("1010");
            })
                    .isInstanceOf(ClientAlreadyAuthenticated.class)
                    .hasMessageContaining("Client has been already authenticated");

            assertThat(atm.getAtmState()).isEqualTo(atm.getAccountSelectedState());
            assertThat(atm.getClientAccountsOrNull()).isNotEqualTo(null);
            assertThat(atm.getClientBankOrNull()).isNotEqualTo(null);
        }
    }

    @Test
    public void AccountNotSelectedTest() throws ATMException {
        {
            ATMMachine atm = new ATMMachine(100);
            atm.insertCard(user0CardNumber);
            atm.enterPin(user0Pin);

            assertThatThrownBy(() -> {
                atm.getBalance();
            })
                    .isInstanceOf(AccountNotSelectedException.class)
                    .hasMessageContaining("Account has not been selected yet");

            assertThat(atm.getAtmState()).isEqualTo(atm.getNoCardState());
            assertThat(atm.getClientAccountsOrNull()).isEqualTo(null);
            assertThat(atm.getClientBankOrNull()).isEqualTo(null);
        }
        {
            ATMMachine atm = new ATMMachine(100);
            atm.insertCard(user0CardNumber);
            atm.enterPin(user0Pin);

            assertThatThrownBy(() -> {
                atm.withdraw(10);
            })
                    .isInstanceOf(AccountNotSelectedException.class)
                    .hasMessageContaining("Account has not been selected yet");

            assertThat(atm.getAtmState()).isEqualTo(atm.getNoCardState());
            assertThat(atm.getClientAccountsOrNull()).isEqualTo(null);
            assertThat(atm.getClientBankOrNull()).isEqualTo(null);
        }
        {
            ATMMachine atm = new ATMMachine(100);
            atm.insertCard(user0CardNumber);
            atm.enterPin(user0Pin);

            assertThatThrownBy(() -> {
                atm.deposit(10);
            })
                    .isInstanceOf(AccountNotSelectedException.class)
                    .hasMessageContaining("Account has not been selected yet");

            assertThat(atm.getAtmState()).isEqualTo(atm.getNoCardState());
            assertThat(atm.getClientAccountsOrNull()).isEqualTo(null);
            assertThat(atm.getClientBankOrNull()).isEqualTo(null);
        }
    }
}