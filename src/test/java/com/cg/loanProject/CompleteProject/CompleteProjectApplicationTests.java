package com.cg.loanProject.CompleteProject;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.cg.loanProject.bean.Account;
import com.cg.loanProject.exceptions.*;
import com.cg.loanProject.service.ServiceImpl;


@SpringBootTest
@Transactional
@Rollback(true)
class CompleteProjectApplicationTests {
	
	@Autowired
	ServiceImpl service;
	
	Account account = new Account("Vinay","Kuyya","8885468999","m","vinay@gmail.com","BGTPT7490R","Vinayk21","VINAYKUY",85000);
	Account account1 = new Account("Vinay","Kuyya","8885468999","m","vinay@gmail.com","BGTPT7490P","Vinayk21","VINAYKUY",85000);
	/*Test Case:1 
	 * Description: Validating  correct user credentials
	 */
	@Test
	public void test1()
	{
		int validate = service.passwordValidate("ATHATIPA", "Adithya");
		assertEquals(1, validate);	
	}
	
	/*Test Case:2 
	 * Description: Validating  incorrect user credentials
	 */
	@Test
	public void test2()
	{
		assertThrows(InvalidUserNameOrPasswordException.class, ()->{ service.passwordValidate("VINESHKA", "Vinesh"); });
	}
	
	/*Test Case:3 
	 * Description: Checking Account Balance  with valid user credentials
	 */
	@Test
	public void test3() 
	{
		double balance = service.checkBalance("ATHATIPA");
		assertEquals(85200, balance,1);	
	}
	
	/*Test Case:4
	 * Description: Checking Account Balance with invalid user credentials
	 */
	@Test
	public void test4()
	{
		assertThrows(NoAccountFoundException.class, ()->{ service.checkBalance("VINESH"); });	
	}
	
	/*Test Case:5
	 * Description: Deposit Amount in Existing account 
	 */
	@Test
	public void test5()
	{
		double amount = service.depositAmount("ATHATIPA",50000);
		assertEquals(1, amount,1);		
	}
	
	/*Test Case:6
	 * Description: Deposit amount in an account which is not in database
	 */
	@Test
	public void test6()
	{
		assertThrows(NoAccountFoundException.class, ()->{ service.depositAmount("VINESHKA",50000);});	
	}
	
	/*Test Case:7 
	 * Description: Find Account which is not exist in database
	 */
	@Test
	public void test7() 
	{
		assertThrows(NoAccountFoundException.class, ()->{ service.findAccount("VINESHKA");});	
	}
	
	/*Test Case:8 
	 * Description: Applying Loan with valid data and again applying loan which throws LoanExistException
	 */
	@Test
	public void test8()
	{
		double loan = service.applyLoan("ATHATIPA", 500000, 700000, 5, "Home Loan");
		assertEquals(1,loan,1);
		assertThrows(LoanExistException.class, ()->{ service.applyLoan("ATHATIPA", 500000, 700000, 5, "Home Loan");});
	}
	
	/*Test Case:9 
	 * Description: Applying Loan with asset value not greater than 1.4 times of loan amount
	 */
	@Test
	public void test9()
	{
		assertThrows(LoanAmountException.class, ()->{ service.applyLoan("ATHATIPA", 1000000, 1100000, 5, "Home Loan");});
	}
	
	/*Test Case:10 
	 * Description: Applying Loan with asset value less than or equal to the loan amount
	 */
	@Test
	public void test10()
	{
		assertThrows(AssetValueException.class, ()->{ service.applyLoan("ATHATIPA", 500000, 500000, 5, "Home Loan");});
	}
	
	/*Test Case:11 
	 * Description: Applying Loan for an account which is not exist in database
	 */
	@Test
	public void test11() 
	{
		assertThrows(NoAccountFoundException.class, ()->{ service.applyLoan("VINESHKA", 500000, 600000, 5, "Home Loan");});	
	}
	
