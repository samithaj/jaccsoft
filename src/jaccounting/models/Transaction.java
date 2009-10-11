/*
 * Transaction.java		    1.0.0	    09/2009
 * This file contains the transaction model class of the JAccounting application.
 *
 * JAccounting - Basic Double Entry Accounting Software.
 * Copyright (c) 2009 Boubacar Diallo.
 *
 * This software is free: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this software.  If not, see http://www.gnu.org/licenses.
 */

package jaccounting.models;

import jaccounting.ErrorCode;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Transaction is the class representing an accounting transaction. A Transaction
 * object holds details such as the date and amount of a transaction. It also holds
 * TransactionEntry objects representing the its Debit and Credit sides.
 *
 * @author	    Boubacar Diallo
 * @version	    1.0.0
 * @see		    TransactionEntry
 * @see		    Account
 * @since	    1.0.0
 */
public class Transaction extends BaseModel {
    
    protected Date date;		    // the date of the transaction

    /** the voucher or reference document number of the transaction */
    protected String refNo;

    /** the description or textual details about the transaction */
    protected String memo;

    protected double amount;		    // the money amount of the transaction

    /** the TransactionEntry representing the Debit side of the transaction */
    protected TransactionEntry debitEntry;

    /** the TransactionEntry representing the Credit side of the transaction */
    protected TransactionEntry creditEntry;


    /**
     * Default no argument constructor; initializes {@code date} to the current
     * date, {@code refNo} and {@code memo} to empty strings, {@code debitEntry}
     * and {@code creditEntry} to new TransactionEntry objects.
     *
     * @see		    #Transaction(java.util.Date, java.lang.String,
     *					java.lang.String, double)
     * @since		    1.0.0
     */
    public Transaction() {
	this(new Date(), "", "", 0.0);
    }

    /**
     * Convenience constructor. This method sets each common property to the given
     * value and sets the {@code debitEntry} and {@code creditEntry} to new
     * TransactionEntry objects.
     *
     * @param date		the date of the transaction
     * @param refNo		the reference/voucher document number
     * @param memo		the textual details about the transaction
     * @param amount		the amount of the transaction
     * @see			#Transaction(java.util.Date, java.lang.String, 
     *					    java.lang.String, double, 
     *					    jaccounting.models.TransactionEntry, 
     *					    jaccounting.models.TransactionEntry)
     * @since			1.0.0
     */
    public Transaction(Date date, String refNo, String memo, double amount) {
	this(date, refNo, memo, amount,
		new TransactionEntry(null, null, TransactionEntry.Type.DEBIT, 0.0),
		new TransactionEntry(null, null, TransactionEntry.Type.CREDIT, 0.0));
>>>>>>> .r71
    }

    /**
     * Full argument constructor; initializes each property to the given value.
     *
     * @param date		    the date of the transaction
     * @param refNo		    the reference/voucher document number
     * @param memo		    the textual details about the transaction
     * @param amount		    the amount of the transaction
     * @param debitEntry	    the TransactionEntry representing the Debit
     *				    side of the transaction
     * @param creditEntry	    the TransactionEntry representing the Credit
     *				    side of the transaction
     * @see			    #set(java.util.Date, java.lang.String,
     *					    java.lang.String, double,
     *					    jaccounting.models.TransactionEntry,
     *					    jaccounting.models.TransactionEntry)
     * @since			    1.0.0
     */
    public Transaction(Date date, String refNo, String memo, double amount, 
	    TransactionEntry debitEntry, TransactionEntry creditEntry) {
	set(date, refNo, memo, amount, debitEntry, creditEntry);
    }


    /**
     * Creates a new Transaction object. This method sets the Transaction's
     * TransactionEntrys' {@code transaction} property to itself -note that
     * this can't be safely done in the constructors.
     *
     * @return		    the new Transaction object
     * @since		    1.0.0
     */
    public static Transaction createTransaction() {
	Transaction rTrans = new Transaction();

	rTrans.debitEntry.setTransaction(rTrans);
	rTrans.creditEntry.setTransaction(rTrans);

	return rTrans;
    }

