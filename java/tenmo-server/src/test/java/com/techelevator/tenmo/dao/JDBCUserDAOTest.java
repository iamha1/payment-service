package com.techelevator.tenmo.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;
import java.text.DecimalFormat;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import com.techelevator.tenmo.model.User;

public class JDBCUserDAOTest {

	private static SingleConnectionDataSource dataSource;
	private static UserDAO dao;
	
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
		String sqlInsertUser = "INSERT INTO users (user_id, username, password_hash) VALUES (444, 444, 444)";
		String sqlInsertAccount = "INSERT INTO accounts (account_id, user_id, balance) VALUES (444, 444, 444.00)";
		
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.update(sqlInsertUser);
		jdbcTemplate.update(sqlInsertAccount);
		
		dao = new JDBCUserDAO(jdbcTemplate);	
	}
	
	@AfterEach
	public void rollback() throws SQLException {
		dataSource.getConnection().rollback();
	}	
	@Test
	public void find_username_by_id_test() {
		
		String testName = "444";
		
		User testUserInfo = getUserInfo(444, testName, "444");

		String results = dao.findUsernameById(444);
				
		assertNotNull(results);
		
		assertEquals(testUserInfo.getUsername(), results);
	
	}
	
	@Test
	public void find_id_by_username_test() {
		long testId = 444;
		
		User testUserInfo = getUserInfo(testId, "444", "444");
		
		long results = dao.findIdByUsername("444");
		
		assertNotNull(results);
		
		assertEquals(testUserInfo.getId(), results);
	}

	// helper methods
	
	private User getUserInfo(long userId, String username, String password) {
		User user = new User();
		user.setId(userId);
		user.setUsername(username);
		user.setPassword(password);

		return user;
	}

	private void assertAccountsAreEqual(User expected, User actual) {
		assertEquals(expected.getId(), actual.getId());
		assertEquals(expected.getUsername(), actual.getUsername());
		assertEquals(expected.getPassword(), actual.getPassword());
	}
			
}		

