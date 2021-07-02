package com.cg.loanProject.bean;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.*;


@Entity
@Table(name="Account")
public class Account {
	
	@Id
	@Column(name = "ACCOUNT_NUMBER")
	private int account_Number;
	
	@Column(name = "FIRSTNAME")
	private String firstName;
	
	@Column(name = "LASTNAME")
	private String lastName;
	
	@Column(name = "PHONE_NUMBER")
	private String phone_Number;
	
	@Column(name = "SEX")
	private String sex;
	
//	@Column(name = "DATEOFBIRTH")
//	private Date dateOfBirth;
	
	@Column(name = "EMAIL")
	private String email;
	
	@Column(name = "USERID")
	private String userId;
	
	@Column(name = "PASSWORD")
	private String password;
	
	@Column(name = "USERNAME")
	private String userName;
	
	@Column(name = "BALANCE_AMOUNT")
	private double balance_Amount;
	
	@Column(name = "LOAN_AMOUNT")
	private double loan_Amount;
	
	@Column(name = "EMI")
	private double emi;
	
	@Column(name = "ASSET_VALUE")
	private double asset_Value;
	
	@Column(name = "TRANSACTION_COUNT")
	private int transaction_Count;
	
	@Column(name = "LOAN_TYPE")
	private String loan_Type;
	
	public Account() {

	}
	
	public Account(String first_Name, String last_Name, String phone_Number, String sex,String email, String userId, String password, String userName, double balance_Amount) {
		this.firstName = first_Name;
		this.lastName = last_Name;
		this.phone_Number = phone_Number;
		this.sex = sex;
		this.email = email;
		this.userId = userId;
		this.password = password;
		this.userName = userName;
		this.balance_Amount = balance_Amount;
	}


	public int getAccount_Number() {
		return account_Number;
	}

	public void setAccount_Number(int account_Number) {
		this.account_Number = account_Number;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String first_Name) {
		this.firstName = first_Name;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String last_Name) {
		this.lastName = last_Name;
	}

	public String getPhone_Number() {
		return phone_Number;
	}

	public void setPhone_Number(String phone_Number) {
		this.phone_Number = phone_Number;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public double getBalance_Amount() {
		return balance_Amount;
	}

	public void setBalance_Amount(double balance_Amount) {
		this.balance_Amount = balance_Amount;
	}

	public double getLoan_Amount() {
		return loan_Amount;
	}

	public void setLoan_Amount(double loan_Amount) {
		this.loan_Amount = loan_Amount;
	}

	public double getEmi() {
		return emi;
	}

	public void setEmi(double emi) {
		this.emi = emi;
	}

	public double getAsset_Value() {
		return asset_Value;
	}

	public void setAsset_Value(double asset_Value) {
		this.asset_Value = asset_Value;
	}

	public int getTransaction_Count() {
		return transaction_Count;
	}

	public void setTransaction_Count(int transaction_Count) {
		this.transaction_Count = transaction_Count;
	}

	public String getLoan_Type() {
		return loan_Type;
	}

	public void setLoan_Type(String loan_Type) {
		this.loan_Type = loan_Type;
	}
	
	
//	public Date getDateOfBirth() {
//		return dateOfBirth;
//	}
//
//	public void setDateOfBirth(Date dob) {
//		this.dateOfBirth = dob;
//	}
	
	
}
