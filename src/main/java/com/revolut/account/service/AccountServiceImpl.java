
package main.java.com.revolut.account.service;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import main.java.com.revolut.account.model.Account;
import main.java.com.revolut.account.model.Response;
import main.java.com.revolut.account.model.ResponseAccount;
import main.java.com.revolut.account.model.Transaction;

//TODO: use javax.ws.rs.core.Response instead of  main.java.com.revolut.account.model.Response
/*
   if(uuid == null || uuid.trim().length() == 0) {
        return Response.serverError().entity("UUID cannot be blank").build();
    }
    Entity entity = service.getById(uuid);
    if(entity == null) {
        return Response.status(Response.Status.NOT_FOUND).entity("Entity not found for UUID: " + uuid).build();
    }
    String json = //convert entity to json
    return Response.ok(json, MediaType.APPLICATION_JSON).build();

 */

//TODO: add try-catch and proper exception handling

@Path("/account")
@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
public class AccountServiceImpl implements AccountService {
	
	//TODO: implement real-life logger with parameterized file name and level 
	private static Logger LOGGER = Logger.getLogger(AccountServiceImpl.class.getName() + "Logger");
	
	private static Map<Long, Account> accounts = new HashMap<Long, Account>();
	private static Map<UUID, Transaction> transactions = new HashMap<UUID, Transaction>();
	
	private static Object addAccountLock = new Object();
	
	static {
		FileHandler fileTxt;
		try {
			fileTxt = new FileHandler("AccountServiceAPI.log");
	    	SimpleFormatter formatterTxt = new SimpleFormatter();
	    	fileTxt.setFormatter(formatterTxt);
	    	LOGGER.addHandler(fileTxt);
		} catch (Exception e) {
			LOGGER.warning("Was not able to set log file: AccountService.log");
		}
		finally {
			 LOGGER.info("INITIALIZED Logger Name: "+ LOGGER.getName());
		}
	}
	
	@Override
	@POST
    @Path("/doTransaction")
	public Response doTransaction(Transaction transaction) {
		LOGGER.info("POST - doTransaction");
		LOGGER.info(" Request Payload: ");
		LOGGER.info(transaction.toString());
		LOGGER.info(" Responce Payload: ");
		
		Response response = new Response();
		if(transaction.getAmount().compareTo(new BigDecimal(0)) != 1){
			response.setStatus(false);
			response.setMessage(Transaction.AMOUNT_SHOULD_BE_GREATER_THEN_0);
			LOGGER.info(response.toString());
			return response;
		}
		
		if(transaction.getFromAccount() == transaction.getToAccount()){
			response.setStatus(false);
			response.setMessage(Transaction.ACCOUNT_FROM_TO_ARE_SAME);
			LOGGER.info(response.toString());
			return response;
		}
		
		transaction.setId(UUID.randomUUID());
		
		Account accountFrom = accounts.get(transaction.getFromAccount());
		Account accountTo = accounts.get(transaction.getToAccount());
		
		if(accountFrom == null ||  accountTo == null){
			response.setStatus(false);
			response.setMessage(Transaction.ACCOUNT_FROM_TO_DOES_NOT_EXIST);
			LOGGER.info(response.toString());
			return response;
		}
		Account accountToLock1 = accountFrom.getId() > accountTo.getId() ? accountFrom : accountTo;
		Account accountToLock2 = accountFrom.getId() > accountTo.getId() ? accountTo : accountFrom;
		
		synchronized (accountToLock1){
			synchronized (accountToLock2){
				if(accountToLock1.isClosed() || accountToLock1.isClosed()) {
					response.setStatus(false);
					response.setMessage(Transaction.ACCOUNT_FROM_TO_IS_CLOSED);
					LOGGER.info(response.toString());
					return response;
				}
				if(accountFrom.getBalance().compareTo(transaction.getAmount()) < 0) {
					response.setStatus(false);
					response.setMessage(Transaction.ACCOUNT_FROM_DOES_NOT_HAVE_ENOUGHT_BALANCE);
					LOGGER.info(response.toString());
					return response;
				}
				else {
					accountFrom.setBalance(accountFrom.getBalance().subtract(transaction.getAmount()));
					accountTo.setBalance(accountTo.getBalance().add(transaction.getAmount()));
				}
			}
		}
		
		response.setStatus(true);
		response.setMessage(Transaction.TRANSACTION_HAS_BEEN_COMPLETED_SUCCESSFULLY);
		LOGGER.info(" Responce Payload: ");
		return response;
	}
	
	@Override
	@POST
    @Path("/add")
	public Object addAccount(Account account) {
		Response response = new Response();
		if(account.getId() != 0 && accounts.get(account.getId()) != null){
			response.setStatus(false);
			response.setMessage("Account Already Exists");
			return response;
		}
		
		// TODO: implement synchronization better 
		synchronized (addAccountLock){
			//TODO: map.size() - is not good way to generate new ID
			account.setId(Long.valueOf(accounts.size() + 1));
			accounts.put(account.getId(), account);
		}
		//response.setAccount(account);
		response.setStatus(true);
		response.setMessage("Account created successfully");
		return account;
	}

	//TODO: implement account update - PUT
	
	@Override
	@DELETE
    @Path("/{id}/delete")
	public Response deleteAccount(@PathParam("id") long id) {
		Response response = new Response();
		Account account = accounts.get(id);
		if(account == null){
			response.setStatus(false);
			response.setMessage(Response.ACCOUNT_DOES_NOT_EXIST);
			return response;
		}
		synchronized (account) {
			account.setClosed(true);
		}
		//accounts.remove(id);
		response.setStatus(true);
		response.setMessage(Response.ACCOUNT_DELETED_SUCCESSFULLY);
		return response;
	}

	@Override
	@GET
	@Path("/{id}/get")
	public Account getAccount(@PathParam("id") long id) {
		return accounts.get(id);
	}

	//TODO: implement paging and limited result
	@Override
	@GET
	@Path("/getAll")
	public List<Account> getAllAccounts() {
		Set<Long> ids = accounts.keySet();
		List<Account> accountList = new ArrayList<Account>(ids.size());
		for(Long id : ids){
			Account accountTemp = accounts.get(id);
			if(!accountTemp.isClosed()) {
				accountList.add(accounts.get(id));
			}
		}
		((ArrayList<Account>) accountList).trimToSize();
		return accountList;
	}
	
	//For testing purpose only - verify is server working properly
	@GET
	@Path("/{id}/getAccountEcho")
	public Account getAccountEcho(@PathParam("id") long id) {
		Account account = new Account();
		account.setBalance(new BigDecimal(0));
		account.setOwner("AccountEcho");
		account.setId(id);
		return account;
	}
	
	
	//For testing purpose only to clean in memory DB
	@DELETE
	@Path("/resetAll")
	public Response resetAll() {
		// this operation does not need to by thread safe, it is not
		//use case to use some where except integration tests from RevolutAccountAPITest
		accounts.clear(); 
		transactions.clear(); 
		
		Response response = new Response();
		response.setStatus(true);
		response.setMessage("In Memory DB reseted successfully");
		return response;
	}
	
}
