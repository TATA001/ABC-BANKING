create table Account(account_Number number(6) primary key, firstName varchar2(30), lastName varchar2(30), userId varchar2(10),
phone_Number varchar2(10),sex varchar2(2), email varchar2(30), userName varchar2(8), password varchar2(12), balance_Amount number, asset_Value number,
loan_Type varchar2(20), loan_Amount number, emi number,transaction_Count number(5)); 

create table Transactions (transaction_Number varchar2(10), transaction_Time varchar2(20), account_Number number(6), description varchar2(20),
transaction_Id number(5),credit number, debit number, balance number);
