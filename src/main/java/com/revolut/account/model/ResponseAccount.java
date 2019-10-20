package main.java.com.revolut.account.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="responseAccount")
public class ResponseAccount extends Response {

	private Account account;

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}
	
	
}
