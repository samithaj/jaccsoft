/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jaccounting.models;

/**
 *
 * @author bouba
 */
public class TransactionEntry extends BaseModel {
    
    public static enum Type { DEBIT, CREDIT };

    protected Account transferAccount;

    protected Transaction transaction;

    protected Type type;

    protected double accountBalance;

    public TransactionEntry(Account transferAccount, Transaction transaction, Type type, double accountBalance) {
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

    /*void setTransaction(Transaction transaction) {
	this.transaction = transaction;
    }*/

    protected void setTransferAccount(Account transferAccount) {
	this.transferAccount = transferAccount;
    }

    protected void setAccountBalance(double accountBalance) {
	this.accountBalance = accountBalance;
    }

    
}
