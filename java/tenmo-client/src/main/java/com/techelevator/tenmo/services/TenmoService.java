package com.techelevator.tenmo.services;

import java.math.BigDecimal;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.techelevator.tenmo.models.Accounts;
import com.techelevator.tenmo.models.Transfers;
import com.techelevator.tenmo.models.User;

public class TenmoService {

	private String BASE_URL;
	private RestTemplate restTemplate;

	public TenmoService(String url) {
		BASE_URL = url;
		restTemplate = new RestTemplate();
	}

	public User[] getAllUsers(String authToken) {
		ResponseEntity<User[]> users = restTemplate.exchange(BASE_URL + "members", HttpMethod.GET, 
				makeAuthEntity(authToken), User[].class);
		
		return users.getBody();
		
	}
	
	public String getUsernameFromId(String authToken, int id) {
		ResponseEntity<String> user = restTemplate.exchange(BASE_URL + "members/" + id, HttpMethod.GET, 
				makeAuthEntity(authToken), String.class);
		
		return user.getBody();
	}
	
	public BigDecimal getUserBalance(String authToken, int id) {
		ResponseEntity<BigDecimal> response = restTemplate.exchange(BASE_URL + "balance/" + id, HttpMethod.GET,
				makeAuthEntity(authToken), BigDecimal.class);

		return response.getBody();
	}
	
	public BigDecimal updateBalance(String authToken, Accounts accounts) {
		ResponseEntity<BigDecimal> response = restTemplate.exchange(BASE_URL + "balance/" + accounts.getUserId(), HttpMethod.PUT,
			makeAccountsAuthEntity(authToken, accounts), BigDecimal.class);
		
		return response.getBody();
	}
	
	public Transfers[] getUserTransfers(String authToken, int id) {
		ResponseEntity<Transfers[]> userTransfers = restTemplate.exchange(BASE_URL + "transfers/" + id, HttpMethod.GET, 
				makeAuthEntity(authToken), Transfers[].class);
		
		return userTransfers.getBody();
		
	}
	
	public Transfers createNewTransfer(String authToken, Transfers transfers) {
		ResponseEntity<Transfers> createTransfer = restTemplate.exchange(BASE_URL + "transfers/" + transfers.getAccountFrom(), HttpMethod.POST,
				makeTransfersAuthEntity(authToken, transfers), Transfers.class);
		
		return createTransfer.getBody();
	}

	private HttpEntity makeAuthEntity(String authToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(authToken);
		HttpEntity entity = new HttpEntity<>(headers);
		return entity;
	}
	
	private HttpEntity<Transfers> makeTransfersAuthEntity(String authToken, Transfers transfer) {
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(authToken);
		HttpEntity<Transfers> entity = new HttpEntity<>(transfer, headers);
		return entity;
	}
	
	private HttpEntity<Accounts> makeAccountsAuthEntity(String authToken, Accounts accounts) {
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(authToken);
		HttpEntity<Accounts> entity = new HttpEntity<>(accounts, headers);
		return entity;
	}
}
