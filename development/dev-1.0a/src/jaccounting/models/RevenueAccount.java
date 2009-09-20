/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jaccounting.models;

import java.util.List;

/**
 *
 * @author bouba
 */
public class RevenueAccount extends Account {
    
    public RevenueAccount() {
	super();
	type = Type.REVENUE;
    }

    public RevenueAccount(int number, String name, String description, double balance,
	    boolean transactionsEnabled) {
	super(number, name, description, balance, transactionsEnabled);
	type = Type.REVENUE;
    }

    public RevenueAccount(int number, String name, String description,
                               double balance, boolean allowTransactions,
			       List<TransactionEntry> entries) {
        super(number, name, description, balance, allowTransactions, entries);
	type = Type.REVENUE;
    }

    protected void applyDebit(double pAmount) {
	balance -= pAmount;
    }

    protected void applyCredit(double pAmount) {
	balance += pAmount;
    }
}
