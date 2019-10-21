package test.java.com.revolut.account;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.uri.UriTemplate;

import main.java.com.revolut.account.model.Account;
import main.java.com.revolut.account.model.Response;
import main.java.com.revolut.account.model.Transaction;

/*
 // keep it for not Jersey implementation
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
*/

//TODO: Add more useful comments

/*
 * Integration tests for AccountServiceImpl
 * 
 */
//@Ignore
public class RevolutAccountAPITest {
	final private static String rootUrl = "http://localhost:8080/RevolutAccountAPI/account/";
	final private static String contentType =  "application/json";
	
	private String testOwner1 = "testOwner1";
	private long testID1 = 1000;
	private BigDecimal balance1 = new BigDecimal(100);
	private String testOwner2 = "testOwner2";
	private long testID2 = 2000;

	private ClientResponse createRequest(String service, String type, String urlVar, Object postArg) {
		UriTemplate uriTemplate = new UriTemplate(rootUrl + service);
		String serviceUrl = uriTemplate.createURI(urlVar);
		Client client = Client.create();
		WebResource resource = client.resource(serviceUrl);
		ClientResponse response = null;
		
		if(type == "DELETE")		
			response = resource.accept(contentType).delete(ClientResponse.class);
		else if(type == "POST")		
			response = resource.accept(contentType).post(ClientResponse.class, postArg);	
		else //(type == "GET")		
			response = resource.accept(contentType).get(ClientResponse.class);
		
		if (response.getStatus() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
		}
		return response;
	}
	  
	private Account addAccount(long id, String owner, BigDecimal balance) {
		Account account = new Account();
		account.setOwner(owner);
		account.setBalance(balance);
		account.setId(id);
		
		ClientResponse response = createRequest("add", "POST", "", account);

		Account accountResponse = response.getEntity(Account.class);
		return accountResponse;
	}
	
	@Before
    public void initialize() {
		ClientResponse response = createRequest("resetAll", "DELETE", "", null);
    }
	
	@Test
	public void getAccountEchoReturns200CheckIdTest() {
		ClientResponse response = createRequest("{id}/getAccountEcho", "GET",  Long.toString(testID1), null);
		Account outputAccount = response.getEntity(Account.class);

		System.out.println("<<<< Generating JSON Output >>>");
		System.out.println(outputAccount);
		assertEquals(outputAccount.getId(), testID1);
	}
	
	@Test
	public void addAccountReturns200WithCheckResultTest() {
		Account resAccount = addAccount(testID1, testOwner1, balance1);
		System.out.println("<<<< Generating JSON Output >>>");
		System.out.println(resAccount);
		
		assertTrue(resAccount.getId() > 0);
		assertTrue(resAccount.getId() != testID1);
		assertTrue(resAccount.getBalance().equals(balance1));
		assertTrue(resAccount.getOwner().equals(testOwner1));
		assertTrue(resAccount.getBalance().equals(balance1));
	}
	
