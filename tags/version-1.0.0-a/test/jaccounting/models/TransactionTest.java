/*
 * TransactionTest.java		    1.0.0	    09/2009
 * This file contains test cases for the Transaction class of the JAccounting application.
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
import java.util.Date;
import java.util.Map;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * TransactionTest is the test class for the Transaction class.
 *
 * @author	    Boubacar Diallo
 * @version	    1.0.0
 * @see		    Transaction
 * @since	    1.0.0
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
	TransactionEntry de = new TransactionEntry(null, null,
		    TransactionEntry.Type.DEBIT, 0.0);
	TransactionEntry ce = new TransactionEntry(null, null,
		    TransactionEntry.Type.CREDIT, 0.0);
	transaction = new TransactionMock(new Date(), "Sample RefNo",
		    "Sample Memo", 0.0, de, ce);
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
	Account creditAccount = new AssetAccount(-1, "Sample Asset", "", 0.0,
						    true);
	Account debitAccount = new ExpenseAccount(-1, "Sample Expense", "", 0.0,
						    true);
	
	Map result = transaction.update(date, refNo, memo, amount, debitAccount,
					creditAccount);

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
	Account creditAccount = new AssetAccount(-1, "Sample Asset", "", 0.0,
						    true);
	Account debitAccount = new ExpenseAccount(-1, "Sample Expense", "", 0.0,
						    false);

	Map result = transaction.update(date, refNo, memo, amount, debitAccount,
					creditAccount);

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
	Account creditAccount = new AssetAccount(-1, "Sample Asset", "", 0.0,
						    false);
	Account debitAccount = new ExpenseAccount(-1, "Sample Expense", "", 0.0,
						    true);

	Map result = transaction.update(date, refNo, memo, amount, debitAccount,
					creditAccount);

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
	Account creditAccount = new AssetAccount(-1, "Sample Asset", "", 0.0,
						    true);
	Account debitAccount = new ExpenseAccount(-1, "Sample Expense", "", 0.0,
						    true);

	Map result = transaction.update(date, refNo, memo, amount, debitAccount,
					creditAccount);

	assertTrue(result.isEmpty());
	assertEquals(transaction.getDebitAccount(), debitAccount);
	assertEquals(transaction.getCreditAccount(), creditAccount);
	assertEquals(transaction.getDate(), date);
	assertEquals(transaction.getRefNo(), refNo);
	assertEquals(transaction.getMemo(), memo);
	assertEquals(transaction.getAmount(), amount, 0.0);
    }


    private class TransactionMock extends Transaction {

	public TransactionMock(Date date, String refNo, String memo, double amount, 
		    TransactionEntry debitEntry, TransactionEntry creditEntry) {
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