/*
 * JournalTest.java		    1.0.0	    09/2009
 * This file contains test cases for the Journal class of the JAccounting application.
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
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * JournalTest is the test class for the Journal class.
 *
 * @author	    Boubacar Diallo
 * @version	    1.0.0
 * @see		    Journal
 * @since	    1.0.0
 */
public class JournalTest {

    private Journal journal;


    public JournalTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
	journal = new JournalMock();
    }

    @After
    public void tearDown() {
    }

    
    /**
     * Test of addTransaction method, of class Journal.
     */
    @Test
    public void testAddTransaction_Correctly_Inserts_Two_Transactions() {
	System.out.println("addTransaction");
	Transaction t1 = new TransactionMock(new Date(123456789), "Sample RefNo",
		"Sample Memo", 0.0, null, null);
	Transaction t2 = new TransactionMock(new Date(123456987), "Sample RefNo 2",
		"Sample Memo 2", 0.0, null, null);
	
	journal.addTransaction(t2);
	journal.addTransaction(t1);

	assertEquals(2, journal.getTransactions().size());
	assertEquals(t1, journal.getTransactions().get(0));
	assertEquals(t2, journal.getTransactions().get(1));
    }

    /**
     * Test of getTransaction method, of class Journal.
     */
    @Test
    public void testGetTransaction() {
	System.out.println("getTransaction");
	int pIndex = 0;
	Transaction t1 = new TransactionMock(new Date(123456789), "Sample RefNo",
		"Sample Memo", 0.0, null, null);
	Transaction t2 = new TransactionMock(new Date(123456987), "Sample RefNo 2",
		"Sample Memo 2", 0.0, null, null);
	journal.addTransaction(t2);
	journal.addTransaction(t1);

	Transaction result = journal.getTransaction(pIndex);

	assertEquals(t1, result);
    }

    /**
     * Test of removeTransaction method, of class Journal.
     */
    @Test
    public void testRemoveTransaction() {
	System.out.println("removeTransaction");
	int vRow = 0;
	Transaction t1 = new TransactionMock(new Date(123456789), "Sample RefNo", "Sample Memo", 0.0, null, null);
	Transaction t2 = new TransactionMock(new Date(123456987), "Sample RefNo 2", "Sample Memo 2", 0.0, null, null);
	journal.addTransaction(t2);
	journal.addTransaction(t1);

	journal.removeTransaction(vRow);

	assertEquals(1, journal.getTransactions().size());
	assertEquals(t2, journal.getTransactions().get(0));
    }

    /**
     * Test of removeTransactions method, of class Journal.
     */
    @Test
    public void testRemoveTransactions() {
	System.out.println("removeTransactions");
	List<Transaction> pTransactions = new ArrayList<Transaction>();
	Transaction t1 = new TransactionMock(new Date(123456789), "Sample RefNo",
		"Sample Memo", 0.0, null, null);
	Transaction t2 = new TransactionMock(new Date(123456987), "Sample RefNo 2",
		"Sample Memo 2", 0.0, null, null);
	pTransactions.add(t1);
	pTransactions.add(t2);
	journal.addTransaction(t2);
	journal.addTransaction(t1);

	journal.removeTransactions(pTransactions);

	assertEquals(0, journal.getTransactions().size());
    }


    private class JournalMock extends Journal {

	@Override
	protected void setChangedAndNotifyObservers() {
	}

    }
    

    private class TransactionMock extends Transaction {

	public TransactionMock(Date date, String refNo, String memo, double amount, 
		    TransactionEntry debitEntry, TransactionEntry creditEntry) {
	    super(date, refNo, memo, amount, debitEntry, creditEntry);
	}

	@Override
	public void removeEntriesFromAccounts() throws NotTransactionnableAccountException {
	}

    }
}