	@Test
	public void getAccountReturns200WithCheckResultTest() {
		Account accountAdd = addAccount(testID1, testOwner1, balance1);
		Account accountGet = createRequest("{id}/get", "GET",  Long.toString(accountAdd.getId()), null).getEntity(Account.class);
		System.out.println("<<<< Generating JSON Output >>>");
		System.out.println(accountGet);
		
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
	public void getAllAccountReturns200WithEmptyListTest() {
		ClientResponse response = createRequest("getAll", "GET", "", null);
		GenericType<List<Account>> gType = new GenericType<List<Account>>() {	};
		List<Account> outputAccount = response.getEntity(gType);

		
		System.out.println("<<<< Generating JSON Output >>>");
		System.out.println(outputAccount);
		assertEquals(outputAccount.size(), 0);
	}
	
	@Test
	public void getAllAccountReturns200WithListTest() {
		Account accountAdd = addAccount(testID1, testOwner1, balance1);
		ClientResponse response = createRequest("getAll", "GET", "", null);
		GenericType<List<Account>> gType = new GenericType<List<Account>>() {	};
		List<Account> outputAccount = response.getEntity(gType);

		System.out.println("<<<< Generating JSON Output >>>");
		System.out.println(outputAccount);
		assertEquals(outputAccount.size(), 1);
	}
	
	@Test
	public void deleteAccountReturns200WithNoAccountTest() {
		ClientResponse response = createRequest("{id}/delete", "DELETE", Long.toString(testID1), null);
		Response delResponse = response.getEntity(Response.class);
		

		System.out.println("<<<< Generating JSON Output >>>");
		System.out.println(delResponse);
		assertTrue(delResponse.getMessage().equals(Response.ACCOUNT_DOES_NOT_EXIST));
		assertTrue(!delResponse.getStatus());
	}
	
	@Test
	public void deleteAccountReturns200WithAccountTest() {
		Account accountAdd = addAccount(testID1, testOwner1, balance1);
		Account accountGet = createRequest("{id}/get", "GET",  Long.toString(accountAdd.getId()), null).getEntity(Account.class);
		
		ClientResponse response = createRequest("{id}/delete", "DELETE", Long.toString(accountGet.getId()), null);
		Response delResponse = response.getEntity(Response.class);
		

		System.out.println("<<<< Generating JSON Output >>>");
		System.out.println(delResponse);
		assertTrue(delResponse.getMessage().equals(Response.ACCOUNT_DELETED_SUCCESSFULLY));
		assertTrue(delResponse.getStatus());
	}

	@Test
	public void doTransactionNoAccountsTestReturns200WithdNoAccountTest() {
		Transaction transaction = new Transaction();
		transaction.setFromAccount(testID1);
		transaction.setToAccount(testID2);
		transaction.setAmount(balance1);
		ClientResponse response = createRequest("doTransaction", "POST", "", transaction);
		Response tranResponse = response.getEntity(Response.class);
		

		System.out.println("<<<< Generating JSON Output >>>");
		System.out.println(tranResponse);
		assertTrue(tranResponse.getMessage().contentEquals(Transaction.ACCOUNT_FROM_TO_DOES_NOT_EXIST));
		assertTrue(!tranResponse.getStatus());
	}
	
	@Test
	public void doTransactionAccountsTestReturns200WithAccountTest() {
		Account accountFrom = addAccount(testID1, testOwner1, balance1);
		Account accountTo = addAccount(testID2, testOwner2, new BigDecimal(0));
		
		accountFrom = createRequest("{id}/get", "GET",  Long.toString(accountFrom.getId()), null).getEntity(Account.class);
		accountTo = createRequest("{id}/get", "GET",  Long.toString(accountTo.getId()), null).getEntity(Account.class);;

		Transaction transaction = new Transaction();
		transaction.setFromAccount(accountFrom.getId());
		transaction.setToAccount(accountTo.getId());
		transaction.setAmount(balance1);
		ClientResponse response = createRequest("doTransaction", "POST", "", transaction);
		Response tranResponse = response.getEntity(Response.class);
		

		System.out.println("<<<< Generating JSON Output >>>");
		System.out.println(tranResponse);
		assertTrue(tranResponse.getMessage().contentEquals(Transaction.TRANSACTION_HAS_BEEN_COMPLETED_SUCCESSFULLY));
		assertTrue(tranResponse.getStatus());
		
		accountFrom = createRequest("{id}/get", "GET",  Long.toString(accountFrom.getId()), null).getEntity(Account.class);
		accountTo = createRequest("{id}/get", "GET",  Long.toString(accountTo.getId()), null).getEntity(Account.class);;

		assertTrue(accountFrom.getBalance().equals(new BigDecimal(0)));
		assertTrue(accountTo.getBalance().equals(balance1));
	}
/*
  // keep it for not Jersey implementation
	@Test
	public void getAccountEcho_returns_200_with_expected_id() {

		// Specify the base URL to the RESTful web service
		RestAssured.baseURI = "http://localhost:8080/RevolutAccountAPI";
		// Get the RequestSpecification of the request that you want to sent
		// to the server. The server is specified by the BaseURI that we have
		// specified in the above step.
		RequestSpecification httpRequest = RestAssured.given();
		httpRequest.accept(ContentType.JSON);
		httpRequest.contentType(ContentType.JSON);
		// Make a request to the server by specifying the method Type and the method
		// URL.
		// This will return the Response from the server. Store the response in a
		// variable.
		Response response = httpRequest.request(Method.GET, "/account/{id}/getAccountEcho", 12);

		// Now let us print the body of the message to see what response
		// we have recieved from the server
		String responseBody = response.getBody().asString();
		System.out.println("Response Body is =>  " + responseBody);

	}
*/
	

}
