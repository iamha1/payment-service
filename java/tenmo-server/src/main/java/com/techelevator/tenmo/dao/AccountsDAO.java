package com.techelevator.tenmo.dao;

import java.math.BigDecimal;

import com.techelevator.tenmo.model.Accounts;

public interface AccountsDAO {

	BigDecimal getBalance(int id);

	void updateBalance(int id, Accounts accounts);

}
