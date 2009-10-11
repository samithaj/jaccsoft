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
<<<<<<< .mine

    protected RevenueAccount() {
	this(-1, "", "", 0.0, true);
=======
    
    protected RevenueAccount() {
	this(-1, "", "", 0.0, true);
>>>>>>> .r71
    }

    protected RevenueAccount(int number, String name, String description,
                               double balance, boolean allowTransactions) {
        super(number, name, description, balance, Type.REVENUE, allowTransactions);
    }

    @Override
    protected void applyDebit(double pAmount) {
	balance -= pAmount;
    }

    @Override
    protected void applyCredit(double pAmount) {
	balance += pAmount;
    }
}
