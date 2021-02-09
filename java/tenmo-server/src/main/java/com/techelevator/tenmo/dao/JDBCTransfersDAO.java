package com.techelevator.tenmo.dao;

import java.util.ArrayList;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import com.techelevator.tenmo.model.Transfers;

@Component
public class JDBCTransfersDAO implements TransfersDAO {

	private JdbcTemplate jdbcTemplate;

	public JDBCTransfersDAO(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public List<Transfers> getUserTransfers(int id) {
		List<Transfers> transfersList = new ArrayList<>();
		String sqlGetTransferById = "SELECT * FROM transfers WHERE account_from = ? OR account_to = ?";

		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetTransferById, id, id);

		while (results.next()) {
			Transfers theTransfer = mapRowToTransfers(results);
			transfersList.add(theTransfer);
		}
		
		return transfersList;
	}

	@Override
	public void createNewTransfer(Transfers transfers) {
		jdbcTemplate.update(
				"INSERT INTO transfers (transfer_type_id, transfer_status_id," + " account_from, account_to, amount)"
						+ " VALUES (?, ?, ?, ?, ?)",
				transfers.getTransferTypeId(), transfers.getTransferStatusId(), transfers.getAccountFrom(),
				transfers.getAccountTo(), transfers.getAmount());

	} 

	private Transfers mapRowToTransfers(SqlRowSet results) {
		Transfers transfers = new Transfers();
		transfers.setTransferId(results.getLong("transfer_id"));
		transfers.setTransferTypeId(results.getInt("transfer_type_id"));
		transfers.setTransferStatusId(results.getInt("transfer_status_id"));
		transfers.setAccountFrom(results.getInt("account_from"));
		transfers.setAccountTo(results.getInt("account_to"));
		transfers.setAmount(results.getBigDecimal("amount"));

		return transfers;
	}

}
