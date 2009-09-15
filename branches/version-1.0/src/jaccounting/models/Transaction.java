/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jaccounting.models;

import jaccounting.exceptions.ErrorCode;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author bouba
 */
public class Transaction extends BaseModel {
    
    protected Date date;

    protected String refNo;

    protected String memo;

    protected double amount;

    protected TransactionEntry debitEntry;

    protected TransactionEntry creditEntry;

    /*protected Transaction(Date date, String refNo, String memo,
                              double amount, TransactionEntry debitEntry,
                              TransactionEntry creditEntry) {
	initProperties(date, refNo, memo, amount, debitEntry, creditEntry);
    }*/

    protected Transaction() {
	initProperties(new Date(), "", "", 0.0, new TransactionEntry(null, null, TransactionEntry.Type.DEBIT, 0.0),
			new TransactionEntry(null, null, TransactionEntry.Type.CREDIT, 0.0));
    }

    public Transaction(Date date, String refNo, String memo, double amount, TransactionEntry debitEntry,
			TransactionEntry creditEntry) {
	initProperties(date, refNo, memo, amount, debitEntry, creditEntry);
	
    }

    public static Transaction createTransaction() {
	return new Transaction();
    }

    public double getAmount() {
        return amount;
    }

    public TransactionEntry getCreditEntry() {
        return creditEntry;
    }

    public Date getDate() {
        return date;
    }

    public TransactionEntry getDebitEntry() {
        return debitEntry;
    }

    public String getMemo() {
        return memo;
    }

    public String getRefNo() {
        return refNo;
    }

    public Account getCreditAccount() {
	return debitEntry.getTransferAccount();
    }

    public Account getDebitAccount() {
	return creditEntry.getTransferAccount();
    }

    protected void initProperties(Date date, String refNo, String memo,
                              double amount, TransactionEntry debitEntry,
                              TransactionEntry creditEntry) {
        this.date = date;
        this.refNo = refNo;
        this.memo = memo;
        this.amount = amount;
        this.debitEntry = debitEntry;
        this.creditEntry = creditEntry;
	this.debitEntry.transaction = this;
	this.creditEntry.transaction = this;
    }

    public Map<String, ErrorCode> updateProperties(Date date, String refNo, String memo,
							  double amount, Account debitAccount,
							  Account creditAccount) {
	Map<String, ErrorCode> rErrors = validatePropertyValues(amount, debitAccount, creditAccount);
	if (rErrors.isEmpty()) {
	    // update fields
	    this.date = date;
	    this.refNo = refNo;
	    this.memo = memo;
	    this.amount = amount;
	    
	    // update the credit account to its new value
	    this.debitEntry.setTransferAccount(creditAccount);
	    // update debit account
	    this.creditEntry.setTransferAccount(debitAccount);
	    // remove the credit entry from the current credit account
	    /*this.debitEntry.getTransferAccount().removeEntry(this.creditEntry);
	    // add the credit entry to the new credit account
	    this.debitEntry.getTransferAccount().addEntry(this.creditEntry); // <=> creditAccount.addEntry(...)
	    // update the debit aocount's entries
	    this.creditEntry.getTransferAccount().removeEntry(this.debitEntry);
	    this.creditEntry.getTransferAccount().addEntry(this.debitEntry);*/
	    removeEntriesFromAccounts();
	    addEntriesToAccounts();
	    
	    // notify observers
	    setChangedAndNotifyObservers();
	}
	return rErrors;
    }

    public void removeEntriesFromAccounts() {
	getDebitAccount().removeEntry(debitEntry);
	getCreditAccount().removeEntry(creditEntry);
    }

    protected void addEntriesToAccounts() {
	getDebitAccount().addEntry(debitEntry);
	getCreditAccount().addEntry(creditEntry);
    }

    public Map<String, ErrorCode> validatePropertyValues(double amount, Account debitAccount, Account creditAccount) {
	Map<String, ErrorCode> rErrors = new HashMap<String, ErrorCode>();

	if (amount < 0) rErrors.put("amount", ErrorCode.NEGATIVE_TRANSACTION_AMOUNT);
	if (!debitAccount.isTransactionsEnabled()) rErrors.put("debitAccount", ErrorCode.NOT_TRANSACTIONNABLE_ACCOUNT);
	if (!creditAccount.isTransactionsEnabled()) rErrors.put("creditAccount", ErrorCode.NOT_TRANSACTIONNABLE_ACCOUNT);
	// ??? make sure accounts are not equal although not necessary

	return rErrors;
    }
 
}
