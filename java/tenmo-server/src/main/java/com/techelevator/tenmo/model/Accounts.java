package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Accounts {

	private Long accountId;
	private int userId;
	private BigDecimal balance;

	public Accounts(Long accountId, int userId, BigDecimal balance) {
		this.accountId = accountId;
		this.userId = userId;
		this.balance = balance;
	}

	public Accounts() {
		// Overloaded
	}

	public Accounts(int i, int userId2, BigDecimal bal) {
		// TODO Auto-generated constructor stub
	}

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

}
