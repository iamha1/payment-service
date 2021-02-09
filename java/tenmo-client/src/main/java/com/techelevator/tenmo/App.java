package com.techelevator.tenmo;

import java.math.BigDecimal;
import java.util.Scanner;

import com.techelevator.tenmo.models.Accounts;
import com.techelevator.tenmo.models.AuthenticatedUser;
import com.techelevator.tenmo.models.Transfers;
import com.techelevator.tenmo.models.User;
import com.techelevator.tenmo.models.UserCredentials;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.AuthenticationServiceException;
import com.techelevator.tenmo.services.TenmoService;
import com.techelevator.view.ConsoleService;

public class App {

	private static final String API_BASE_URL = "http://localhost:8080/";

	private static final String MENU_OPTION_EXIT = "Exit";
	private static final String LOGIN_MENU_OPTION_REGISTER = "Register";
	private static final String LOGIN_MENU_OPTION_LOGIN = "Login";
	private static final String[] LOGIN_MENU_OPTIONS = { LOGIN_MENU_OPTION_REGISTER, LOGIN_MENU_OPTION_LOGIN,
			MENU_OPTION_EXIT };
	private static final String MAIN_MENU_OPTION_SEND_BUCKS = "Send TE bucks";
	private static final String MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS = "View your past transfers";
	private static final String MAIN_MENU_OPTION_REQUEST_BUCKS = "*Under Maintanence* Request TE bucks";
	private static final String MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS = "*Under Maintanence* View your pending requests";
	private static final String MAIN_MENU_OPTION_LOGIN = "Login as different user";
	private static final String[] MAIN_MENU_OPTIONS = { MAIN_MENU_OPTION_SEND_BUCKS,
			MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS, MAIN_MENU_OPTION_REQUEST_BUCKS,
			MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS, MAIN_MENU_OPTION_LOGIN, MENU_OPTION_EXIT };

	private TenmoService tenmoService = new TenmoService(API_BASE_URL);
	private AuthenticatedUser currentUser;
	private ConsoleService console;
	private AuthenticationService authenticationService;

	Transfers transfers = new Transfers();
	Accounts accounts = new Accounts();
	User user = new User();

	Scanner userInput = new Scanner(System.in);

	public static void main(String[] args) {
		App app = new App(new ConsoleService(System.in, System.out), new AuthenticationService(API_BASE_URL));
		app.run();
	}

	public App(ConsoleService console, AuthenticationService authenticationService) {
		this.console = console;
		this.authenticationService = authenticationService;
	}

	public void run() {
		console.clearConsole();
		System.out.println("***********************************************");
		System.out.println("************** Welcome to TEnmo! **************");
		System.out.println("***********************************************");
		System.out.println("Co-Devleoped By Ha Nguyen and Brandon Wakefield");

		registerAndLogin();
		mainMenu();
	}

	private void mainMenu() {
		while (true) {
			System.out.println("\nLogged In As    : " + this.currentUser.getUser().getUsername());
			viewCurrentBalance();

			String choice = (String) console.getChoiceFromOptions(MAIN_MENU_OPTIONS);
			if (MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS.equals(choice)) {
				viewTransferHistory();
			} else if (MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS.equals(choice)) {
				viewPendingRequests();
			} else if (MAIN_MENU_OPTION_SEND_BUCKS.equals(choice)) {
				sendBucks();
			} else if (MAIN_MENU_OPTION_REQUEST_BUCKS.equals(choice)) {
				requestBucks();
			} else if (MAIN_MENU_OPTION_LOGIN.equals(choice)) {
				login();
			} else {
				exitProgram();
			}
		}
	}

	private void viewCurrentBalance() {
		BigDecimal userBalance = tenmoService.getUserBalance(this.currentUser.getToken(),
				this.currentUser.getUser().getId());

		String printUserBalance = String.format("%.2f", userBalance);
		System.out.println("Current Balance : TE$" + printUserBalance);
	}

	private void viewTransferHistory() {
		console.clearConsole();
		Transfers[] userTransfers = tenmoService.getUserTransfers(this.currentUser.getToken(),
				this.currentUser.getUser().getId());

			while (true) {
				if (userTransfers.length == 0) {
					System.out.println(this.currentUser.getUser().getUsername() + ", you have no past transfers to view.");
					break;
					
				} else {
					System.out
							.println("Enter the corresponding menu number for the transfer ID you want details of.\n");
					System.out.println(
							"Transfer Type Codes: (1 - Request)(2 - Send) | Transfer Status Codes: (1 - Pending)(2 - Approved)(3 - Rejected)");

					Transfers userTransferChoice = (Transfers) console.getChoiceFromOptions(userTransfers);
					console.clearConsole();

					System.out.println("Transfer Details:\n");
					System.out.println("Transfer ID : " + userTransferChoice.getTransferId());

					accounts.setUserId(userTransferChoice.getAccountFrom());
					String accountFromUsername = tenmoService.getUsernameFromId(this.currentUser.getToken(),
							accounts.getUserId());
					user.setUsername(accountFromUsername);
					System.out.println("Sent From   : " + user.getUsername());

					accounts.setUserId(userTransferChoice.getAccountTo());
					String accountToUsername = tenmoService.getUsernameFromId(this.currentUser.getToken(),
							accounts.getUserId());
					user.setUsername(accountToUsername);
					System.out.println("Sent To     : " + user.getUsername());

					System.out.println("Type        : Send");
					System.out.println("Status      : Approved");

					System.out.println("Amount      : TE$" + userTransferChoice.getAmount());
					break;
				}
			}
		}
	

