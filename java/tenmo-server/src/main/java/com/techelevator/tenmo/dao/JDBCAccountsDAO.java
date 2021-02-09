package com.techelevator.tenmo.dao;

import java.math.BigDecimal;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.techelevator.tenmo.model.Accounts;

@Component
public class JDBCAccountsDAO implements AccountsDAO {

	private JdbcTemplate jdbcTemplate;

	public JDBCAccountsDAO(DataSource jdbcTemplate) {
		this.jdbcTemplate = new JdbcTemplate(jdbcTemplate);
	}

	@Override
	public BigDecimal getBalance(int id) {
		return jdbcTemplate.queryForObject("SELECT balance FROM accounts "
				+ "WHERE user_id = ?", BigDecimal.class, id);
	}

	@Override
	public void updateBalance(int id, Accounts accounts) {
		jdbcTemplate.update("UPDATE accounts SET balance = ? "
				+ "WHERE user_id = ?", accounts.getBalance(), id);
	}

}
