package com.techelevator.tenmo.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;



import com.techelevator.tenmo.model.Transfers;

public class JDBCTransfersDAOTest {
	

	private static SingleConnectionDataSource dataSource;
	private JDBCTransfersDAO dao; 
	
	
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
		String sqlTransfer = "INSERT INTO transfers(transfer_id, transfer_type_id, transfer_status_id,"
				+ "account_from, account_to, amount) VALUES (444, 444, 444, 444, 444, 444.00)";
		
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.update(sqlTransfer);
		
		
		dao = new JDBCTransfersDAO(jdbcTemplate);
		
	}
	@AfterEach
	public void rollback() throws SQLException {
		dataSource.getConnection().rollback();
	}
	
	
	@Test
	void create_new_transfer_test() {
		
		DecimalFormat f = new DecimalFormat("##0.00");
		
		Transfers transfers = getTransfers(444, 444, 444, 444, 444, new BigDecimal(444.00));
		
		List<Transfers> transferList =  dao.getUserTransfers(444);
		
		Transfers results = transferList.get(transferList.size()-1);
			
		assertNotNull(transferList); 
		
		assertEquals(transfers.getTransferId(), results.getTransferId());
		assertEquals(transfers.getTransferTypeId(), results.getTransferTypeId());
		assertEquals(transfers.getTransferStatusId(), results.getTransferStatusId());
		assertEquals(transfers.getAccountFrom(), results.getAccountFrom());
		assertEquals(transfers.getAccountTo(), results.getAccountFrom());
		assertEquals(f.format(transfers.getAmount()), f.format(results.getAmount()));
		
	}	
	
	// helper methods
	
	private Transfers getTransfers(int transferId, int transferTypeId, int transferStatusId, int accountFrom, int accountTo, BigDecimal amount) {
		Transfers transfers = new Transfers();
		
		transfers.setTransferId((long)transferId);
		transfers.setTransferTypeId(transferTypeId);
		transfers.setTransferStatusId(transferStatusId);
		transfers.setAccountFrom(accountFrom);
		transfers.setAccountTo(accountTo);
		transfers.setAmount(amount);

		return transfers;
	}

	private void assertTransfersAreEqual(Transfers expected, Transfers actual) {
		assertEquals(expected.getTransferId(), actual.getTransferId());
		assertEquals(expected.getTransferTypeId(), actual.getTransferTypeId());
		assertEquals(expected.getTransferStatusId(), actual.getTransferStatusId());
		assertEquals(expected.getAccountFrom(), actual.getAccountFrom());
		assertEquals(expected.getAccountTo(), actual.getAccountTo());
		assertEquals(expected.getAmount(), actual.getAmount());
	}
			
	}


