package main.java.com.revolut.account;

import java.io.IOException;

import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;

import javax.ws.rs.core.UriBuilder;

import com.sun.jersey.api.container.httpserver.HttpServerFactory;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.net.httpserver.HttpServer;

// Embedded server from This tutorial: https://crunchify.com/how-to-start-embedded-http-jersey-server-during-java-application-startup/
 
//TODO: move API url, port values  to a configuration file
//TODO: Add logs

// Actual server to run AccountAPI Implementation
public class RevolutAccountAPI {

	public static void main(String[] args) throws IOException {
		System.out.println("Starting RevolutAccountAPI Embedded Jersey HTTPServer...\n");
		HttpServer revolutAccountHTTPServer = createHttpServer();
		revolutAccountHTTPServer.start();
		System.out.println(
				String.format("\n RevolutAccountAPI Application Server started with WADL available at " + "%s/application.wadl\n",
						getAccountAPIURI()));
		System.out.println("Started RevolutAccountAPI Embedded Jersey HTTPServer Successfully !!!");
	}

	private static HttpServer createHttpServer() throws IOException {
		ResourceConfig revolutAccountConfig = new PackagesResourceConfig("main.java.com.revolut.account");

		return HttpServerFactory.create(getAccountAPIURI(), revolutAccountConfig);
	}
	
	private static URI getAccountAPIURI() {
		return UriBuilder.fromUri("http://" + accountAPIGetHostName() + "/RevolutAccountAPI").port(8080).build();
	}

	private static String accountAPIGetHostName() {
		String hostName = "localhost";
		try {
			hostName = InetAddress.getLocalHost().getCanonicalHostName();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return hostName;
	}

}
