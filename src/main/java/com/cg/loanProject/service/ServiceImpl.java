package com.cg.loanProject.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cg.loanProject.bean.Account;
import com.cg.loanProject.bean.Transactions;
import com.cg.loanProject.controller.ProjectController;
import com.cg.loanProject.dao.IAccountDao;
import com.cg.loanProject.exceptions.*;

@Service
@Transactional
public class ServiceImpl implements IService {

	Logger logger = LoggerFactory.getLogger(ProjectController.class);
	
	@Autowired
	private IAccountDao loanDao;

		/* Method: getAllAccounts
		 * Description: Used to fetch all the records exist in account table.
		 * @return List: It returns the list of accounts
		 * @throws NoDataFoundException: It is raised if there exist no data in table.
		 */
		@Override
		public List<Account> getAllAccounts() {
			if(loanDao.getAllAccounts().size()>0)
			{
				logger.info("Returning Accounts to client");
				return loanDao.getAllAccounts();
			}
		
			else
				logger.error("No accounts exist in database");
				throw new NoDataFoundException("No Users Exist");
		}
		
		/* Method: editAccount
		 * Description: To edit or update the existing record.
		 * @param account : It contains the data of type Account
		 * @return nothing
		 */
		@Override
		public void editAccount(Account account) {
			loanDao.editAccount(account);
		}

		/* Method: deleteAccount
		 * Description: Used to delete the record from account table in database.
		 * @param userName : Username of user.
		 * @return nothing
		 */	
		@Override
		public void deleteAccount(String userName) {
			loanDao.deleteAccount(userName);
		}
		
		/* Method: addAccount
		 * Description: Used to create a new account.
		 * @param account : It contains the data of type Account.
		 * @return int: It returns an integer as status if account is created.
		 * @throws AccountAlreadyExistException: It is raised if an account exist with the given inputs.
		 */
		@Override
		public int addAccount(Account account) {
			int i = loanDao.generateAccountNumber(account);
			if (i != 0) {
				account.setAccount_Number(i + 1);
				account.setEmi(0.0);
				account.setLoan_Amount(0.0);
				account.setTransaction_Count(1);
				account.setLoan_Type(null);
				Transactions transactions = new Transactions();
				transactions.setTransaction_Number(loanDao.generateTransactionNumber());
				transactions.setAccount_Number(account.getAccount_Number());
				transactions.setTransaction_Id(1);
				transactions.setDescription("Amount Credited");
				transactions.setCredit(account.getBalance_Amount());
				transactions.setBalance(account.getBalance_Amount());
				String transaction_time = setTransactionTime().toString();
				transactions.setTransaction_Time(transaction_time);
				loanDao.addTransaction(transactions);
				loanDao.addAccount(account);
				return 1; 
			} 
			else
				logger.error("Account already exist in database");
				throw new AccountAlreadyExistException("Account already exist with this PAN Number or Username!");
		}

		/* Method: findAccount
		 * Description: Used to find the account.
		 * @param userName : Username of user account.
		 * @return Account: It returns Account of user.
		 * @throws NoAccountFoundException: It is raised if no account exist with given username.
		 */
		@Override
		public Account findAccount(String userName) {
			if(loanDao.findAccount(userName)!= null)
			{
				logger.info("Returning Account data to client");
				return loanDao.findAccount(userName);
			}
			else
				logger.error("No Account found with username: "+userName);
				throw new NoAccountFoundException("No Account Found with given Username");
		}

		/* Method: checkBalance
		 * Description: Used to check the account balance.
		 * @param userName : Username of user account.
		 * @return double: It returns account balance.
		 * @throws NoAccountFoundException: It is raised if no account exist with given username.
		 */
		@Override
		public double checkBalance(String userName) {
			Account account = loanDao.findAccount(userName);
		
				if(account!= null)
				{
					logger.info("Returning Balance Amount to client");
					return account.getBalance_Amount();
				}
				else
					logger.error("No Account found with username: "+userName);
					throw new NoAccountFoundException("No Account Found with given Username");
		}

		/* Method: passwordValidate
		 * Description: Used to verify user credentials while logging in.
		 * @param userName : Username of user account.
		 * @param password : Password of user account.
		 * @return int: It returns an integer as a status that the credentials provided by user are valid. 
		 * @throws InvalidUsernameOrPawwordException: It is raised if user enters invalid credentials(i.e.,incorrect username or password).
		 */
		@Override
		public int passwordValidate(String userName, String password){
			if(loanDao.passwordValidate(userName, password) == 0)
			{
				logger.error("Invalid username or password");
				throw new InvalidUserNameOrPasswordException("Invalid Username or Password");
			}
			else
				logger.info("Returning validation success status to client");
				return loanDao.passwordValidate(userName, password);
		}

