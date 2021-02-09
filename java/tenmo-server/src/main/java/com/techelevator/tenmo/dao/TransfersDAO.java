package com.techelevator.tenmo.dao;

import java.util.List;
import com.techelevator.tenmo.model.Transfers;

public interface TransfersDAO {

	List<Transfers> getUserTransfers(int id);

	void createNewTransfer(Transfers transfers);

}
