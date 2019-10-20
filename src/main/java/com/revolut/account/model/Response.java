package main.java.com.revolut.account.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="responce")
public class Response {

	public static final String ACCOUNT_DELETED_SUCCESSFULLY = "Account deleted successfully";
	public static final String ACCOUNT_DOES_NOT_EXIST = "Account Doesn't Exists";
		
	private boolean status;
	private String message;

	public boolean getStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
