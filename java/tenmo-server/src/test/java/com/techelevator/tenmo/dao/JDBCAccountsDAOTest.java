package com.techelevator.tenmo.dao;

import static org.junit.jupiter.api.Assertions.*;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.DecimalFormat;

import javax.sql.DataSource;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import com.techelevator.tenmo.model.Accounts;

public class JDBCAccountsDAOTest {
	private static SingleConnectionDataSource dataSource;
	private static AccountsDAO dao;

	
	@BeforeAll
	public static void setupDataSource() {
		dataSource = new SingleConnectionDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/tenmo");
		dataSource.setUsername("postgres");
		dataSource.setPassword("postgres1");
		dataSource.setAutoCommit(false);
	}
	@AfterAll
	public static void closeDataSource() throws SQLException {
	dataSource.destroy();
	}
	
	@BeforeEach
	public void setup() {
		String sqlUser = "INSERT INTO users (user_id, username, password_hash) VALUES (444, 444, 444)";
		String sqlAccount = "INSERT INTO accounts (account_id, user_id, balance) VALUES (444, 444, 444.00)";
		
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.update(sqlUser);
		jdbcTemplate.update(sqlAccount);
		
		dao = new JDBCAccountsDAO(dataSource);	
	}
	
	@AfterEach
	public void rollback() throws SQLException {
		dataSource.getConnection().rollback();
	}	
	@Test
	public void get_balance_test() {
		
		BigDecimal testBalance = new BigDecimal(444.00);
		
		Accounts testAccount = getAccountBalance(444, testBalance);
		
		DecimalFormat f = new DecimalFormat("##0.00");
				
		BigDecimal results = dao.getBalance(444);
				
		assertNotNull(results);
		
		assertEquals(f.format(testAccount.getBalance()), f.format(results));
	
	}

	// helper methods
	
	private Accounts getAccountBalance(int userId, BigDecimal balance) {
		Accounts accounts = new Accounts();
		accounts.setUserId(userId);
		accounts.setBalance(balance);

		return accounts;
	}

	private void assertAccountsAreEqual(Accounts expected, Accounts actual) {
		assertEquals(expected.getAccountId(), actual.getAccountId());
		assertEquals(expected.getUserId(), actual.getUserId());
		assertEquals(expected.getBalance(), actual.getBalance());
	}
			
}		
