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

//http://localhost:8080/RevolutAccountAPI/account/5617214d-04a8-46cc-a90c-d975d3015387/getDummy
//https://crunchify.com/how-to-start-embedded-http-jersey-server-during-java-application-startup/
//@SuppressWarnings("restriction")
public class RevolutAccountAPI {

	public static void main(String[] args) throws IOException {
		System.out.println("Starting Crunchify's Embedded Jersey HTTPServer...\n");
		HttpServer crunchifyHTTPServer = createHttpServer();
		crunchifyHTTPServer.start();
		System.out.println(
				String.format("\nJersey Application Server started with WADL available at " + "%sapplication.wadl\n",
						getCrunchifyURI()));
		System.out.println("Started Crunchify's Embedded Jersey HTTPServer Successfully !!!");
	}

	private static HttpServer createHttpServer() throws IOException {
		ResourceConfig crunchifyResourceConfig = new PackagesResourceConfig("main.java.com.revolut.account");
		// This tutorial required and then enable below line:
		// http://crunchify.me/1VIwInK
		// crunchifyResourceConfig.getContainerResponseFilters().add(CrunchifyCORSFilter.class);
		return HttpServerFactory.create(getCrunchifyURI(), crunchifyResourceConfig);
	}

	private static URI getCrunchifyURI() {
		return UriBuilder.fromUri("http://" + crunchifyGetHostName() + "/RevolutAccountAPI").port(8080).build();
	}

	private static String crunchifyGetHostName() {
		String hostName = "localhost";
		try {
			hostName = InetAddress.getLocalHost().getCanonicalHostName();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return hostName;
	}

}
