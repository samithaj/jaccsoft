/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jaccounting.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;

/**
 *
 * @author bouba
 */
public class Journal extends BaseModel {

    protected List<Transaction> transactions;

    public Journal() {
        transactions = new ArrayList<Transaction>();
    }

    public Journal(List<Transaction> transactions) {
	this.transactions = transactions;
    }

    public List<Transaction> getTransactions() {
	return transactions;
    }

    public void addTransaction(Transaction pTransaction) {
	int vIndex = getIndexOfFirstTransactionLaterThan(pTransaction.getDate());
	if (vIndex != -1) {
	    transactions.add(vIndex, pTransaction);
	} else {
	    transactions.add(pTransaction);
	}
	setChangedAndNotifyObservers();
    }

    private int getIndexOfFirstTransactionLaterThan(Date pDate) {
	int rIndex = -1;
	int vCurrIndex = -1;
	Transaction vTrans;
	ListIterator vIt = transactions.listIterator();

	// loop through and compare dates
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

    public Transaction getTransaction(int pIndex) {
	return transactions.get(pIndex);
    }

    public void removeTransaction(int vRow) {
	Transaction vTrans;
	if ((vTrans=transactions.remove(vRow)) != null) {
	    vTrans.removeEntriesFromAccounts();
	    setChangedAndNotifyObservers();
	}
    }

    public void removeTransactions(List<Transaction> pTransactions) {
	Transaction vTrans;
	ListIterator vIt = transactions.listIterator();

	while (vIt.hasNext()) {
	    vTrans = (Transaction) vIt.next();
	    if (transactions.remove(vTrans)) {
		vTrans.removeEntriesFromAccounts();
	    }
	}

	setChangedAndNotifyObservers();
    }

}