	private void viewPendingRequests() {
		// TODO Auto-generated method stub

	}

	private void sendBucks() {
		console.clearConsole();
		BigDecimal userBalance = tenmoService.getUserBalance(this.currentUser.getToken(),
				this.currentUser.getUser().getId());

		System.out.println("How many TEBucks do you want to send?");
		System.out.print("TE$");
		String userAmountToSendStr = userInput.nextLine();
		BigDecimal userAmountToSend = new BigDecimal(userAmountToSendStr);

		if (userBalance.compareTo(userAmountToSend) < 0) {
			console.clearConsole();
			System.out.println("You do not have enough TEBucks in your account to make that transfer.");

		} else if (userBalance.compareTo(userAmountToSend) >= 0) {

			console.clearConsole();
			System.out.println("Who do you want to send TE$" + userAmountToSend + " to?\n");

			User[] printUserList = tenmoService.getAllUsers(this.currentUser.getToken());

			while (true) {
				System.out.println("Enter the corresponding menu number for the member you want to transfer to.");
				User userTo = (User) console.getChoiceFromOptions(printUserList);
				console.clearConsole();

				if (userTo.getId() == this.currentUser.getUser().getId()) {
					System.out.println("You may not send money to yourself.");
					break;

				} else {
					accounts.setUserId(this.currentUser.getUser().getId());
					BigDecimal newUserBalance = userBalance.subtract(userAmountToSend);
					accounts.setBalance(newUserBalance);

					tenmoService.updateBalance(this.currentUser.getToken(), accounts);

					accounts.setUserId(userTo.getId());
					BigDecimal toAccountBalanceBeforeTransfer = tenmoService.getUserBalance(this.currentUser.getToken(),
							accounts.getUserId());
					BigDecimal newAccountToBalance = toAccountBalanceBeforeTransfer.add(userAmountToSend);
					accounts.setBalance(newAccountToBalance);

					tenmoService.updateBalance(this.currentUser.getToken(), accounts);

					transfers.setTransferTypeId(1);
					transfers.setTransferStatusId(2);
					transfers.setAccountFrom(this.currentUser.getUser().getId());
					transfers.setAccountTo(userTo.getId());
					transfers.setAmount(userAmountToSend);

					tenmoService.createNewTransfer(this.currentUser.getToken(), transfers);

					System.out.println("Your transfer is approved! You sent TE$" + userAmountToSend + " to " +
					userTo.getUsername() + ".");
					break;
				}
			}

		}

	}

	private void requestBucks() {
		// TODO Auto-generated method stub

	}

	private void exitProgram() {
		System.exit(0);
	}

	private void registerAndLogin() {
		while (!isAuthenticated()) {
			String choice = (String) console.getChoiceFromOptions(LOGIN_MENU_OPTIONS);
			if (LOGIN_MENU_OPTION_LOGIN.equals(choice)) {
				console.clearConsole();
				login();
			} else if (LOGIN_MENU_OPTION_REGISTER.equals(choice)) {
				console.clearConsole();
				register();
			} else {
				// the only other option on the login menu is to exit
				exitProgram();
			}
		}
	}

	private boolean isAuthenticated() {
		return currentUser != null;
	}

	private void register() {
		System.out.println("Please register a new user account.");
		boolean isRegistered = false;
		while (!isRegistered) // will keep looping until user is registered
		{
			UserCredentials credentials = collectUserCredentials();
			try {
				authenticationService.register(credentials);
				isRegistered = true;
				System.out.println("Registration successful. You can now login.");
			} catch (AuthenticationServiceException e) {
				System.out.println("REGISTRATION ERROR: " + e.getMessage());
				System.out.println("Please attempt to register again.");
			}
		}
	}

	private void login() {
		System.out.println("Please log in.");
		currentUser = null;
		while (currentUser == null) // will keep looping until user is logged in
		{
			UserCredentials credentials = collectUserCredentials();
			try {
				currentUser = authenticationService.login(credentials);
			} catch (AuthenticationServiceException e) {
				System.out.println("LOGIN ERROR: " + e.getMessage());
				System.out.println("Please attempt to login again.");
			}
		}
	}

	private UserCredentials collectUserCredentials() {
		String username = console.getUserInput("Username");
		String password = console.getUserInput("Password");
		console.clearConsole();
		return new UserCredentials(username, password);
	}
}
