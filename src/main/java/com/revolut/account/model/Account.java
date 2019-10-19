
package main.java.com.revolut.account.model;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDateTime;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import main.java.com.revolut.LocalDateTimeAdapter;

@XmlRootElement (name="account")
public class Account {
	private BigDecimal balance;
	private long id;
	private String owner;
	private boolean isClosed = false;
	
	public boolean isClosed() {
		return isClosed;
	}

	public void setClosed(boolean isClosed) {
		updateDate = LocalDateTime.now(Clock.systemUTC());
		this.isClosed = isClosed;
	}

	//@XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
	private transient  LocalDateTime  creationDate;
	
	//@XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
	private transient LocalDateTime updateDate;
	
	public Account() {
		creationDate = LocalDateTime.now(Clock.systemUTC());
		updateDate = LocalDateTime.now(Clock.systemUTC());
	}
	
	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public LocalDateTime getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(LocalDateTime creationDate) {
		this.creationDate = creationDate;
	}

	public LocalDateTime getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(LocalDateTime updateDate) {
		this.updateDate = updateDate;
	}

	public BigDecimal getBalance() {
		return balance;
	}
	
	public void setBalance(BigDecimal balance) {
		updateDate = LocalDateTime.now(Clock.systemUTC());
		this.balance = balance;
	}
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "Account [balance=" + balance + ", id=" + id + ", owner=" + owner /*+ ", creationDate=" + creationDate
				+ ", updateDate=" + updateDate */+ "]";
	}
	
	

	
}