		/* Method: depositAmount
		 * Description: Used to depoist amount in account.
		 * @param userName : Username of user account.
		 * @param amount : Amount to be deposited in user's account.
		 * @return int: It returns an integer as a status that amount is credited into account. 
		 * @throws NoAccountFoundException: It is raised if no account exist with given username.
		 */
		@Override
		public double depositAmount(String userName, double amount) {
			Account account = loanDao.findAccount(userName);
		
			if(account!= null)
			{
				double balance = account.getBalance_Amount();
				double balanceSum = balance + amount;
				account.setBalance_Amount(balanceSum);
				Transactions transactions = new Transactions();
				transactions.setTransaction_Number(loanDao.generateTransactionNumber());
				transactions.setAccount_Number(account.getAccount_Number());
				transactions.setTransaction_Id(account.getTransaction_Count() + 1);
				transactions.setDescription("Amount Credited");
				transactions.setCredit(amount);
				transactions.setBalance(account.getBalance_Amount());
				String transaction_time = setTransactionTime().toString();
				transactions.setTransaction_Time(transaction_time);
				account.setTransaction_Count(account.getTransaction_Count() + 1);
				loanDao.addTransaction(transactions);
				logger.info("Amount deposited in account. Returning status to client");
				return 1;
			}
			else 
				logger.error("No Account found with username: "+userName);
				throw new NoAccountFoundException("No Account Found with given Username");
		}
		
		/* Method: applyLoan
		 * Description: Used to Apply for Loan.
		 * @param userName : Username of user account.
		 * @param amount : Loan Amount
		 * @param asset : Asset value
		 * @param years : Loan Tenure
		 * @param type : Loan type
		 * @return int: It returns an integer as a status that loan has been sanctioned.
		 * @throws NoAccountFoundException: It is raised if no account exist with given username.
		 * @throws AssetValueException: It is raised if asset value is lessthan loan amount.
		 * @throws LoanAmountException: It is raised if loan exceed the asset limit.
		 * @throws InsufficientFundsException: It is raised if user don't have the required account balance to apply for loan.
		 * @throws LoanExistException: It is raised if user has taken a loan earlier and is not cleared. 
		 */
		@Override
		public double applyLoan(String userName, double amount, double assetValue, int years, String loanType) {
			Account account = loanDao.findAccount(userName);
			if(account!= null)
			{
				if (account.getLoan_Amount() == 0.0) {
			
					if (assetValue > amount && assetValue >=(amount*1.4) && account.getBalance_Amount() >= 50000) {
						double rateOfInterset = 0;
						
						if(loanType.equals("Home Loan")){rateOfInterset=8.69;}
				
						if(loanType.equals("Business Loan")){rateOfInterset = 13.5;}
				
						if(loanType.equals("Gold Loan")){rateOfInterset = 8.13;}
				
						if(loanType.equals("Vehicle Loan")){rateOfInterset = 7.43;}
					
						if(loanType.equals("Education Loan")){rateOfInterset = 10.7;}
				
						if(loanType.equals("Personal Loan")) {rateOfInterset = 10.85;}
				
						account.setAsset_Value(assetValue);
						account.setLoan_Type(loanType);
						double monthlyEmi = calculateEmi(amount, years ,rateOfInterset);
						account.setEmi(monthlyEmi);
						account.setLoan_Amount(monthlyEmi*12*years);
						logger.info("Loan Sanctioned. Returning Status to client");
						return 1;
					}
			
					else if (assetValue <= amount) {
						logger.error("Asset value must be greater than loan Amount");
						throw new AssetValueException("Assest Value must be greater than Loan Amount!");
					}
					
					else if (assetValue > amount && assetValue < (amount*1.4)) {
						logger.error("Loan limit exceeded");
						throw new LoanAmountException("Loan limit exceeded!");
					}

					else if (account.getBalance_Amount() < 50000) {
						logger.error("Insufficent Account Balance to procces the loan");
						throw new InsufficientFundsException("Insufficent Account Balance. Loan can't be Processed!");
					}
					else return 0;
				} 
				else
					logger.error("Pending loan exist");
					throw new LoanExistException("Pending Loan exist, Can't process the loan application!");
			}
			else 
				logger.error("No Account found with username: "+userName);
				throw new NoAccountFoundException("No Account Found with given Username");
		}

		/* Method: calculateEmi
		 * Description: Used to calculate EMI.
		 * @param amount : Loan Amount
		 * @param years : Loan Tenure
		 * @param rateOfInterset : Interest rateOfInterset (varies based on type of loan) 
		 * @return double: It returns monthly emi.
		 */
		@Override
		public double calculateEmi(double amount, int years, double rateOfInterset) {
			double emi = Math.ceil((amount + (amount * rateOfInterset * years) / 100) / (years * 12));
			return emi;
		}

