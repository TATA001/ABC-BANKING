package com.cg.loanProject.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.cg.loanProject.bean.Account;
import com.cg.loanProject.bean.Transactions;
import com.cg.loanProject.controller.ProjectController;
import com.cg.loanProject.exceptions.NoAccountFoundException;

@Repository
public class AccountDaoImpl implements IAccountDao {
	
	Logger logger = LoggerFactory.getLogger(ProjectController.class);

	@Autowired
	private EntityManager entityManager;
	
	static int accountNumber = 100000;

	static String transactionNumber = "T0001";

		/* Method: getAllAccounts
		 * Description: Used to fetch all the records exist in account table.
		 * @return List: It returns the list of accounts
		 */
		@Override
		public List<Account> getAllAccounts() {
			String s = "select a from Account a";
			TypedQuery<Account> q1 = entityManager.createQuery(s, Account.class);
			List<Account> accounts = q1.getResultList();
			return accounts;
		}

		/* Method: findAccount
		 * Description: Used to find the account.
		 * @param userName : Username of user account.
		 * @return Account: It returns Account of user.
		 * @return null: It returns null if no user exist with given username.
		 */
		@Override
		public Account findAccount(String userName) {
			String Str = "SELECT account_Number FROM Account where userName=:num";
			TypedQuery<Integer> query1 = entityManager.createQuery(Str, Integer.class);
			query1.setParameter("num", userName);
			List<Integer> account_Numbers = query1.getResultList();

			if (!account_Numbers.isEmpty()) {
				Account account = entityManager.find(Account.class, account_Numbers.get(0));
				return account;
			}
			else {
				return null;
			}
		}

		/* Method: editAccount
		 * Description: To edit or update the existing record.
		 * @param account : It contains the data of type Account
		 * @return nothing
		 */
		@Override
		public void editAccount(Account account) {
			logger.info("Account Updated");
			entityManager.merge(account);
		}

		/* Method: deleteAccount
		 * Description: Used to delete the record from account table in database.
		 * @param userName : Username of user.
		 * @throws NoAccountFoundException: It is raised if no account exist with given username.
		 */
		@Override
		public void deleteAccount(String userName) {
			Account account = findAccount(userName);
		
			if(account!= null)
			{
				Query query2 = entityManager.createQuery("DELETE FROM Transactions where account_Number=:num");
				query2.setParameter("num", account.getAccount_Number());
				
				query2.executeUpdate();
				entityManager.remove(account);
				logger.info("Account Deleted");
			}
			else{
				logger.error("No account found with username: "+userName);
				throw new NoAccountFoundException("No Account found with given Username");
			}
		}

		/* Method: passwordValidate
		 * Description: Used to verify user credentials while logging in.
		 * @param userName : Username of user account.
		 * @param password : Password of user account.
		 * @return int: It returns 1 as a status if the credentials provided by user are valid. 
		 * @return int: It returns 0 as a status if the credentials provided by user are invalid. 
		 */
		@Override
		public int passwordValidate(String userName, String password) {
			String qStr = "SELECT a.password FROM Account a  where a.userName=:num";
			TypedQuery<String> query = entityManager.createQuery(qStr, String.class);
			query.setParameter("num", userName);
			List<String> password_list = query.getResultList();
			
			if (!password_list.isEmpty()) {
				if (password_list.get(0).equals(password)) {
					return 1;
				}
			else {
				return 0;
			}
		} 
		else {
			return 0;
		}
	}

	/* Method: generateAccountNumber
	 * Description: Used to generate account number when user is creating account for first time.
	 * @param account : It contains the data of type account.
	 * @return int: It returns maximum account number if there exist accounts in database and the data provided by the user is unique.
	 * @return int: It returns static value as account number if there are no accounts exist in database.
	 * @return int: It returns 0 if account already exist in database with the data provided by user. 
	 */
	@Override
	public int generateAccountNumber(Account account) {

		String qStr = "SELECT userId FROM Account";
		TypedQuery<String> query = entityManager.createQuery(qStr, String.class);
		List<String> userId_list = query.getResultList();

		String qStr1 = "SELECT userName FROM Account";
		TypedQuery<String> query2 = entityManager.createQuery(qStr1, String.class);
		List<String> userName_list = query2.getResultList();

		if (!userId_list.contains(account.getUserId()) && (!userName_list.contains(account.getUserName())) && !userId_list.isEmpty() && !userName_list.isEmpty()) {

			String Str = "SELECT max(account_Number) FROM Account";
			TypedQuery<Integer> query1 = entityManager.createQuery(Str, Integer.class);
			int account_Number = query1.getSingleResult();
			 
			return account_Number;

		} else if (userId_list.isEmpty() && userName_list.isEmpty()) {

			return accountNumber; 
			
		} else {

			return 0; 
		}
	}

