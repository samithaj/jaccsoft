/*
 * AssetAccount.java	    1.0.0	    09/2009
 * This file contains the asset account model class of the JAccounting application.
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

import java.util.List;

/**
 * AssetAccount is the class for Account objects of type ASSET. An AssetAccount
 * represents an accounting asset account where Debits increase its balance
 * and Credits decrease its balance.
 *
 * @author	    Boubacar Diallo
 * @version	    1.0.0
 * @see		    TransactionEntry
 * @see		    Account
 * @since	    1.0.0
 */
public class AssetAccount extends Account {

    /**
     * Default no argument constructor. Calls the corresponding super constructor
     * and sets its type to ASSET.
     *
     * @see		    Account#Account()
     * @since		    1.0.0
     */
    public AssetAccount() {
	super();
	type = Type.ASSET;
    }

    /**
     * Convenience constructor. Calls the corresponding super constructor and
     * sets its type to ASSET.
     *
     * @param number			the account number
     * @param name			the account name
     * @param description		the account description
     * @param balance			the account balance
     * @param transactionsEnabled	the transactions enabling flag
     * @see				Account#Account(int, java.lang.String,
     *					    java.lang.String, double, boolean)
     * @since				1.0.0
     */
    public AssetAccount(int number, String name, String description, double balance,
			boolean transactionsEnabled) {
	super(number, name, description, balance, transactionsEnabled);
	type = Type.ASSET;
    }

    /**
     * Full argument constructor. Calls the corresponding super constructor and
     * sets its type to ASSET.
     *
     * @param number		    the account number
     * @param name		    the account name
     * @param description	    the account description
     * @param balance		    the account balance
     * @param allowTransactions	    the transactions enabling flag
     * @param entries		    the account TransactionEntry objects list
     * @see			    Account#Account(int, java.lang.String,
     *						    java.lang.String, double,
     *						    boolean, java.util.List)
     * @since			    1.0.0
     */
    public AssetAccount(int number, String name, String description,
                        double balance, boolean allowTransactions,
			List<TransactionEntry> entries) {
        super(number, name, description, balance, allowTransactions, entries);
	type = Type.ASSET;
    }

    
    /**
     * Applies a Debit amount to this Account's balance. This method increases
     * the balance by the Debit amount.
     *
     * @param pAmount		    the amount to apply as a Debit
     * @since			    1.0.0
     */
    protected void applyDebit(double pAmount) {
	balance += pAmount;
    }

    /**
     * Applies a Credit amount to this Account's balance. This method decreases
     * the balance by the Credit amount.
     *
     * @param pAmount		    the amount to apply as a Credit
     * @since			    1.0.0
     */
    protected void applyCredit(double pAmount) {
	balance -= pAmount;
    }
}