		/* Method: payEmi
		 * Description: Used to pay EMI for the loan that user had taken.
		 * @param userName : Username of user account.
		 * @return int: It returns an integer as a successful payment status.
		 * @throws NoAccountFoundException: It is raised if no account exist with given username.
		 * @throws InsufficientFundsException: It is raised if user don't have sufficient account balance to pay EMI.
		 * @throws NoLoanExistException: It is raised if user don't have any pending loan. 
		 */
		@Override
		public double payEmi(String userName) {
			Account account = loanDao.findAccount(userName);
			
			if(account!= null)
			{
				double loanAmount = account.getLoan_Amount();
				if (loanAmount == 0.0) {
					logger.error("No Pending EMI exist");
					throw new NoLoanExistException("No Pending EMI exist!");
				} 
				else {
					
					double balance = account.getBalance_Amount();
					double paybleAmount = account.getEmi();
					if (paybleAmount <= balance) {
						double pendingLoan = loanAmount - paybleAmount;
						double balanceLeft = balance - paybleAmount;
						account.setBalance_Amount(balanceLeft);
						account.setLoan_Amount(pendingLoan);
						Transactions transactions = new Transactions();
						transactions.setTransaction_Number(loanDao.generateTransactionNumber());
						transactions.setAccount_Number(account.getAccount_Number());
						transactions.setTransaction_Id(account.getTransaction_Count() + 1);
						transactions.setDescription("Amount Debited");
						transactions.setDebit(paybleAmount);
						transactions.setBalance(account.getBalance_Amount());
						String transaction_time = setTransactionTime().toString();
						transactions.setTransaction_Time(transaction_time);
						account.setTransaction_Count(account.getTransaction_Count() + 1);
						loanDao.addTransaction(transactions);
						logger.info("EMI Paid. Returning status to client");
						return 1; 
					} 
					else {
						logger.error("Insufficient funds in the account");
						throw new InsufficientFundsException("Transaction Failed due to insufficient funds!");
					}
				}
			}
			else
			{
				logger.error("No Account found with username: "+userName);
				throw new NoAccountFoundException("No Account Found with given Username");
			}
		 }
		
		/* Method: loanForeClose
		 * Description: Used to foreclose the Loan.
		 * @param userName : Username of user account.
		 * @return int: It returns an integer as a status if loan has been foreclosed successfully.
		 * @throws NoAccountFoundException: It is raised if no account exist with given username.
		 * @throws InsufficientFundsException: It is raised if user don't have enough account balance to foreclose loan.
		 * @throws NoLoanExistException: It is raised if user don't have pending loans. 
		 */
		@Override
		public double foreClose(String userName) {
			Account account = loanDao.findAccount(userName);
			
			if(account!= null) {
				double loanAmount = account.getLoan_Amount();
				if (loanAmount == 0.0) {
					logger.error("No Pending EMI exist");
					throw new NoLoanExistException("No Pending Loans exist!");
				} 
				else {
					double balance = account.getBalance_Amount();
					if (balance >= loanAmount) {
						double balanceLeft = balance - loanAmount;
						account.setBalance_Amount(balanceLeft);
						account.setLoan_Amount(0.0);
						account.setEmi(0.0);
						account.setAsset_Value(0.0);
						account.setLoan_Type(null);

						Transactions transactions = new Transactions();
						transactions.setTransaction_Number(loanDao.generateTransactionNumber());
						transactions.setAccount_Number(account.getAccount_Number());
						transactions.setTransaction_Id(account.getTransaction_Count() + 1);
						transactions.setDescription("Amount Debited");
						transactions.setDebit(balance);
						transactions.setBalance(account.getBalance_Amount());
						String transaction_time = setTransactionTime().toString();
						transactions.setTransaction_Time(transaction_time);
						account.setTransaction_Count(account.getTransaction_Count() + 1);
						loanDao.addTransaction(transactions);
						logger.info("Loan Foreclosed. Returning status to client");
						return 2;

					} 
					else {
						logger.error("Action can't be done due to insufficient funds in account");
						throw new InsufficientFundsException("Transaction Failed due to insufficient funds!");
					}
				}
			}
			else 
				logger.error("No Account found with username: "+userName);
				throw new NoAccountFoundException("No Account Found with given Username");	
		}
		