    /**
     * Creates a new Transaction object. This method sets the Transaction's
     * properties to the given values. This method sets the Transaction's
     * TransactionEntrys'  {@code transaction} property to itself -note that
     * this can't be safely done in the constructors.
     *
     * @param date		    the date of the transaction
     * @param refNo		    the reference/voucher document number
     * @param memo		    the textual details about the transaction
     * @param amount		    the amount of the transaction
     * @param debitEntry	    the TransactionEntry representing the Debit
     *				    side of the transaction
     * @param creditEntry	    the TransactionEntry representing the Credit
     *				    side of the transaction
     * @return			    the new Transaction object with the given
     *				    property values
     * @since			    1.0.0
     */
    public static Transaction createTransaction(Date date, String refNo, 
		String memo, double amount, TransactionEntry debitEntry,
		TransactionEntry creditEntry) {
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

    /**
     * Gets the Account that this Transaction applies a Credit to. It is technically
     * the {@code debitEntry}'s {@code transferAccount}; the debitAccount also being
     * the Account that holds the {@code debitEntry} into its TransactionEntrys
     * list.
     *
     * @return		    the Account that this Transaction applies a Credit to.
     * @since		    1.0.0
     */
    public Account getCreditAccount() {
	return debitEntry.getTransferAccount();
    }

    /**
     * Gets the Account that this Transaction applies a Debit to. It is technically
     * the {@code creditEntry}'s {@code transferAccount}; the creditAccount also being
     * the Account that holds the {@code creditEntry} into its TransactionEntrys
     * list.
     *
     * @return		    the Account that this Transaction applies a Debit to.
     * @since		    1.0.0
     */
    public Account getDebitAccount() {
	return creditEntry.getTransferAccount();
    }

    /**
     * Sets this Transaction's properties to the specified values. This method is a
     * batch setter that can be used when initializing and updating a Transaction.
     *
     * @param date		    the date
     * @param refNo		    the refNo
     * @param memo		    the memo
     * @param amount		    the amount
     * @param debitEntry	    the Debit TransactionEntry
     * @param creditEntry	    the Credit TransactionEntry
     * @since			    1.0.0
     */
    protected void set(Date date, String refNo, String memo, double amount,
		    TransactionEntry debitEntry, TransactionEntry creditEntry) {
        this.date = date;
        this.refNo = refNo;
        this.memo = memo;
        this.amount = amount;
        this.debitEntry = debitEntry;
        this.creditEntry = creditEntry;
    }

    /**
     * Sets this Transaction's most common properties to the specified values. This
     * method is a convenience batch setter.
     *
     * @param date		    the date
     * @param refNo		    the refNo
     * @param memo		    the memo
     * @param amount		    the amount
     * @since			    1.0.0
     */
    protected void set(Date date, String refNo, String memo, double amount) {
        this.date = date;
        this.refNo = refNo;
        this.memo = memo;
        this.amount = amount;
    }

    /**
     * Updates this Transaction's properties and notifies its change observers.
     * This method first validates the new values to update to. It unposts this
     * Transaction from the Accounts it used to affect, sets the common properties
     * the new values, updates its Debit Account and Credit Account and finally
     * posts this Transaction to new Accounts it affects.
     *
     * @param date		    the date
     * @param refNo		    the refNo
     * @param memo		    the memo
     * @param amount		    the amount
     * @param debitAccount	    the Account to apply a Debit to
     * @param creditAccount	    the Account to apply a Credit to
     * @return			    a map of errors codes indexed by property
     *				    name from the validation attempt
     * @see			    #validate(double, jaccounting.models.Account, jaccounting.models.Account)
     * @see			    BaseModel#setChangedAndNotifyObservers()
     * @since			    1.0.0
     */
    public Map<String, ErrorCode> update(Date date, String refNo, String memo,
		double amount, Account debitAccount, Account creditAccount) {
	Map<String, ErrorCode> rErrors = validate(amount, debitAccount, 
							creditAccount);

	if (rErrors.isEmpty()) {
	    try {
		if (getCreditAccount() != null && getDebitAccount() != null) {
		    unpostTransaction();
		}

		set(date, refNo, memo, amount);
		debitEntry.setTransferAccount(creditAccount);
		creditEntry.setTransferAccount(debitAccount);
		postTransaction();

		setChangedAndNotifyObservers();
	    } 
	    catch (NotTransactionnableAccountException ex) {
		/**
		 * no need to throw this exception since it will be contained in
		 * the errors returned by the validation
		 */
		Logger.getLogger(Transaction.class.getName()).log(Level.SEVERE, null, ex);
	    }
	}

	return rErrors;
    }

    /**
     * Posts a Transaction to the Accounts it affects. This method simply applies
     * the amount of this Transaction to the affected Accounts balances by adding
     * its TransactionEntrys to those Accounts.
     *
     * @throws NotTransactionnableAccountException  if an Account does not
     *						    allow for Transactions to
     *						    modify its balance
     * @see					    #addEntriesToAccounts()
     * @since					    1.0.0
     */
    public void postTransaction() throws NotTransactionnableAccountException {
	addEntriesToAccounts();
    }

    /**
     * Unposts a Transaction from the Accounts it affected. This method simply
     * un-applies the amount of this Transaction to the affected Accounts balances
     * by removing its TransactionEntrys from those Accounts.
     *
     * @throws NotTransactionnableAccountException  if an Account does not
     *						    allow for Transactions to
     *						    modify its balance
     * @see					    #removeEntriesFromAccounts()
     * @since					    1.0.0
     */
    public void unpostTransaction() throws NotTransactionnableAccountException {
	removeEntriesFromAccounts();
    }

    /**
     * Removes this Transaction's TransactionEntrys from Accounts it affected.
     *
     * @throws NotTransactionnableAccountException  if an Account does not
     *						    allow for Transactions to
     *						    modify its balance in the
     *						    first place)
     * @see					    Account#removeEntry
     *					    (jaccounting.models.TransactionEntry)
     * @since					    1.0.0
     */
    protected void removeEntriesFromAccounts() throws NotTransactionnableAccountException {
	getDebitAccount().removeEntry(debitEntry);
	getCreditAccount().removeEntry(creditEntry);
    }

    /**
     * Adds this Transaction's TransactionEntrys to the Accounts it affects.
     *
     * @throws NotTransactionnableAccountException  if an Account does not
     *						    allow for Transactions to
     *						    modify its balance
     * @see					    Account#addEntry
     *					    (jaccounting.models.TransactionEntry)
     * @since					    1.0.0
     */
    protected void addEntriesToAccounts() throws NotTransactionnableAccountException {
	getDebitAccount().addEntry(debitEntry);
	getCreditAccount().addEntry(creditEntry);
    }

    /**
     * Validates possible values of a Transaction's properties. {@code amount}
     * must be positive and the Accounts that a Transaction is supposed to affect
     * must allow for it in the first place.
     *
     * @param amount		    the amount of the Transaction
     * @param debitAccount	    the Account to apply a Debit to
     * @param creditAccount	    the Account to apply a Credit to
     * @return			    a map of errors codes indexed by property
     *				    name
     * @since			    1.0.0
     */
    public static Map<String, ErrorCode> validate(double amount, Account debitAccount, Account creditAccount) {
	Map<String, ErrorCode> rErrors = new HashMap<String, ErrorCode>();

	if (amount < 0) rErrors.put("amount", ErrorCode.NEGATIVE_TRANSACTION_AMOUNT);
	if (!debitAccount.isTransactionsEnabled()) rErrors.put("debitAccount", ErrorCode.NOT_TRANSACTIONNABLE_ACCOUNT);
	if (!creditAccount.isTransactionsEnabled()) rErrors.put("creditAccount", ErrorCode.NOT_TRANSACTIONNABLE_ACCOUNT);
	
>>>>>>> .r71
	return rErrors;
    }
 
}