	/* Method: addAccount
	 * Description: Used to create a new account.
	 * @param account : It contains the data of type Account.
	 * @return int: It returns account number.
	 */
	@Override
	public int addAccount(Account account) {
		entityManager.persist(account);
		logger.info("Account Created. Returning status to client");
		return account.getAccount_Number();
	}

	/* Method: generateTransactionNumber
	 * Description: Used to generate transaction number when amount is credited or debited from account.
	 * @return int: It returns maximum transaction number if there exist transactions in database.
	 * @return int: It returns static value as transaction number if there are no transactions exist in database.
	 */
	@Override
	public String generateTransactionNumber() {
		String qStr5 = "SELECT transaction_Number FROM Transactions";
		TypedQuery<String> query5 = entityManager.createQuery(qStr5, String.class);
		List<String> transaction_Number_List = query5.getResultList();
		
		if (transaction_Number_List.size()>0) {
			String qStr = "SELECT max(transaction_Number) FROM Transactions";
			TypedQuery<String> query = entityManager.createQuery(qStr, String.class);
			String transaction_Number = query.getSingleResult();
			String transaction_Number1 = "T" + String.format("%04d", Integer.parseInt(transaction_Number.substring(1)) + 1);
			return transaction_Number1;
		} else
			return transactionNumber;

	}

	/* Method: addTransaction
	 * Description: Used to persist the transaction done.
	 * @param transaction : It contains the data of type transaction.
	 * @return nothing
	 */
	@Override
	public void addTransaction(Transactions transaction) {
		entityManager.persist(transaction);
	}

	/* Method: printTransactions
	 * Description: Used to print the transaction history of the user.
	 * @param userName : Username of user account.
	 * @return List: It returns list of transactions under the user account.
	 * @return null: It returns null if no account found with given username.
	 */
	
	@Override
	public List<Transactions> printTransactions(String userName) {

		Account account = findAccount(userName);

		if (account != null) {

			String qStr2 = "SELECT t FROM Transactions t WHERE t.transaction_Id between :start and :end and t.account_Number= :num order by t.transaction_Id desc";
			TypedQuery<Transactions> query = entityManager.createQuery(qStr2, Transactions.class);
			query.setParameter("start", 1);
			query.setParameter("end", account.getTransaction_Count());
			query.setParameter("num", account.getAccount_Number());
			List<Transactions> transactions = query.getResultList();

			return transactions;
		}
		else {
			return null;
		}
	}

	/* Method: accountValidate
	 * Description: Used to validate account details if user forgets his/her password and wants to set a new password.
	 * @param userName : Username of user account.
	 * @param accountNumber : Account number of user.
	 * @return String: It returns PAN number of user.
	 * @return String: It returns false if no account found with given inputs.
	 */
	@Override
	public String accountValidate(String userName, int accountNumber) {

		String Str = "SELECT a.userId FROM Account a where a.userName=:num and a.account_Number=:num1";
		TypedQuery<String> query1 = entityManager.createQuery(Str, String.class);
		query1.setParameter("num", userName);
		query1.setParameter("num1", accountNumber);
		List<String> userId_List = query1.getResultList();

		if (!userId_List.isEmpty()) {
			String userId = userId_List.get(0);
			return userId;
		} else
			return "false";
	}

	/* Method: getTotalBalance
	 * Description: Used to get the total balance in the account table.
	 * @return double: It returns closing balance of bank.
	 * @return double: It return 0 if there exist no data in database.
	 */
	@Override
	public double getTotalBalance() {
		String qStr = "SELECT balance_Amount FROM Account";
		TypedQuery<Double> query = entityManager.createQuery(qStr, Double.class);
		List<Double> totalBalance_List= query.getResultList();
		
		if(!totalBalance_List.isEmpty())
		{
			String qStr1 = "SELECT SUM(balance_Amount) FROM Account";
			TypedQuery<Double> query1 = entityManager.createQuery(qStr1, Double.class);
			double totalBalance= query1.getSingleResult();
			return totalBalance;
		}
		else
			return 0;
		
	}
	

	/* Method: getPendingLoans
	 * Description: Used to get the total pending loans in the account table.
	 * @return double: It returns pending loan amount in bank.
	 * @return double: It return 0 if there exist no data in database.
	 */
	@Override
	public double getPendingLoans() {
		String qStr = "SELECT loan_Amount FROM Account";
		TypedQuery<Double> query = entityManager.createQuery(qStr, Double.class);
		List<Double> pendingLoan_List= query.getResultList();
		
		if(!pendingLoan_List.isEmpty())
		{
			String qStr1 = "SELECT SUM(loan_Amount) FROM Account";
			TypedQuery<Double> query1 = entityManager.createQuery(qStr1, Double.class);
			double pendingLoanAmount= query1.getSingleResult();
			return pendingLoanAmount;
		}
		else
			return 0;
	}

}