		/* Method: loanForceClose
		 * Description: Used to foreclose loan if user agrees to handover his assets to bank.
		 * @param userName : Username of user account.
		 * @return int: It returns an integer as a status that loan has been foreclosed.
		 * @throws NoAccountFoundException: It is raised if no account exist with given username.
		 * @throws NoLoanExistException: It is raised if user don't have pending loan. 
		 */
		@Override
		public double forceClose(String userName) {
			Account account = loanDao.findAccount(userName);
		
			if(account!= null)
			{
				double loanAmount = account.getLoan_Amount();
				if (loanAmount == 0.0) {
					throw new NoLoanExistException("No Pending Loans exist!");
				} 
				else {
					Transactions transactions = new Transactions();
					account.setLoan_Amount(0.0);
					account.setEmi(0.0);
					transactions.setDebit(account.getBalance_Amount());
					account.setBalance_Amount(0.0);
					account.setAsset_Value(0.0);
					account.setLoan_Type(null);
					transactions.setTransaction_Number(loanDao.generateTransactionNumber());
					transactions.setAccount_Number(account.getAccount_Number());
					transactions.setTransaction_Id(account.getTransaction_Count() + 1);
					transactions.setDescription("Amount Debited");
					transactions.setBalance(account.getBalance_Amount());
					String transaction_time = setTransactionTime().toString();
					transactions.setTransaction_Time(transaction_time);
					account.setTransaction_Count(account.getTransaction_Count() + 1);
					loanDao.addTransaction(transactions);
					logger.info("Loan Foreclosed. Returning status to client");
					return 0; 
				}
			}
			else 
				logger.error("No Account found with username: "+userName);
				throw new NoAccountFoundException("No Account Found with given Username");
		}

		/* Method: printTransactions
		 * Description: Used to print the transaction history of the user.
		 * @param userName : Username of user account.
		 * @return List: It returns list of transactions under the user account.
		 * @throws NoAccountFoundException: It is raised if no account exist with given username. 
		 */
		@Override
		public List<Transactions> printTransactions(String userName) {
		
			if(loanDao.printTransactions(userName).size()>0)
			{
				logger.info("Returning account transactions to client");
				return loanDao.printTransactions(userName);
			}
			else
			{
				logger.error("No Account found with username: "+userName);
				throw new NoAccountFoundException("No Account exist with the given Username");
			}
		}
		
		/* Method: changePassword
		 * Description: Used to change the password of user account.
		 * @param userName : Username of user account.
		 * @param password: New password to set.
		 * @return int: It returns an integer as a status that password changed successfully.
		 * @throws NoAccountFoundException: It is raised if no account exist with given username. 
		 */
		@Override
		public int changePassword(String userName, String password) {
			Account account = loanDao.findAccount(userName);
			
			if(account!= null)
			{
				account.setPassword(password);
				logger.info("Password Changed. Returning status to client");
				return 1;
			}
			else 
				logger.error("No Account found with username: "+userName);
				throw new NoAccountFoundException("No Account Found with given Username");
		}
		
		/* Method: AccountValidate
		 * Description: Used to validate the account if user forgets his/her password and wants to set a new password.
		 * @param userName : Username of user account.
		 * @param accountNumber : Account number of user.
		 * @return String: It returns PAN number of user to cross-check in frontend.
		 * @throws NoAccountFoundException: It is raised if no account exist with given username and Account number. 
		 */
		@Override
		public String accountValidate(String userName, int accountNumber) {
			if(loanDao.accountValidate(userName,accountNumber) == "false")
			{
				logger.error("No Account found with given inputs");
				throw new NoAccountFoundException("No account exist with the given input!");
			}
			else
			{
				logger.info("Account found. Returning to client");
				return loanDao.accountValidate(userName,accountNumber);
			}
		}
		
		/* Method: setTransactionTime
		 * Description: Used to record the transaction date and time.
		 * @return String: It returns LocalDateTime in string format.
		 */
		@Override
		public String setTransactionTime() {
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern(" dd-MM-yyyy HH:mm:ss");  
			LocalDateTime now = LocalDateTime.now();
			return dtf.format(now);
		}

		/* Method: getTotalBalance
		 * Description: Used to get the total balance in the account table.
		 * @return double: It returns closing balance of bank.
		 * @throws NoDataFoundException: It is raised if there exist no data in table.
		 */
		@Override
		public double getTotalBalance() {
			
			if(loanDao.getTotalBalance()!=0)
			{
				logger.info("Returning closing balance to client");
				return loanDao.getTotalBalance();
			}
			
			else
				logger.error("No data exist in database");
				throw new NoDataFoundException("No Data Found");
		}
		
		/* Method: getPendingLoans
		 * Description: Used to get the total pending loans in the account table.
		 * @return double: It returns pending loan amount in bank.
		 * @throws NoDataFoundException: It is raised if there exist no data in table.
		 */
		@Override
		public double getPendingLoans() {
			if(loanDao.getPendingLoans()!=0)
			{
				logger.info("Returning pending loan amount to client");
				return loanDao.getPendingLoans();
			}
			
			else 
				logger.error("No data exist in database");
				throw new NoDataFoundException("No Data Found");
		}

	}
