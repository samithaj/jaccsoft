/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jaccounting.models;

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
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getTransactions method, of class Journal.
     */
    @Test
    public void testGetTransactions() {
	System.out.println("getTransactions");
	Journal instance = new Journal();
	List expResult = null;
	List result = instance.getTransactions();
	assertEquals(expResult, result);
	// TODO review the generated test code and remove the default call to fail.
	fail("The test case is a prototype.");
    }

    /**
     * Test of addTransaction method, of class Journal.
     */
    @Test
    public void testAddTransaction() {
	System.out.println("addTransaction");
	Transaction pTransaction = null;
	Journal instance = new Journal();
	instance.addTransaction(pTransaction);
	// TODO review the generated test code and remove the default call to fail.
	fail("The test case is a prototype.");
    }

    /**
     * Test of getTransaction method, of class Journal.
     */
    @Test
    public void testGetTransaction() {
	System.out.println("getTransaction");
	int pIndex = 0;
	Journal instance = new Journal();
	Transaction expResult = null;
	Transaction result = instance.getTransaction(pIndex);
	assertEquals(expResult, result);
	// TODO review the generated test code and remove the default call to fail.
	fail("The test case is a prototype.");
    }

    /**
     * Test of removeTransaction method, of class Journal.
     */
    @Test
    public void testRemoveTransaction() {
	System.out.println("removeTransaction");
	int vRow = 0;
	Journal instance = new Journal();
	instance.removeTransaction(vRow);
	// TODO review the generated test code and remove the default call to fail.
	fail("The test case is a prototype.");
    }

    /**
     * Test of removeTransactions method, of class Journal.
     */
    @Test
    public void testRemoveTransactions() {
	System.out.println("removeTransactions");
	List<Transaction> pTransactions = null;
	Journal instance = new Journal();
	instance.removeTransactions(pTransactions);
	// TODO review the generated test code and remove the default call to fail.
	fail("The test case is a prototype.");
    }

}