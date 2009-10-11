/*
 * Account.java	    1.0.0	    09/2009
 * This file contains the account model class of the JAccounting application.
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

import jaccounting.ErrorCode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Account is the base class for all account models of the application. An Account
 * object represents an accounting account with a name, description and balance
 * among main propeties. It also holds a list of TransactionEntry objects that
 * affected its balance ordered by transaction date. Its balance history is
 * therefore held within its TransationEntry objects and it only holds its latest
 * balance.
 *
 * @author	    Boubacar Diallo
 * @version	    1.0.0
 * @see		    TransactionEntry
 * @see		    Transaction
 * @since	    1.0.0
 */
public abstract class Account extends BaseModel {

    /** possible types of accounts */
    public static enum Type {
	ASSET, LIABILITY, EXPENSE, REVENUE, EQUITY
    }

    protected int number;			// account's business code

    protected String name;			// name of account

    protected String description;		// description about account

    protected double balance;			// current balance of account

    protected Type type;			// type of account

    /**
     * flag to indicate whether this account's balance can be modified through
     * transactions
     */
    protected boolean transactionsEnabled;

    /**
     * TransactionEntry objects that affected this account's balance; ordered by
     * Transaction date
     */
    protected List<TransactionEntry> entries;


    /**
     * Default no argument constructor. Initializes number to {@code -1}, name and
     * description to {@code ""}, balance to {@code 0.0}, entries to an empty list
     * and transactionsEnabled to {@code true}. This constructor effectively calls
     * the full agrument constructor with the above values.
     *
     * @see		    #Account(int, java.lang.String, java.lang.String,
     *					double, boolean)
     * @since		    1.0.0
     *
     */
    protected Account() {
	this(-1, "", "", 0.0, true);
    }

    /**
     * Convenience  constructor. Accepts values for the most commonly initialized
     * properties number, name, description, balance, transactionsEnabled and
     * defaults the other property entries to an empty list. This constructor
     * uses the full argument consstructor.
     *
     * @param number			the account number
     * @param name			the account name
     * @param description		the account description
     * @param balance			the account balance
     * @param transactionsEnabled	the transactions enabling flag
     * @see				#Account(int, java.lang.String, java.lang.String,
     *						    double, boolean, java.util.List)
     * @since				1.0.0
     */
    protected Account(int number, String name, String description, double balance,
			boolean transactionsEnabled) {
	this(number, name, description, balance, transactionsEnabled,
		new ArrayList<TransactionEntry>());
    }

