package com.cg.loanProject.dao;

import java.util.List;

import com.cg.loanProject.bean.Account;
import com.cg.loanProject.bean.Transactions;


public interface IAccountDao {

	List<Account> getAllAccounts();

	Account findAccount(String userName);

	void editAccount(Account account);

	void deleteAccount(String userName);

	int passwordValidate(String userName, String password);

	int generateAccountNumber(Account account);

	int addAccount(Account account);

	void addTransaction(Transactions transactions);

	List<Transactions> printTransactions(String userName);

	String accountValidate(String userName, int accountNumber);

	String generateTransactionNumber();
	
	double getTotalBalance();
	
	double getPendingLoans();
}
