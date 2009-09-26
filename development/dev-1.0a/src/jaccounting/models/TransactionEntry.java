/*
 * TransactionEntry.java		    1.0.0	    09/2009
 * This file contains the transaction entry model class of the JAccounting application.
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

/**
 * TransactionEntry is the class representing a Debit or Credit side of a transaction.
 * A TransactionEntry object holds the Account to which to apply a Credit or a
 * Debit as a result of a transaction. It is stored both in a Transaction object
 * and in an Account object as part of that Account's list of TransactionEntrys
 * that affected its balance. It also holds the updated balance of the Account
 * it was added and applied to for history purposes of that Account's balance.
 *
 * @author	    Boubacar Diallo
 * @version	    1.0.0
 * @see		    Transaction
 * @see		    Account
 * @since	    1.0.0
 */
public class TransactionEntry extends BaseModel {
    
    public static enum Type { DEBIT, CREDIT };

    /**
     * the counter-Account of the Account that this TransactionEntry's was added
     * to and applied to; i.e. the other Account holding this TransactionEntry's
     * counter-TransactionEntry
     */
    private Account transferAccount;

    /** the Transaction to which this TransactionEntry belongs to */
    private Transaction transaction;

    /** the type of this TransactionEntry */
    private Type type;

    /**
     * the resulting Account balance after applying this TransactionEntry to the
     * Account that holds it
     */
    private double accountBalance;


    /**
     * No argument constructor. Does nothing since the default values wanted for
     * a TransactionEntry match the ones given to unitialized instance variables
     * by Java.
     *
     * @since		    1.0.0
     */
    public TransactionEntry() {
    }

    /**
     * Full argument constructor. initializes the properties to the given values.
     *
     * @param transferAccount		the counter-Account of this TransactionEntry
     * @param transaction		the Transaction this TransactionEntry
     *					belongs to
     * @param type			the TransactionEntry type
     * @param accountBalance		the balance after this TransactionEntry
     *					was applied to the Account holding it
     * @since				1.0.0
     */
    public TransactionEntry(Account transferAccount, Transaction transaction,
			    Type type, double accountBalance) {
        this.transferAccount = transferAccount;
        this.transaction = transaction;
        this.type = type;
	this.accountBalance = accountBalance;
    }


    public Transaction getTransaction() {
        return transaction;
    }

    public Account getTransferAccount() {
        return transferAccount;
    }

    public Type getType() {
        return type;
    }

    public double getAccountBalance() {
	return accountBalance;
    }

    void setTransaction(Transaction transaction) {
	this.transaction = transaction;
    }

    void setTransferAccount(Account transferAccount) {
	this.transferAccount = transferAccount;
    }

    void setAccountBalance(double accountBalance) {
	this.accountBalance = accountBalance;
    }

    /**
     * Sets the {@code transferAccount} property of this TransactionEntry only if
     * it is not set. This is convenience method for problems when upersisting;
     * at that stage it is initially problematic to unpersist an Account while
     * upersisting a TransactionEntry as it leads to dangerous recursion; so the
     * {@code transferAccount} is left {@code null} first then in a second pass,
     * is properly set through this method.
     *
     * @param transferAccount		the counter-Account of this TransactionEntry
     * @since				1.0.0
     */
    public void initTransferAccount(Account transferAccount) {
	if (this.transferAccount == null) setTransferAccount(transferAccount);
    }
    
}