    /**
     * Full constructor. Initializes an Account object to the given values. The
     * type of an Account must be set by its sub-classes. This constructor calls
     * a convenience setter to set the object's properties.
     *
     * @param number		    the account number
     * @param name		    the account name
     * @param description	    the account description
     * @param balance		    the account balance
     * @param allowTransactions	    the transactions enabling flag
     * @param entries		    the account TransactionEntry objects list
     * @see			    #set(int, java.lang.String, java.lang.String,
     *					double, jaccounting.models.Account.Type,
     *					boolean, java.util.List)
     * @since			    1.0.0
     */
    protected Account(int number, String name, String description,
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

    /**
     * Creates a an Account of the given type with default values.
     *
     * @param pType				the account type
     * @return					an Account of the given type with
     *						default values
     * @throws InvalidAccountTypeException	if the given type is not recognized
     * @see					AssetAccount
     * @see					EquityAccount
     * @see					ExpenseAccount
     * @see					LiabilityAccount
     * @see					RevenueAccount
     * @since					1.0.0
     */
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

    /**
     * Creates a an Account of the given type with the specified values.
     *
     * @param number			    the account number
     * @param name			    the account name
     * @param description		    the account description
     * @param balance			    the account balance
     * @param type			    the account type
     * @param entries			    the account TransactionEntry objects
     *					    list
     * @param allowTransactions		    the transactions enabling flag
     * @return				    an Account object with the specified
     *					    values
     * @throws InvalidAccountTypeException  if the given type is not recognized
     * @see				    AssetAccount
     * @see				    EquityAccount
     * @see				    ExpenseAccount
     * @see				    LiabilityAccount
     * @see				    RevenueAccount
     * @since				    1.0.0
     */
    public static Account createAccount(int number, String name, String description,
		double balance, Type type, List<TransactionEntry> entries,
		boolean allowTransactions) throws InvalidAccountTypeException {
>>>>>>> .r71
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
	rAccount.set(number, name, description, balance, type, allowTransactions,
			entries);

	return (Account) rAccount;
    }

    /**
     * Sets this Account's properties to the specified values. This method is a
     * sort of batch setter that can be used when initializing and updating an
     * Account. If {@code allowTransactions} is {@code false} and {@code entries}
     * is a non empty list, it resets {@code entries} to an empty list.
     *
     * @param number			    the account number
     * @param name			    the account name
     * @param description		    the account description
     * @param balance			    the account balance
     * @param type			    the account type
     * @param allowTransactions		    the transactions enabling flag
     * @param entries			    the account TransactionEntry objects 
     *					    list
     * @since				    1.0.0
     */
    protected void set(int number, String name, String description, double balance,
		Type type, boolean allowTransactions, List<TransactionEntry> entries) {
	if (entries != null && !allowTransactions && entries.size() > 0) {
	    Logger.getLogger(this.getClass().getName()).log(Level.WARNING, 
		    "Attempted to set non empty entries list for not transactionable" +
		    " account: " + name);
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

    /**
     * Sets this Account's most common properties to the specified values. This
     * method is a convenience  sort of batch setter.
     *
     * @param number		    the account number
     * @param name		    the account name
     * @param description	    the account description
     * @since			    1.0.0
     */
    protected void set(int number, String name, String description) {
	this.number = number;
	this.name = name;
	this.description = description;
    }

    /**
     * Gets a list of all Transaction objects that affected this Acount's balance.
     * This method effectively grabs the Transaction objects of its TransactionEntry
     * objects.
     *
     * @return		    the list of all Transaction objects that affected this
     *			    Account
     * @since		    1.0.0
     */
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

    /**
     * Updates this Account's properties and notifies its change observers. This
     * method first validates the new values to update to. It uses {@link #set(int,
     * java.lang.String, java.lang.String) } to update the properties.
     *
     * @param number		    the account number
     * @param name		    the account name
     * @param description	    the account description
     * @return			    a map of error codes indexed by property name
     *				    if the validation failed
     * @see			    #set(int, java.lang.String, java.lang.String)
     * @see			    BaseModel#setChangedAndNotifyObservers()
     * @since			    1.0.0
     */
    public Map<String, ErrorCode> update(int number, String name, String description) {
	Map<String, ErrorCode> rErrors = validate(number, name, description);

	if (rErrors.isEmpty()) {
	    set(number, name, description);
	    setChangedAndNotifyObservers();
	}

	return rErrors;
    }

    /**
     * Validates potential values of an Account's object for errors. account name
     * must be non empty and alpha numeric; account number must be a positive
     * integer.
     *
     * @param number		    an account number
     * @param name		    an account name
     * @param description	    an account description
     * @return			    a map of error codes indexed by property name
     * @since			    1.0.0
     */
    public static Map<String, ErrorCode> validate(int number, String name, String description) {
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

    /**
     * Adds a TransactionEntry to the list of TransactionEntry objects that
     * affected this Account's balance. This methods finds the proper position
     * where to insert the new entry since the entries have to be ordered by date;
     * it then adds the entry to the list, re-calculates this Account's balance
     * starting at the newly added entry and notifies the change observers.
     *
     * @param pEntry				    the TransactionEntry to add
     * @throws NotTransactionnableAccountException  if transactions are not allowed
     *						    for this Account
     * @see					    #getIndexOfFirstEntryLaterThan
     *						    (jaccounting.models.TransactionEntry)
     * @see					    #applyLaterEntriesToBalanceStartingAt(int)
     * @see					    BaseModel#setChangedAndNotifyObservers()
     * @since					    1.0.0
     */
    public void addEntry(TransactionEntry pEntry) throws NotTransactionnableAccountException {
	if (!transactionsEnabled) throw new NotTransactionnableAccountException();
	// get the index of the first entry whose date is bigger than this entry's
	int vIndex = getIndexOfFirstEntryLaterThan(pEntry);
	// insert at that index
	if (vIndex != -1) {
	    entries.add(vIndex, pEntry);
	} 
	// or add at the end
	else {
	    entries.add(pEntry);
	    vIndex = 0;
	}
	// re-calculate balance
	applyLaterEntriesToBalanceStartingAt(vIndex);
	setChangedAndNotifyObservers();
    }

    /**
     * Re-calculates this Account's balance by applying the effects of TransactionEntry
     * objects starting at a given index. This method iterates through the necessary
     * TransactionEntry objects and applies them individually to this Account's
     * balance.
     *
     * @param pIndex		    the index of TransactionEntry objects list to
     *				    start at
     * @see			    #applyEntryToBalance(jaccounting.models.TransactionEntry)
     * @since			    1.0.0
     */
    protected void applyLaterEntriesToBalanceStartingAt(int pIndex) {	
	ListIterator vIt;

	/**
	 * Bearing in mind that the history of this Account balance changes is
	 * held in its TransactionEntry objects, we generally reset the balance
	 * to the balance at one index before our starting index pIndex; when
	 * the starting index is 0, we simply reset the balance to 0.0. Then we
	 * cycly through forward and apply each TransactionEntry to our balance.
	 */

	if (pIndex > 0) {
	    vIt = entries.listIterator(pIndex-1);
	    balance = ((TransactionEntry) vIt.next()).getAccountBalance();
	}
	else {
	    vIt = entries.listIterator(pIndex);
	    balance = 0.0;
	}

	while (vIt.hasNext()) {
	    applyEntryToBalance((TransactionEntry) vIt.next());
	}
    }

    /**
     * Applies a TransactionEntry to this Account's balance. This method adds or
     * substracts the TransactionEntry's Transaction amount to/off this Account's
     * balance depending on the account type and whether the TransactionEntry is
     * a Debit or a Credit, then stores the balance into the TransactionEntry
     * object for history.
     *
     * @param pEntry		    the TransactionEntry to apply to the balance
     * @see			    #applyDebit(double)
     * @see			    #applyCredit(double)
     * @since			    1.0.0
     */
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

    /**
     * Applies a Debit amount to this Account's balance. This method increases
     * or decreases the balance by the Debit amount based on the Account type.
     *
     * @param pAmount		    the amount to apply as a Debit
     * @since			    1.0.0
     */
    protected abstract void applyDebit(double pAmount);

    /**
     * Applies a Credit amount to this Account's balance. This method increases
     * or decreases the balance by the Credit amount based on the Account type.
     *
     * @param pAmount		    the amount to apply as a Credit
     * @since			    1.0.0
     */
    protected abstract void applyCredit(double pAmount);

    /**
     * Removes a TransctionEntry from this Account's TransctionEntry objects list.
     * This method re-calculates this Account's balance after removing the entry
     * and notifies its change observers.
     *
     * @param pEntry				    the TransactionEntry to remove
     * @throws NotTransactionnableAccountException  if this Account does not allow
     *						    transactions
     * @see					    #applyLaterEntriesToBalanceStartingAt(int)
     * @see					    BaseModel#setChangedAndNotifyObservers()
     * @since					    1.0.0
     */
    public void removeEntry(TransactionEntry pEntry) throws NotTransactionnableAccountException {
	if (!transactionsEnabled) throw new NotTransactionnableAccountException();

	int vIndex = entries.indexOf(pEntry);

	if (entries.remove(pEntry)) {
	    if (vIndex > 0) vIndex--;

	    applyLaterEntriesToBalanceStartingAt(vIndex);
	    setChangedAndNotifyObservers();
	}
    }

    /**
     * Gets the index of the first TransactionEntry whose Transaction date is
     * after the given date.
     *
     * @param pDate		the date
     * @return			the index or -1
     * @since			1.0.0
     */
    protected int getIndexOfFirstEntryLaterThan(Date pDate) {
	int rIndex = -1;
	int vCurrIndex = -1;
	boolean vFound = false;
	TransactionEntry vEntry;
	ListIterator vIt = entries.listIterator();

	// loop through and compare dates
	while (!vFound && vIt.hasNext()) {
	    vCurrIndex++;
	    vEntry = (TransactionEntry)vIt.next();
	    if (vEntry.getTransaction().getDate().after(pDate)) {
		rIndex = vCurrIndex;
		vFound = true;
	    }
	}

	return rIndex;
    }

    /**
     * Gets the index of the first TransactionEntry whose Transaction date is
     * after the given TransactionEntry's Transaction date. This method effectively
     * calls {@link #getIndexOfFirstEntryLaterThan(java.util.Date) } with the
     * given TransactionEntry's Transaction date.
     *
     * @param pEntry		the TransactionEntry whose date to compare against
     * @return			the index or -1
     * @see			#getIndexOfFirstEntryLaterThan(java.util.Date)
     * @since			1.0.0
     */
    protected int getIndexOfFirstEntryLaterThan(TransactionEntry pEntry) {
	return getIndexOfFirstEntryLaterThan(pEntry.getTransaction().getDate());
    }

}
