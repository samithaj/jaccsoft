/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jaccounting.models;

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
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of createTransaction method, of class Transaction.
     */
    @Test
    public void testCreateTransaction() {
	System.out.println("createTransaction");
	Transaction expResult = null;
	Transaction result = Transaction.createTransaction();
	assertEquals(expResult, result);
	// TODO review the generated test code and remove the default call to fail.
	fail("The test case is a prototype.");
    }

    /**
     * Test of getAmount method, of class Transaction.
     */
    @Test
    public void testGetAmount() {
	System.out.println("getAmount");
	Transaction instance = new Transaction();
	double expResult = 0.0;
	double result = instance.getAmount();
	assertEquals(expResult, result, 0.0);
	// TODO review the generated test code and remove the default call to fail.
	fail("The test case is a prototype.");
    }

    /**
     * Test of getCreditEntry method, of class Transaction.
     */
    @Test
    public void testGetCreditEntry() {
	System.out.println("getCreditEntry");
	Transaction instance = new Transaction();
	TransactionEntry expResult = null;
	TransactionEntry result = instance.getCreditEntry();
	assertEquals(expResult, result);
	// TODO review the generated test code and remove the default call to fail.
	fail("The test case is a prototype.");
    }

    /**
     * Test of getDate method, of class Transaction.
     */
    @Test
    public void testGetDate() {
	System.out.println("getDate");
	Transaction instance = new Transaction();
	Date expResult = null;
	Date result = instance.getDate();
	assertEquals(expResult, result);
	// TODO review the generated test code and remove the default call to fail.
	fail("The test case is a prototype.");
    }

    /**
     * Test of getDebitEntry method, of class Transaction.
     */
    @Test
    public void testGetDebitEntry() {
	System.out.println("getDebitEntry");
	Transaction instance = new Transaction();
	TransactionEntry expResult = null;
	TransactionEntry result = instance.getDebitEntry();
	assertEquals(expResult, result);
	// TODO review the generated test code and remove the default call to fail.
	fail("The test case is a prototype.");
    }

    /**
     * Test of getMemo method, of class Transaction.
     */
    @Test
    public void testGetMemo() {
	System.out.println("getMemo");
	Transaction instance = new Transaction();
	String expResult = "";
	String result = instance.getMemo();
	assertEquals(expResult, result);
	// TODO review the generated test code and remove the default call to fail.
	fail("The test case is a prototype.");
    }

    /**
     * Test of getRefNo method, of class Transaction.
     */
    @Test
    public void testGetRefNo() {
	System.out.println("getRefNo");
	Transaction instance = new Transaction();
	String expResult = "";
	String result = instance.getRefNo();
	assertEquals(expResult, result);
	// TODO review the generated test code and remove the default call to fail.
	fail("The test case is a prototype.");
    }

    /**
     * Test of getCreditAccount method, of class Transaction.
     */
    @Test
    public void testGetCreditAccount() {
	System.out.println("getCreditAccount");
	Transaction instance = new Transaction();
	Account expResult = null;
	Account result = instance.getCreditAccount();
	assertEquals(expResult, result);
	// TODO review the generated test code and remove the default call to fail.
	fail("The test case is a prototype.");
    }

    /**
     * Test of getDebitAccount method, of class Transaction.
     */
    @Test
    public void testGetDebitAccount() {
	System.out.println("getDebitAccount");
	Transaction instance = new Transaction();
	Account expResult = null;
	Account result = instance.getDebitAccount();
	assertEquals(expResult, result);
	// TODO review the generated test code and remove the default call to fail.
	fail("The test case is a prototype.");
    }

    /**
     * Test of initProperties method, of class Transaction.
     */
    @Test
    public void testInitProperties() {
	System.out.println("initProperties");
	Date date = null;
	String refNo = "";
	String memo = "";
	double amount = 0.0;
	TransactionEntry debitEntry = null;
	TransactionEntry creditEntry = null;
	Transaction instance = new Transaction();
	instance.initProperties(date, refNo, memo, amount, debitEntry, creditEntry);
	// TODO review the generated test code and remove the default call to fail.
	fail("The test case is a prototype.");
    }

    /**
     * Test of updateProperties method, of class Transaction.
     */
    @Test
    public void testUpdateProperties() {
	System.out.println("updateProperties");
	Date date = null;
	String refNo = "";
	String memo = "";
	double amount = 0.0;
	Account debitAccount = null;
	Account creditAccount = null;
	Transaction instance = new Transaction();
	Map expResult = null;
	Map result = instance.updateProperties(date, refNo, memo, amount, debitAccount, creditAccount);
	assertEquals(expResult, result);
	// TODO review the generated test code and remove the default call to fail.
	fail("The test case is a prototype.");
    }

    /**
     * Test of removeEntriesFromAccounts method, of class Transaction.
     */
    @Test
    public void testRemoveEntriesFromAccounts() {
	System.out.println("removeEntriesFromAccounts");
	Transaction instance = new Transaction();
	instance.removeEntriesFromAccounts();
	// TODO review the generated test code and remove the default call to fail.
	fail("The test case is a prototype.");
    }

    /**
     * Test of addEntriesToAccounts method, of class Transaction.
     */
    @Test
    public void testAddEntriesToAccounts() {
	System.out.println("addEntriesToAccounts");
	Transaction instance = new Transaction();
	instance.addEntriesToAccounts();
	// TODO review the generated test code and remove the default call to fail.
	fail("The test case is a prototype.");
    }

    /**
     * Test of validatePropertyValues method, of class Transaction.
     */
    @Test
    public void testValidatePropertyValues() {
	System.out.println("validatePropertyValues");
	double amount = 0.0;
	Account debitAccount = null;
	Account creditAccount = null;
	Transaction instance = new Transaction();
	Map expResult = null;
	Map result = instance.validatePropertyValues(amount, debitAccount, creditAccount);
	assertEquals(expResult, result);
	// TODO review the generated test code and remove the default call to fail.
	fail("The test case is a prototype.");
    }

}