package test.java.com.revolut.account;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import javax.ws.rs.core.MediaType;

import org.junit.Test;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.uri.UriTemplate;

import main.java.com.revolut.account.model.Account;
import main.java.com.revolut.account.model.ResponseAccount;

/*
 // keep it for not Jersey implementation
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
*/

/*
 * Integration tests for AccountServiceImpl
 * 
 */
public class RevolutAccountAPITest {
	final private static String rootUrl = "http://localhost:8080/RevolutAccountAPI/account/";
	final private static String contentType =  "application/json";
	
	@Test
	public void getAccountEcho_returns_200_with_expected_id() {
		UriTemplate t = new UriTemplate(rootUrl + "{id}/getAccountEcho");
		long idTest = 11;

		Client client = Client.create();
		WebResource resource = client.resource(t.createURI(Long.toString(idTest)));
		ClientResponse response = resource.accept(contentType).get(ClientResponse.class);
		if (response.getStatus() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
		}

		Account outputAccount = response.getEntity(Account.class);

		System.out.println("<<<< Generating JSON Output >>>");
		System.out.println(outputAccount);
		assertEquals(outputAccount.getId(), idTest);
		
	}
	
	@Test
	public void getAllAccount_returns_200_with_expected_empty_list() {
		UriTemplate t = new UriTemplate(rootUrl + "getAll");

		Client client = Client.create();
		WebResource resource = client.resource(t.createURI());
		ClientResponse response = resource.accept(contentType).get(ClientResponse.class);
		if (response.getStatus() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
		}

		Account[] outputAccount = response.getEntity(Account[].class);

		System.out.println("<<<< Generating JSON Output >>>");
		System.out.println(outputAccount);
		assertEquals(outputAccount.length, 0);
	}
	
	@Test
	public void addAccount_returns_200_with_expected_result() {
		UriTemplate t = new UriTemplate(rootUrl + "add");

		Client client = Client.create();
		WebResource resource = client.resource(t.createURI());
		Account reqAccount = new Account();
		String testOwner = "testOwner";
		reqAccount.setOwner(testOwner);
		ClientResponse response = resource.accept(contentType).post(ClientResponse.class, reqAccount);
		if (response.getStatus() != 201) {
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
		}

		ResponseAccount outputAccount = response.getEntity(ResponseAccount.class);
		Account resAccount = outputAccount.getAccount();
		System.out.println("<<<< Generating JSON Output >>>");
		System.out.println(outputAccount);
		assertTrue(resAccount.getId() > 0);
	}
	
	
	private void resetDB() {
		
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
