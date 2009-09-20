/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jaccounting.models;

import jaccounting.exceptions.ErrorCode;
import jaccounting.exceptions.NotTransactionnableAccountException;
import java.util.Date;
import java.util.Map;
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
public class TransactionTest {

    private Transaction transaction;

    public TransactionTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
	TransactionEntry de = new TransactionEntry(null, null, TransactionEntry.Type.DEBIT, 0.0);
	TransactionEntry ce = new TransactionEntry(null, null, TransactionEntry.Type.CREDIT, 0.0);
	transaction = new TransactionMock(new Date(), "Sample RefNo", "Sample Memo", 0.0, de, ce);
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of update method, of class Transaction.
     */
    @Test
    public void testUpdate_Rejects_Negative_Amount() {
	System.out.println("update");
	Date date = new Date();
	String refNo = "New Sample RefNo";
	String memo = "New Sample Memo";
	double amount = -2000.60;
	Account creditAccount = new AssetAccount(-1, "Sample Asset", "", 0.0, true);
	Account debitAccount = new ExpenseAccount(-1, "Sample Expense", "", 0.0, true);
	
	Map result = transaction.update(date, refNo, memo, amount, debitAccount, creditAccount);

	assertTrue(result.containsKey("amount"));
	assertTrue(result.containsValue(ErrorCode.NEGATIVE_TRANSACTION_AMOUNT));
	assertEquals(1, result.size());
	assertFalse(debitAccount.equals(transaction.getDebitAccount()));
	assertFalse(creditAccount.equals(transaction.getCreditAccount()));
	assertFalse(transaction.getDate().equals(date));
	assertFalse(transaction.getRefNo().equals(refNo));
	assertFalse(transaction.getMemo().equals(memo));
	assertFalse(transaction.getAmount() == amount);
    }

    /**
     * Test of update method, of class Transaction.
     */
    @Test
    public void testUpdate_Rejects_Not_Transactionnale_Debit_Account() {
	System.out.println("update");
	Date date = new Date();
	String refNo = "New Sample RefNo";
	String memo = "New Sample Memo";
	double amount = 2000.60;
	Account creditAccount = new AssetAccount(-1, "Sample Asset", "", 0.0, true);
	Account debitAccount = new ExpenseAccount(-1, "Sample Expense", "", 0.0, false);

	Map result = transaction.update(date, refNo, memo, amount, debitAccount, creditAccount);

	assertTrue(result.containsKey("debitAccount"));
	assertTrue(result.containsValue(ErrorCode.NOT_TRANSACTIONNABLE_ACCOUNT));
	assertEquals(1, result.size());
    }

    /**
     * Test of update method, of class Transaction.
     */
    @Test
    public void testUpdate_Rejects_Not_Transactionnale_Credit_Account() {
	System.out.println("update");
	Date date = new Date();
	String refNo = "New Sample RefNo";
	String memo = "New Sample Memo";
	double amount = 2000.60;
	Account creditAccount = new AssetAccount(-1, "Sample Asset", "", 0.0, false);
	Account debitAccount = new ExpenseAccount(-1, "Sample Expense", "", 0.0, true);

	Map result = transaction.update(date, refNo, memo, amount, debitAccount, creditAccount);

	assertTrue(result.containsKey("creditAccount"));
	assertTrue(result.containsValue(ErrorCode.NOT_TRANSACTIONNABLE_ACCOUNT));
	assertEquals(1, result.size());
    }

    /**
     * Test of update method, of class Transaction.
     */
    @Test
    public void testUpdate_Makes_A_Correct_Update() {
	System.out.println("update");
	Date date = new Date();
	String refNo = "New Sample RefNo";
	String memo = "New Sample Memo";
	double amount = 2000.60;
	Account creditAccount = new AssetAccount(-1, "Sample Asset", "", 0.0, true);
	Account debitAccount = new ExpenseAccount(-1, "Sample Expense", "", 0.0, true);

	Map result = transaction.update(date, refNo, memo, amount, debitAccount, creditAccount);

	assertTrue(result.isEmpty());
	assertEquals(transaction.getDebitAccount(), debitAccount);
	assertEquals(transaction.getCreditAccount(), creditAccount);
	assertEquals(transaction.getDate(), date);
	assertEquals(transaction.getRefNo(), refNo);
	assertEquals(transaction.getMemo(), memo);
	assertEquals(transaction.getAmount(), amount, 0.0);
    }


    public class TransactionMock extends Transaction {

	public TransactionMock(Date date, String refNo, String memo, double amount, TransactionEntry debitEntry,
			TransactionEntry creditEntry) {
	    super(date, refNo, memo, amount, debitEntry, creditEntry);
	}

	@Override
	protected void removeEntriesFromAccounts() throws NotTransactionnableAccountException {
	}

	@Override
	protected void addEntriesToAccounts() throws NotTransactionnableAccountException {
	}

	@Override
	protected void setChangedAndNotifyObservers() {
	}
    }

}