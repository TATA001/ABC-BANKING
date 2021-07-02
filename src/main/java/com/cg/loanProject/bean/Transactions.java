package com.cg.loanProject.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="Transactions")
public class Transactions  {
	
	@Id
	@Column(name="TRANSACTION_NUMBER")
	private String transaction_Number;
	
	@Column(name="ACCOUNT_NUMBER")
	private int account_Number;
	
	@Column(name="TRANSACTION_ID")
	private int transaction_Id;
	
	@Column(name="DESCRIPTION")
	private String description;
	
	@Column(name="DEBIT")
	private double debit;
	
	@Column(name="CREDIT")
	private double credit;
	
	@Column(name="BALANCE")
	private double balance;
	
	@Column(name="TRANSACTION_TIME")
	private String transaction_Time;
	
//	@ManyToOne
//	@JoinColumn(name="Account")
//	private Account account;
	
	public Transactions()
	{
		
	}
	
	public Transactions(String transaction_Number, int account_Number, int transaction_Id, String description, double debit, double credit, double balance, String transaction_Time) {
	this.transaction_Number = transaction_Number;
	this.account_Number = account_Number;
	this.transaction_Id = transaction_Id;
	this.description = description;
	this.debit = debit;
	this.credit = credit;
	this.balance = balance;
	this.transaction_Time = transaction_Time;
	}


	public String getTransaction_Number() {
		return transaction_Number;
	}
	public void setTransaction_Number(String transaction_Number) {
		this.transaction_Number = transaction_Number;
	}
	public int getAccount_Number() {
		return account_Number;
	}
	public void setAccount_Number(int account_Number) {
		this.account_Number = account_Number;
	}
	public int getTransaction_Id() {
		return transaction_Id;
	}
	public void setTransaction_Id(int transaction_Id) {
		this.transaction_Id = transaction_Id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public double getDebit() {
		return debit;
	}
	public void setDebit(double debit) {
		this.debit = debit;
	}
	public double getCredit() {
		return credit;
	}
	public void setCredit(double credit) {
		this.credit = credit;
	}
	public double getBalance() {
		return balance;
	}
	public void setBalance(double balance) {
		this.balance = balance;
	}
	public String getTransaction_Time() {
		return transaction_Time;
	}
	public void setTransaction_Time(String transaction_Time) {
		this.transaction_Time = transaction_Time;
	}
	
			
}
