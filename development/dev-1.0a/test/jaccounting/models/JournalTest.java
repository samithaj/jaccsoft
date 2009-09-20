/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jaccounting.models;

import jaccounting.exceptions.NotTransactionnableAccountException;
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
 *
 * @author bouba
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
	Transaction t1 = new TransactionMock(new Date(123456789), "Sample RefNo", "Sample Memo", 0.0, null, null);
	Transaction t2 = new TransactionMock(new Date(123456987), "Sample RefNo 2", "Sample Memo 2", 0.0, null, null);
	
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
	Transaction t1 = new TransactionMock(new Date(123456789), "Sample RefNo", "Sample Memo", 0.0, null, null);
	Transaction t2 = new TransactionMock(new Date(123456987), "Sample RefNo 2", "Sample Memo 2", 0.0, null, null);

	journal.addTransaction(t2);
	journal.addTransaction(t1);

	Transaction result = journal.getTransaction(pIndex);
	assertEquals(t1, result);
    }

    /**
     * Test of removeTransaction method, of class Journal.
     */
    @Test
    public void testRemoveTransaction() throws Exception {
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
    public void testRemoveTransactions() throws Exception {
	System.out.println("removeTransactions");
	List<Transaction> pTransactions = new ArrayList<Transaction>();
	Transaction t1 = new TransactionMock(new Date(123456789), "Sample RefNo", "Sample Memo", 0.0, null, null);
	Transaction t2 = new TransactionMock(new Date(123456987), "Sample RefNo 2", "Sample Memo 2", 0.0, null, null);
	pTransactions.add(t1);
	pTransactions.add(t2);

	journal.addTransaction(t2);
	journal.addTransaction(t1);

	journal.removeTransactions(pTransactions);

	assertEquals(0, journal.getTransactions().size());
    }


    public class JournalMock extends Journal {

	@Override
	protected void setChangedAndNotifyObservers() {
	}

    }

    public class TransactionMock extends Transaction {

	public TransactionMock(Date date, String refNo, String memo, double amount, TransactionEntry debitEntry,
			TransactionEntry creditEntry) {
	    super(date, refNo, memo, amount, debitEntry, creditEntry);
	}

	@Override
	public void removeEntriesFromAccounts() throws NotTransactionnableAccountException {
	}

    }
}