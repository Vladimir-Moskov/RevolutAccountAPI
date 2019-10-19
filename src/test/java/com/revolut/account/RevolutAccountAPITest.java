package test.java.com.revolut.account;

import org.junit.Test;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;


public class RevolutAccountAPITest {

	@Test
	public void	lotto_resource_returns_200_with_expected_id_and_winners() {
/*
		
		 // Specify the base URL to the RESTful web service
		// RestAssured.baseURI = "http://restapi.demoqa.com/utilities/weather/city";
		 RestAssured.baseURI = "http://localhost:8080/RevolutAccountAPI";
		 // Get the RequestSpecification of the request that you want to sent
		 // to the server. The server is specified by the BaseURI that we have
		 // specified in the above step.
		 RequestSpecification httpRequest = RestAssured.given();
		 httpRequest.accept(ContentType.JSON);
		 httpRequest.contentType(ContentType.JSON);
		 // Make a request to the server by specifying the method Type and the method URL.
		 // This will return the Response from the server. Store the response in a variable.
		 Response response = httpRequest.request(Method.GET, "/account/{id}/getDummy", 12);
		 
		 // Now let us print the body of the message to see what response
		 // we have recieved from the server
		 String responseBody = response.getBody().asString();
		 System.out.println("Response Body is =>  " + responseBody);
*/
	}
}
