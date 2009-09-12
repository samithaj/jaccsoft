/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jaccounting.models;

/**
 *
 * @author bouba
 */
public class RevenueAccount extends Account {
    
    protected RevenueAccount() {
	this(-1, "", "", 0.0, true);
    }

    protected RevenueAccount(int number, String name, String description,
                               double balance, boolean allowTransactions) {
        super(number, name, description, balance, Type.REVENUE, allowTransactions);
    }

    protected void applyDebit(double pAmount) {
	balance -= pAmount;
    }

    protected void applyCredit(double pAmount) {
	balance += pAmount;
    }
}
