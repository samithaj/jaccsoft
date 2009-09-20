/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jaccounting.models;

import jaccounting.exceptions.ErrorCode;
import jaccounting.exceptions.NotTransactionnableAccountException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

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


    public Transaction() {
	this(new Date(), "", "", 0.0,
		new TransactionEntry(null, null, TransactionEntry.Type.DEBIT, 0.0),
		new TransactionEntry(null, null, TransactionEntry.Type.CREDIT, 0.0));
    }

    public Transaction(Date date, String refNo, String memo, double amount) {
	this(date, refNo, memo, amount,
		new TransactionEntry(null, null, TransactionEntry.Type.DEBIT, 0.0),
		new TransactionEntry(null, null, TransactionEntry.Type.CREDIT, 0.0));
    }

    public Transaction(Date date, String refNo, String memo, double amount, 
	    TransactionEntry debitEntry, TransactionEntry creditEntry) {
	set(date, refNo, memo, amount, debitEntry, creditEntry);
    }

    public static Transaction createTransaction() {
	Transaction rTrans = new Transaction();

	rTrans.debitEntry.setTransaction(rTrans);
	rTrans.creditEntry.setTransaction(rTrans);

	return rTrans;
    }

    public static Transaction createTransaction(Date date, String refNo, String memo, double amount,
	    TransactionEntry debitEntry, TransactionEntry creditEntry) {
	Transaction rTrans = new Transaction(date, refNo, memo, amount, debitEntry, creditEntry);

	rTrans.debitEntry.setTransaction(rTrans);
	rTrans.creditEntry.setTransaction(rTrans);

	return rTrans;
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

    protected void set(Date date, String refNo, String memo,
                              double amount, TransactionEntry debitEntry,
                              TransactionEntry creditEntry) {
        this.date = date;
        this.refNo = refNo;
        this.memo = memo;
        this.amount = amount;
        this.debitEntry = debitEntry;
        this.creditEntry = creditEntry;
    }

    protected void set(Date date, String refNo, String memo, double amount) {
        this.date = date;
        this.refNo = refNo;
        this.memo = memo;
        this.amount = amount;
    }

    public Map<String, ErrorCode> update(Date date, String refNo, String memo,
							  double amount, Account debitAccount,
							  Account creditAccount) {
	Map<String, ErrorCode> rErrors = validateValues(amount, debitAccount, creditAccount);
	if (rErrors.isEmpty()) {
	    try {
		// update fields
		set(date, refNo, memo, amount);
		if (getCreditAccount() != null && getDebitAccount() != null) {
		    unpostTransaction();
		}
		// update the credit account to its new value
		debitEntry.setTransferAccount(creditAccount);
		// update debit account
		creditEntry.setTransferAccount(debitAccount);
		// update entries accounts
		postTransaction();
		// notify observers
		setChangedAndNotifyObservers();
	    } catch (NotTransactionnableAccountException ex) {
		Logger.getLogger(Transaction.class.getName()).log(Level.SEVERE, null, ex);
	    }
	}
	return rErrors;
    }

    public void postTransaction() throws NotTransactionnableAccountException {
	addEntriesToAccounts();
    }

    public void unpostTransaction() throws NotTransactionnableAccountException {
	removeEntriesFromAccounts();
    }

    protected void removeEntriesFromAccounts() throws NotTransactionnableAccountException {
	getDebitAccount().removeEntry(debitEntry);
	getCreditAccount().removeEntry(creditEntry);
    }

    protected void addEntriesToAccounts() throws NotTransactionnableAccountException {
	getDebitAccount().addEntry(debitEntry);
	getCreditAccount().addEntry(creditEntry);
    }

    public Map<String, ErrorCode> validateValues(double amount, Account debitAccount, Account creditAccount) {
	Map<String, ErrorCode> rErrors = new HashMap<String, ErrorCode>();

	if (amount < 0) rErrors.put("amount", ErrorCode.NEGATIVE_TRANSACTION_AMOUNT);
	if (!debitAccount.isTransactionsEnabled()) rErrors.put("debitAccount", ErrorCode.NOT_TRANSACTIONNABLE_ACCOUNT);
	if (!creditAccount.isTransactionsEnabled()) rErrors.put("creditAccount", ErrorCode.NOT_TRANSACTIONNABLE_ACCOUNT);
	
	return rErrors;
    }
 
}
