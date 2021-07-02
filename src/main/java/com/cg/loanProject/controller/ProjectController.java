package com.cg.loanProject.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cg.loanProject.bean.Account;
import com.cg.loanProject.bean.Transactions;
import com.cg.loanProject.service.IService;

@CrossOrigin(origins="http://localhost:4205")
@RestController
@RequestMapping("/abc")
public class ProjectController {
	
	Logger logger = LoggerFactory.getLogger(ProjectController.class);

	@Autowired
	private IService service;

	/* Method: getAllAccounts
	 * Mapping Type: GetMapping
	 * Description: Used to fetch all the records exist in account table.
	 * @return List: It returns the list of accounts
	 */
	@GetMapping("/all")
	public List<Account> getAllAccounts() {
		logger.info("Fetching all the accounts from database");
		return service.getAllAccounts();
	}
	
	/* Method: addAccount
	 * Description: Used to create a new account.
	 * Mapping Type: PostMapping
	 * @param account : It contains the data of type Account.
	 * @return int: It returns an integer as status if account is created.
	 */
	@PostMapping("/all/add")
	public int addAccount(@RequestBody Account a) {
		logger.info("Creating a new account");
		return service.addAccount(a);
	}

	/* Method: editAccount
	 * Mapping Type: PutMapping
	 * Description: To edit or update the existing record.
	 * @param account : It contains the data of type Account
	 * @param userName: It is the username of account which as to be updated 
	 */
	@PutMapping("/all/{userName}")
	public Account editAccount(@RequestBody Account account, @PathVariable String userName) {
		logger.info("Updating account details of an account with username: "+userName);
		service.editAccount(account);
		return account;
	}

	/* Method: deleteAccount
	 * Mapping Type: DeleteMapping
	 * Description: Used to delete the record from account table in database.
	 * @param userName : Username of user.
	 */	
	@DeleteMapping("/all/{userName}")
	public void deleteAccount(@PathVariable String userName) {
		logger.info("Deleting an account from database with username: "+userName);
		service.deleteAccount(userName);
	}

	/* Method: findAccount
	 * Mapping Type: GetMapping
	 * Description: Used to find the account.
	 * @param userName : Username of user account.
	 * @return Account: It returns Account of user.
	 */
	@GetMapping("/all/{userName}")
	public Account findAccount(@PathVariable String userName) {
		logger.info("Fetching details of an account with username: "+userName);
		return service.findAccount(userName);
	}
	
	/* Method: passwordValidate
	 * Method Type: GetMapping
	 * Description: Used to verify user credentials while logging in.
	 * @param userName : Username of user account.
	 * @param password : Password of user account.
	 * @return int: It returns an integer as a status that the credentials provided by user are valid. 
	 */
		@GetMapping("/login/{userName}/{password}")
		public int passwordValidate(@PathVariable String userName, @PathVariable String password) {
			logger.info("Validating user credentials");
			return service.passwordValidate(userName, password);
		}

		/* Method: checkBalance
		 * Method Type: GetMapping
		 * Description: Used to check the account balance.
		 * @param userName : Username of user account.
		 * @return double: It returns account balance.
		 */
	@GetMapping("/balance/{userName}")
	public double checkBalance(@PathVariable String userName) {
		logger.info("Checking account balance of "+userName);
		return service.checkBalance(userName);
	}

	/* Method: depositAmount
	 * Method Type: GetMapping
	 * Description: Used to depoist amount in account.
	 * @param userName : Username of user account.
	 * @param amount : Amount to be deposited in user's account.
	 * @return double: It returns a double value as a status that amount is credited into account. 
	 */
	@GetMapping("/deposit/{userName}/{amount}")
	public double deposit(@PathVariable String userName, @PathVariable double amount) {
		logger.info("Depositing amount in account with username "+userName);
		return service.depositAmount(userName, amount);
	}

