/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jaccounting.models;

/**
 *
 * @author bouba
 */
public class LiabilityAccount extends Account {
<<<<<<< .mine
    protected LiabilityAccount() {
		this(-1, "", "", 0.0, true);
=======
    protected LiabilityAccount() {
		this(-1, "", "", 0.0, true);
	}

    protected LiabilityAccount(int number, String name, String description,
                               double balance, boolean allowTransactions) {
        super(number, name, description, balance, Type.LIABILITY, allowTransactions);
>>>>>>> .r71
    }

<<<<<<< .mine
    protected LiabilityAccount(int number, String name, String description,
                               double balance, boolean allowTransactions) {
        super(number, name, description, balance, Type.LIABILITY, allowTransactions);
    }

    @Override
=======
    @Override
>>>>>>> .r71
    protected void applyDebit(double pAmount) {
	balance -= pAmount;
    }

    @Override
    protected void applyCredit(double pAmount) {
	balance += pAmount;
    }
}
