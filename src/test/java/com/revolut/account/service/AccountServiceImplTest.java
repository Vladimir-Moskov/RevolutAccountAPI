package test.java.com.revolut.account.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import main.java.com.revolut.account.model.Account;
import main.java.com.revolut.account.model.Response;
import main.java.com.revolut.account.model.Transaction;
import main.java.com.revolut.account.service.AccountServiceImpl;

//TODO: Add more useful comments

/*
 * Unit testing for AccountServiceImpl
 * 
 */
public class AccountServiceImplTest {

	private String testOwner1 = "testOwner1";
	private long testID1 = 1000;
	private BigDecimal balance1 = new BigDecimal(100);
	private String testOwner2 = "testOwner2";
	private long testID2 = 2000;
	private AccountServiceImpl service;
	
	@Before
    public void initialize() {
		this.service = new AccountServiceImpl();
		this.service.resetAll();
    }
	
	@Test
	public void getAccountEchoTest(){
		Account account = service.getAccountEcho(testID1);
		assertEquals(account.getId(), testID1);
	}
	
	@Test
	public void addAccountTest(){
		Account accountResponse = addAccount(testID1, testOwner1, balance1);
		
		assertTrue(accountResponse.getId() != testID1);
		assertTrue(accountResponse.getBalance().equals(balance1));
		assertTrue(accountResponse.getOwner().equals(testOwner1));
		assertTrue(accountResponse.getBalance().equals(balance1));
	}
	
	@Test
	public void getAccountTest(){
		Account accountAdd = addAccount(testID1, testOwner1, balance1);
		Account accountGet = (Account) service.getAccount(accountAdd.getId());
		
		assertTrue(accountGet.getId() != testID1);
		assertTrue(accountGet.getOwner().equals(testOwner1));
		assertTrue(accountGet.getBalance().equals(balance1));
		
		assertTrue(accountAdd.getId() == accountGet.getId());
		assertTrue(accountAdd.getOwner().equals(accountGet.getOwner()));
		assertTrue(accountAdd.getBalance().equals(accountGet.getBalance()));
		assertTrue(accountAdd.getCreationDate().equals(accountGet.getCreationDate()));
		assertTrue(accountAdd.getUpdateDate().equals(accountGet.getUpdateDate()));
		assertTrue(accountAdd.getBalance().equals(accountGet.getBalance()));
	}
	
	@Test
	public void getAllTest(){
		List<Account> accounts = service.getAllAccounts();
		assertTrue(accounts.size() == 0);

		Account accountAdd = addAccount(testID1, testOwner1, balance1);
		assertTrue(accountAdd.getId() > 0);
		accounts = service.getAllAccounts();
		assertTrue( "Real size = " + accounts.size(), accounts.size() == 1);
	}
	
	@Test
	public void deleteAccountTest(){
		Response response = service.deleteAccount(testID1);
		assertTrue(response.getMessage().equals(Response.ACCOUNT_DOES_NOT_EXIST));
		assertTrue(!response.getStatus());
		
		Account accountAdd = addAccount(testID1, testOwner1, balance1);
	
		response = service.deleteAccount(accountAdd.getId());
		assertTrue(response.getMessage().equals(Response.ACCOUNT_DELETED_SUCCESSFULLY));
		assertTrue(response.getStatus());
		List<Account> accounts = service.getAllAccounts();
		assertTrue(accounts.size() == 0);
	}
	
	@Test
	public void doTransactionNoAccountsTest(){
		Transaction transaction = new Transaction();
		transaction.setFromAccount(testID1);
		transaction.setToAccount(testID2);
		transaction.setToAccount(testID2);
		transaction.setAmount(balance1);
		Response response = service.doTransaction(transaction);
		assertTrue(response.getMessage().contentEquals(Transaction.ACCOUNT_FROM_TO_DOES_NOT_EXIST));
		assertTrue(!response.getStatus());
	}
	
	@Test
	public void doTransactionTest(){

		Account accountFrom = addAccount(testID1, testOwner1, balance1);
		Account accountTo = addAccount(testID2, testOwner2, new BigDecimal(0));
		
		Transaction transaction = new Transaction();
		transaction.setFromAccount(accountFrom.getId());
		transaction.setToAccount(accountTo.getId());
		transaction.setAmount(balance1);
		
		Response response = service.doTransaction(transaction);
		assertTrue(response.getMessage().equals(Transaction.TRANSACTION_HAS_BEEN_COMPLETED_SUCCESSFULLY));
		assertTrue(response.getStatus());
		
		accountFrom = (Account) service.getAccount(accountFrom.getId());
		accountTo = (Account) service.getAccount(accountTo.getId());
		
		assertTrue(accountFrom.getBalance().equals(new BigDecimal(0)));
		assertTrue(accountTo.getBalance().equals(balance1));
	}
	
	/*
	 * add account local helper function 
	 */
	private Account addAccount(long id, String owner, BigDecimal balance) {
		Account account = new Account();
		account.setOwner(owner);
		account.setBalance(balance);
		account.setId(id);
		
		Object objectResponse = service.addAccount(account);
		assertTrue(objectResponse instanceof  Account);
		return (Account) objectResponse;
	}
}
