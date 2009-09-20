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

    private Account transferAccount;

    private Transaction transaction;

    private Type type;

    private double accountBalance;

    public TransactionEntry() {
    }

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

    void setTransaction(Transaction transaction) {
	this.transaction = transaction;
    }

    void setTransferAccount(Account transferAccount) {
	this.transferAccount = transferAccount;
    }

    void setAccountBalance(double accountBalance) {
	this.accountBalance = accountBalance;
    }

    public void initTransferAccount(Account transferAccount) {
	if (this.transferAccount == null) setTransferAccount(transferAccount);
    }
    
}
