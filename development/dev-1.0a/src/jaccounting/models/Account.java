/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jaccounting.models;

import jaccounting.exceptions.ErrorCode;
import jaccounting.exceptions.InvalidAccountTypeException;
import jaccounting.exceptions.NotTransactionnableAccountException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author bouba
 */
public abstract class Account extends BaseModel {

    public static enum Type {
	ASSET, LIABILITY, EXPENSE, REVENUE, EQUITY
    }

    protected int number;
    protected String name;
    protected String description;
    protected double balance;
    protected Type type;
    protected boolean transactionsEnabled;
    protected List<TransactionEntry> entries;

    public Account() {
	this(-1, "", "", 0.0, true, new ArrayList<TransactionEntry>());
    }

    public Account(int number, String name, String description, double balance,
	    boolean transactionsEnabled) {
	this(number, name, description, balance, transactionsEnabled,
		new ArrayList<TransactionEntry>());
    }

    public Account(int number, String name, String description,
	    double balance, boolean allowTransactions,
	    List<TransactionEntry> entries) {
	set(number, name, description, balance, null, allowTransactions, entries);
    }

    public double getBalance() {
	return balance;
    }

    public String getDescription() {
	return description;
    }

    public List< TransactionEntry> getEntries() {
	return entries;
    }

    public String getName() {
	return name;
    }

    public int getNumber() {
	return number;
    }

    public Type getType() {
	return type;
    }

    public boolean isTransactionsEnabled() {
	return transactionsEnabled;
    }

    public void setType(Type type) {
	this.type = type;
    }

    public static Account createAccount(Type pType) throws InvalidAccountTypeException {
	switch (pType) {
	    case ASSET:
		return (Account) new AssetAccount();
	    case EQUITY:
		return (Account) new EquityAccount();
	    case EXPENSE:
		return (Account) new ExpenseAccount();
	    case LIABILITY:
		return (Account) new LiabilityAccount();
	    case REVENUE:
		return (Account) new RevenueAccount();
	    default:
		throw new InvalidAccountTypeException();
	}
    }

    public static Account createAccount(int number, String name, String description,
	    double balance, Type type, List<TransactionEntry> entries,
	    boolean allowTransactions) throws InvalidAccountTypeException {
	Account rAccount = null;

	switch (type) {
	    case ASSET:
		rAccount = new AssetAccount();
		break;
	    case EQUITY:
		rAccount = new EquityAccount();
		break;
	    case EXPENSE:
		rAccount = new ExpenseAccount();
		break;
	    case LIABILITY:
		rAccount = new LiabilityAccount();
		break;
	    case REVENUE:
		rAccount = new RevenueAccount();
		break;
	}
	if (rAccount == null) throw new InvalidAccountTypeException();
	rAccount.set(number, name, description, balance, type, allowTransactions, entries);

	return (Account) rAccount;
    }

    protected void set(int number, String name, String description,
	    double balance, Type type, boolean allowTransactions, List<TransactionEntry> entries) {
	if (entries != null && !allowTransactions && entries.size() > 0) {
	    Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Attempted to set non empty entries list for not transactionable account: " + name);
	    entries = new ArrayList<TransactionEntry>();
	}
	this.number = number;
	this.name = name;
	this.description = description;
	this.balance = balance;
	this.type = type;
	this.transactionsEnabled = allowTransactions;
	this.entries = entries;
    }

    protected void set(int number, String name, String description) {
	this.number = number;
	this.name = name;
	this.description = description;
    }

    public List<Transaction> getAccountTransactions() {
	List<Transaction> rTransactions = new ArrayList<Transaction>();
	TransactionEntry vEntry;
	ListIterator vIt = entries.listIterator();

	while (vIt.hasNext()) {
	    vEntry = (TransactionEntry) vIt.next();
	    rTransactions.add(vEntry.getTransaction());
	}

	return rTransactions;
    }

    public Map<String, ErrorCode> update(int number, String name, String description) {
	Map<String, ErrorCode> rErrors = validateValues(number, name, description);
	if (rErrors.isEmpty()) {
	    // update fields
	    set(number, name, description);
	    // notify observers
	    setChangedAndNotifyObservers();
	}
	return rErrors;
    }

    public Map<String, ErrorCode> validateValues(int number, String name, String description) {
	Map<String, ErrorCode> rErrors = new HashMap<String, ErrorCode>();

	if (number < -1) {
	    rErrors.put("number", ErrorCode.NEGATIVE_ACCOUNT_NUMBER);
	}
	if (name.length() <= 0) {
	    rErrors.put("name", ErrorCode.EMPTY_ACCOUNT_NAME);
	} else if (!name.matches("(\\w+\\s?)+")) {
	    rErrors.put("name", ErrorCode.NOT_ALPHANUMERIC_ACCOUNT_NAME);
	}

	return rErrors;
    }

    public void addEntry(TransactionEntry pEntry) throws NotTransactionnableAccountException {
	if (!transactionsEnabled) throw new NotTransactionnableAccountException();
	// get the index of the first entry whose date is bigger than this entry's
	int vIndex = getIndexOfFirstEntryLaterThan(pEntry.getTransaction().getDate());
	// insert at that index
	if (vIndex != -1) {
	    entries.add(vIndex, pEntry);
	// or add at the end
	} else {
	    entries.add(pEntry);
	    vIndex = 0;
	}
	// apply entry to balance
	applyLaterEntriesToBalanceStartingAt(vIndex);
	setChangedAndNotifyObservers();
    }

    protected void applyLaterEntriesToBalanceStartingAt(int pIndex) {	
	ListIterator vIt;

	if (pIndex > 0) {
	    vIt = entries.listIterator(pIndex-1);
	    balance = ((TransactionEntry) vIt.next()).getAccountBalance();
	} else {
	    vIt = entries.listIterator(pIndex);
	    balance = 0.0;
	}
	while (vIt.hasNext()) {
	    applyEntryToBalance((TransactionEntry) vIt.next());
	}
    }

    protected void applyEntryToBalance(TransactionEntry pEntry) {
	double vAmount = pEntry.getTransaction().getAmount();
	switch (pEntry.getType()) {
	    case DEBIT:
		applyDebit(vAmount);
		break;
	    case CREDIT:
		applyCredit(vAmount);
		break;
	}
	pEntry.setAccountBalance(balance);
    }

    protected abstract void applyDebit(double pAmount);

    protected abstract void applyCredit(double pAmount);

    public void removeEntry(TransactionEntry pEntry) throws NotTransactionnableAccountException {
	if (!transactionsEnabled) throw new NotTransactionnableAccountException();
	int vIndex = entries.indexOf(pEntry);
	if (entries.remove(pEntry)) {
	    if (vIndex > 0) vIndex--;
	    applyLaterEntriesToBalanceStartingAt(vIndex);
	    setChangedAndNotifyObservers();
	}
    }

    protected int getIndexOfFirstEntryLaterThan(Date pDate) {
	int rIndex = -1;
	int vCurrIndex = -1;
	boolean vFound = false;
	TransactionEntry vEntry;
	ListIterator vIt = entries.listIterator();

	// loop through and compare dates
	while (vIt.hasNext() && !vFound) {
	    vCurrIndex++;
	    vEntry = (TransactionEntry)vIt.next();
	    if (vEntry.getTransaction().getDate().after(pDate)) {
		rIndex = vCurrIndex;
		vFound = true;
	    }
	}

	return rIndex;
    }

    protected int getIndexOfFirstEntryLaterThan(TransactionEntry pEntry) {
	return getIndexOfFirstEntryLaterThan(pEntry.getTransaction().getDate());
    }

}