	/* Method: applyLoan
	 * Method Type: GetMapping
	 * Description: Used to Apply for Loan.
	 * @param userName : Username of user account.
	 * @param amount : Loan Amount
	 * @param asset : Asset value
	 * @param years : Loan Tenure
	 * @param type : Loan type
	 * @return double: It returns a double value as a status that loan has been sanctioned.
	 */
	@GetMapping("/loan/{userName}/{amount}/{assetValue}/{years}/{loanType}")
	public double applyLoan(@PathVariable String userName, @PathVariable double amount, @PathVariable double assetValue,
			@PathVariable int years, @PathVariable String loanType) {
		logger.info("Applying loan for an account with username "+userName);
		return service.applyLoan(userName, amount, assetValue, years, loanType);
	}

	/* Method: payEmi
	 * Method Type: GetMapping
	 * Description: Used to pay EMI for the loan that user had taken.
	 * @param userName : Username of user account.
	 * @return double: It returns a double value as a successful payment status.
	 */
	@GetMapping("/payEmi/{userName}")
	public double payEmi(@PathVariable String userName) {
		logger.info("Paying EMI of an account with username "+userName);
		return service.payEmi(userName);
	}

	/* Method: loanForeClose
	 * Method Type: GetMapping
	 * Description: Used to foreclose the Loan.
	 * @param userName : Username of user account.
	 * @return double: It returns a double as a status if loan has been foreclosed successfully.
	 */
	@GetMapping("/foreclose/{userName}")
	public double foreClose(@PathVariable String userName) {
		logger.info("Foreclose Loan of an account with username "+userName);
		return service.foreClose(userName);
	}
	
	/* Method: loanForceClose
	 * Method Type: GetMapping
	 * Description: Used to foreclose loan if user agrees to handover his assets to bank.
	 * @param userName : Username of user account.
	 * @return double: It returns a double value as a status that loan has been foreclosed.
	 */
		@GetMapping("/forceClose/{userName}")
		public double forceClose(@PathVariable String userName) {
			logger.info("Foreclose Loan of an account with username "+userName);
			return service.forceClose(userName);
		}

		/* Method: printTransactions
		 * Method Type: GetMapping
		 * Description: Used to print the transaction history of the user.
		 * @param userName : Username of user account.
		 * @return List: It returns list of transactions under the user account.
		 */
	@GetMapping("/transactions/{userName}")
	public List<Transactions> printTransactions(@PathVariable String userName) {
		logger.info("Fetching Transactions of an account with username "+userName);
		return service.printTransactions(userName);
	}

	/* Method: changePassword
	 * Method Type: GetMapping
	 * Description: Used to change the password of user account.
	 * @param userName : Username of user account.
	 * @param password: New password to set.
	 * @return int: It returns an integer as a status that password changed successfully.
	 */
	@GetMapping("/changePassword/{userName}/{password}")
	public int changePassword(@PathVariable String userName, @PathVariable String password) {
		logger.info("Changing password of an account with username "+userName);
		return service.changePassword(userName, password);

	}

	/* Method: AccountValidate
	 * Method Type: GetMapping
	 * Description: Used to validate the account if user forgets his/her password and wants to set a new password.
	 * @param userName : Username of user account.
	 * @param accountNumber : Account number of user.
	 * @return String: It returns PAN number of user to cross-check in frontend.
	 */
	@GetMapping("/checkAccount/{userName}/{accountNumber}")
	public String validate(@PathVariable String userName, @PathVariable int accountNumber) {
		logger.info("Validating account details");
		return service.accountValidate(userName, accountNumber);
	}
	
	/* Method: getTotalBalance
	 * Method Type: GetMapping
	 * Description: Used to get the total balance in the account table.
	 * @return double: It returns closing balance of bank.
	 * @throws NoDataFoundException: It is raised if there exist no data in table.
	 */
	@GetMapping("/totalBalance")
	public double getTotalBalance() {
		logger.info("Feteching total closing balance in bank");
		return service.getTotalBalance();
	}
	
	/* Method: getPendingLoans
	 * Method Type: GetMapping
	 * Description: Used to get the total pending loans in the account table.
	 * @return double: It returns pending loan amount in bank.
	 */
	@GetMapping("/pendingLoan")
	public double getPendingLoans() {
		logger.info("Feteching total pending loans in bank");
		return service.getPendingLoans();
	}

}
