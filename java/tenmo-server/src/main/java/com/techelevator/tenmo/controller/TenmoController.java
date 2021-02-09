package com.techelevator.tenmo.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.techelevator.tenmo.dao.AccountsDAO;
import com.techelevator.tenmo.dao.TransfersDAO;
import com.techelevator.tenmo.dao.UserDAO;
import com.techelevator.tenmo.model.Accounts;
import com.techelevator.tenmo.model.Transfers;
import com.techelevator.tenmo.model.User;

@RestController
public class TenmoController {

	private UserDAO userDao;
	private TransfersDAO transfersDao;
	private AccountsDAO accountsDao;

	public TenmoController(UserDAO userDao, TransfersDAO transfersDao, AccountsDAO accountsDao) {
		this.userDao = userDao;
		this.transfersDao = transfersDao;
		this.accountsDao = accountsDao;
	}

	@RequestMapping(path = "members", method = RequestMethod.GET)
	public List<User> getAllUsersWithIds() {
		return userDao.listUsernameAndId();
	}

	@RequestMapping(path = "members/{id}", method = RequestMethod.GET)
	public String getUsernameFromId(@PathVariable int id) {
		return userDao.findUsernameById(id);
	}

	@RequestMapping(path = "balance/{id}", method = RequestMethod.GET)
	public BigDecimal showBalanceById(@PathVariable int id) {
		return accountsDao.getBalance(id);
	}
	
	@RequestMapping(path = "balance/{id}", method = RequestMethod.PUT)
	public void updateBalance(@PathVariable int id, @RequestBody Accounts accounts) {
		accountsDao.updateBalance(id, accounts);
	}
	
	@RequestMapping(path = "transfers/{id}", method = RequestMethod.GET)
	public List<Transfers> getUserTransfers(@PathVariable int id) {
		return transfersDao.getUserTransfers(id);
	}

	@RequestMapping(path = "transfers/{id}", method = RequestMethod.POST)
	public void addTransfer(@PathVariable int id, @RequestBody Transfers transfer) {
		transfersDao.createNewTransfer(transfer);
	}

}
