package main.java.com.revolut.account.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement (name="transaction")
public class Transaction {
	private UUID id;
	private long fromAccount;
	private long toAccount;
	private BigDecimal amount;
	private LocalDateTime creationDate;
	
	public LocalDateTime getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(LocalDateTime creationDate) {
		this.creationDate = creationDate;
	}
	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}
	public long getFromAccount() {
		return fromAccount;
	}
	public void setFromAccount(long fromAccount) {
		this.fromAccount = fromAccount;
	}
	public long getToAccount() {
		return toAccount;
	}
	public void setToAccount(long toAccount) {
		this.toAccount = toAccount;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	@Override
	public String toString() {
		return "Transaction [id=" + id + ", fromAccount=" + fromAccount + ", toAccount=" + toAccount + ", amount="
				+ amount + "]";
	}
	
}
