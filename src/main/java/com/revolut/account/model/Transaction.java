package main.java.com.revolut.account.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement (name="transaction")
public class Transaction {
	
	public static final String AMOUNT_SHOULD_BE_GREATER_THEN_0 = "Transfered Amount should be greater then 0.";
	public static final String ACCOUNT_FROM_TO_DOES_NOT_EXIST = "Account From/ Account To does not Exist";
	public static final String ACCOUNT_FROM_TO_ARE_SAME = "Account From/ Account are the same";
	public static final String ACCOUNT_FROM_TO_IS_CLOSED = "Transaction has not been completed because Account From/ Account is closed";
	public static final String ACCOUNT_FROM_DOES_NOT_HAVE_ENOUGHT_BALANCE = "Account From  does not have anought balance Exist";
	public static final String TRANSACTION_HAS_BEEN_COMPLETED_SUCCESSFULLY = "Transaction has been completed successfully";
	
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