	/*Test Case:12
	 * Description: Applying Loan  whose account balance is less than required minimum balance(i.e.,50000)
	 */ 
	 /* @Test
	 * public void test12() 
	 * {
	 * assertThrows(InsufficientFundsException.class, ()->{ service.applyLoan("MAHATHIR", 500000, 600000, 5, "Home Loan");});	
	 * }
	 */ 
	
	/*Test Case:13
	 * Description: Pay EMI for an account having no existing pending loans.
	 */
	@Test
	public void test13()
	{
		assertThrows(NoLoanExistException.class, ()->{ service.payEmi("ATHATIPA");});
	}
	
	/*Test Case:14
	 * Description: Pay EMI for an account  which is not exist in database
	 */
	@Test
	public void test14()
	{
		assertThrows(NoAccountFoundException.class, ()->{service.payEmi("VINESHKA");});
	}
	
	/*Test Case:15
	 * Description: Pay EMI when user don't have enough funds in his account
	 */
	/* @Test public void test15() 
	 * { 
	 * 		assertThrows(InsufficientFundsException.class, ()->{ service.payEmi("ATHATIPA");}); 
	 * }
	 */

	/*Test Case:16
	 * Description: Pay EMI for an account having pending loan and have sufficient funds in the account to pay.
	 */
	/*
	 * @Test public void test16() 
	 * {
	 *   double payEmi = service.payEmi("ATHATIPA");
	 * 	 assertEquals(1,payEmi,1); 
	 * }
	 */
	
	/*Test Case:17
	 * Description: Foreclose a loan for an account which don't have existing pending loan
	 */
	@Test
	public void test17()
	{
		assertThrows(NoLoanExistException.class, ()->{ service.foreClose("ATHATIPA");});
	}
	
	/*Test Case:18
	 * Description: Foreclose a loan for an account which is not exist in database
	 */
	@Test
	public void test18()
	{
		assertThrows(NoAccountFoundException.class, ()->{service.foreClose("VINESHKA");});
	}
	
	/*Test Case:19
	 * Description: Foreclose a loan for an account not having sufficient funds to clear the loan
	 */
	/* @Test public void test19() 
	 * { 
	 * 		assertThrows(InsufficientFundsException.class, ()->{ service.foreClose("ATHATIPA");}); 
	 * }
	 */

	/*Test Case:20
	 * Description: Foreclose a loan for an account having pending loan and having sufficient balance in the account to clear the loan
	 */
	/* @Test public void test20() 
	 * { 
	 * double payEmi = service.foreClose("ATHATIPA");
	 * assertEquals(1,payEmi,1); 
	 * }
	 */
	
	/*Test Case:21
	 * Description: Changing password of an account with valid credentials
	 */
	@Test
	public void test21()
	{
		int changePassword = service.changePassword("ATHATIPA", "Ashok21");
		assertEquals(1, changePassword);
	}
	
	/*Test Case:22
	 * Description: Changing password for an account which is not in database
	 */
	@Test
	public void test22()
	{
		assertThrows(NoAccountFoundException.class, ()->{service.changePassword("VINESHKA","Vinesh21");});
	}
	
	/*Test Case:23
	 * Description: Validate account details when user forget password with valid inputs
	 */
	@Test
	public void test23()
	{
		String panNumber = service.accountValidate("ATHATIPA", 100001);
		assertEquals("BGTPT7490R", panNumber);
	}
	
	/*Test Case:24
	 * Description: Validate account details when user forget password with invalid inputs
	 */
	@Test
	public void test24()
	{
		assertThrows(NoAccountFoundException.class, ()->{service.accountValidate("VINESHKA",100008);});
	}
	
	/*Test Case:25
	 * Description: Creating account with valid inputs
	 */
	@Test
	public void test25()
	{
		int createAccount = service.addAccount(account1);
		assertEquals(1, createAccount);
	}
	
	/*Test Case:26
	 * Description: Creating account with inputs that are already in database
	 */
	@Test
	public void test26()
	{
		assertThrows(AccountAlreadyExistException.class, ()->{service.addAccount(account);});
	}
	
}
