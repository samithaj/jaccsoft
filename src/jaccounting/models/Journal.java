/*
 * Journal.java		    1.0.0	    09/2009
 * This file contains the journal model class of the JAccounting application.
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;

/**
 * Journal is the class representing an accounting journal. A Journal object holds
 * and manages the list of Transactions.
 *
 * @author	    Boubacar Diallo
 * @version	    1.0.0
 * @see		    Transaction
 * @since	    1.0.0
 */
public class Journal extends BaseModel {

    protected List<Transaction> transactions;


    /**
     * No argument constructor; initialiazes the {@code transactions} property to an empty
     * list.
     *
     * @since		    1.0.0
     */
    public Journal() {
        transactions = new ArrayList<Transaction>();
    }

    /**
     * Full argument constructor; initializes the {@code transactions} property
     * to the given list.
     *
     * @param transactions	the list of Transactions
     * @since			1.0.0
     */
    public Journal(List<Transaction> transactions) {
	this.transactions = transactions;
    }



    public List<Transaction> getTransactions() {
	return transactions;
    }

    /**
     * Adds a Transaction to this Journal. This method inserts the new Transaction
     * at the proper position to keep the ordering by date and notifies the change
     * observers.
     *
     * @param pTransaction	the Transaction to add
     * @see			#getIndexOfFirstTransactionLaterThan(java.util.Date)
     * @see			BaseModel#setChangedAndNotifyObservers()
     * @since			1.0.0
     */
    public void addTransaction(Transaction pTransaction) {
	int vIndex = getIndexOfFirstTransactionLaterThan(pTransaction.getDate());

	if (vIndex != -1) {
	    transactions.add(vIndex, pTransaction);
	}
	else {
	    transactions.add(pTransaction);
	}

	setChangedAndNotifyObservers();
    }

    /**
     * Gets the index of the first Transaction whose date is later than the given
     * date.
     *
     * @param pDate		the Date to compare against
     * @return			the index or -1
     * @since			1.0.0
     */
    protected int getIndexOfFirstTransactionLaterThan(Date pDate) {
	int rIndex = -1;
	int vCurrIndex = -1;
	Transaction vTrans;
	ListIterator vIt = transactions.listIterator();

	while (vIt.hasNext()) {
	    vCurrIndex++;
	    vTrans = (Transaction)vIt.next();
	    if (vTrans.getDate().after(pDate)) {
		rIndex = vCurrIndex;
		break;
	    }
	}

	return rIndex;
    }

    /**
     * Gets the Transaction at the given index.
     *
     * @param pIndex		the index
     * @return			the Transaction
     * @since			1.0.0
     */
    public Transaction getTransaction(int pIndex) {
	return transactions.get(pIndex);
    }

    /**
     * Removes the Transaction at the given row from this Journal. This method
     * uposts the Transaction i.e. removes the connection between the Transaction
     * the Accounts it affected, then notifies the change observers.
     *
     * @param pRow		the index of the Transaction to remove
     * @return			true if the Transaction removal was successful;
     *				false otherwise
     * @see			Transaction#unpostTransaction()
     * @see			BaseModel#setChangedAndNotifyObservers()
     * @since			1.0.0
     */
    public boolean removeTransaction(int pRow) {
	Transaction vTrans;

	if ((vTrans=transactions.remove(pRow)) != null) {
	    try {
		vTrans.unpostTransaction();
		setChangedAndNotifyObservers();

		return true;
	    }
	    catch (NotTransactionnableAccountException ex) { }
>>>>>>> .r71
	}

	return false;
    }

    /**
     * Removes a bunch of Transactions from this Journal and notifies the change
     * observers. This method unposts each Transaction first.
     *
     * @param pTransactions	    the list of Transactions to be removed
     * @see			    Transaction#unpostTransaction()
     * @see			    BaseModel#setChangedAndNotifyObservers()
     * @since			    1.0.0
     */
    public void removeTransactions(List<Transaction> pTransactions) {
	Transaction vTrans;
	ListIterator vIt = pTransactions.listIterator();

	while (vIt.hasNext()) {
	    vTrans = (Transaction) vIt.next();
	    if (transactions.remove(vTrans)) {
		try {
		    vTrans.unpostTransaction();
		}
		catch (NotTransactionnableAccountException ex) { }
		}
=======
		vTrans.removeEntriesFromAccounts();
>>>>>>> .r71
	    }

	setChangedAndNotifyObservers();
    }

}
