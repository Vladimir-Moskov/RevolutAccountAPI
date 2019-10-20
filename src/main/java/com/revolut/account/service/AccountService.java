
package main.java.com.revolut.account.service;

import java.util.List;

import main.java.com.revolut.account.model.Account;
import main.java.com.revolut.account.model.Response;
import main.java.com.revolut.account.model.Transaction;

public interface AccountService {

	public Object addAccount(Account account);
	
	public Response deleteAccount(long id);
	
	public Account getAccount(long id);
	
	public List<Account> getAllAccounts();
	
	public Response doTransaction(Transaction transaction);

}
