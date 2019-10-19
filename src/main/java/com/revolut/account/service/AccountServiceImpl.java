
package main.java.com.revolut.account.service;


import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import main.java.com.revolut.account.model.Account;
import main.java.com.revolut.account.model.Response;
import main.java.com.revolut.account.model.Transaction;


@Path("/account")
@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
public class AccountServiceImpl implements AccountService {

	private static Map<Long, Account> accounts = new HashMap<Long, Account>();
	private static Map<UUID, Transaction> transactions = new HashMap<UUID, Transaction>();
	
	@Override
	@POST
    @Path("/doTransaction")
	public Response doTransaction(Transaction transaction) {
		Response response = new Response();
		transaction.setId(UUID.randomUUID());
		Account accountFrom = accounts.get(transaction.getFromAccount());
		Account accountTo = accounts.get(transaction.getToAccount());
		
		if(accountFrom == null ||  accountTo != null){
			response.setStatus(false);
			response.setMessage("Account From/ Account To does not Exist");
			return response;
		}
		Account accountToLock1 = accountFrom.getId() > accountTo.getId() ? accountFrom : accountTo;
		Account accountToLock2 = accountFrom.getId() > accountTo.getId() ? accountTo : accountFrom;
		
		synchronized (accountToLock1){
			synchronized (accountToLock2){
				if(accountToLock1.isClosed() || accountToLock1.isClosed()) {
					response.setStatus(false);
					response.setMessage("Transaction has been completed successfully");
				}
				if(accountFrom.getBalance().compareTo(transaction.getAmount()) < 0) {
					response.setStatus(false);
					response.setMessage("Account From  does not have anought balance Exist");
					return response;
				}
				else {
					accountFrom.setBalance(accountFrom.getBalance().subtract(transaction.getAmount()));
					accountTo.setBalance(accountTo.getBalance().add(transaction.getAmount()));
				}
			}
		}
		
		response.setStatus(true);
		response.setMessage("Transaction has been completed successfully");
		return response;
	}
	
	@Override
	@POST
    @Path("/add")
	public Response addAccount(Account account) {
		Response response = new Response();
		if(accounts.get(account.getId()) != null){
			response.setStatus(false);
			response.setMessage("Account Already Exists");
			return response;
		}
		accounts.put(account.getId(), account);
		response.setStatus(true);
		response.setMessage("Account created successfully");
		return response;
	}

	@Override
	@GET
    @Path("/{id}/delete")
	public Response deleteAccount(@PathParam("id") long id) {
		Response response = new Response();
		Account account = accounts.get(id);
		if(account == null){
			response.setStatus(false);
			response.setMessage("Account Doesn't Exists");
			return response;
		}
		synchronized (account) {
			account.setClosed(true);
		}
		//accounts.remove(id);
		response.setStatus(true);
		response.setMessage("Account deleted successfully");
		return response;
	}

	@Override
	@GET
	@Path("/{id}/get")
	public Account getAccount(@PathParam("id") long id) {
		return accounts.get(id);
	}
	
	@GET
	@Path("/{id}/getDummy")
	public Account getDummyPerson(@PathParam("id") long id) {
		Account account = new Account();
		account.setBalance(new BigDecimal(0));
		account.setOwner("ownerDummy");
		account.setId(id);
		return account;
	}

	@Override
	@GET
	@Path("/getAll")
	public Account[] getAllAccounts() {
		Set<Long> ids = accounts.keySet();
		Account[] account = new Account[ids.size()];
		int i=0;
		for(Long id : ids){
			account[i] = accounts.get(id);
			i++;
		}
		return account;
	}
	
}
