/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jaccounting.models;

import javax.swing.tree.DefaultMutableTreeNode;
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
public class GeneralLedgerTest {

    public GeneralLedgerTest() {
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
     * Test of getRoot method, of class GeneralLedger.
     */
    @Test
    public void testGetRoot() {
	System.out.println("getRoot");
	GeneralLedger instance = new GeneralLedger();
	DefaultMutableTreeNode expResult = null;
	DefaultMutableTreeNode result = instance.getRoot();
	assertEquals(expResult, result);
	// TODO review the generated test code and remove the default call to fail.
	fail("The test case is a prototype.");
    }

    /**
     * Test of getAssetsNode method, of class GeneralLedger.
     */
    @Test
    public void testGetAssetsNode() {
	System.out.println("getAssetsNode");
	GeneralLedger instance = new GeneralLedger();
	AccountTreeNode expResult = null;
	AccountTreeNode result = instance.getAssetsNode();
	assertEquals(expResult, result);
	// TODO review the generated test code and remove the default call to fail.
	fail("The test case is a prototype.");
    }

    /**
     * Test of getEquityNode method, of class GeneralLedger.
     */
    @Test
    public void testGetEquityNode() {
	System.out.println("getEquityNode");
	GeneralLedger instance = new GeneralLedger();
	AccountTreeNode expResult = null;
	AccountTreeNode result = instance.getEquityNode();
	assertEquals(expResult, result);
	// TODO review the generated test code and remove the default call to fail.
	fail("The test case is a prototype.");
    }

    /**
     * Test of getExpensesNode method, of class GeneralLedger.
     */
    @Test
    public void testGetExpensesNode() {
	System.out.println("getExpensesNode");
	GeneralLedger instance = new GeneralLedger();
	AccountTreeNode expResult = null;
	AccountTreeNode result = instance.getExpensesNode();
	assertEquals(expResult, result);
	// TODO review the generated test code and remove the default call to fail.
	fail("The test case is a prototype.");
    }

    /**
     * Test of getLiabilitiesNode method, of class GeneralLedger.
     */
    @Test
    public void testGetLiabilitiesNode() {
	System.out.println("getLiabilitiesNode");
	GeneralLedger instance = new GeneralLedger();
	AccountTreeNode expResult = null;
	AccountTreeNode result = instance.getLiabilitiesNode();
	assertEquals(expResult, result);
	// TODO review the generated test code and remove the default call to fail.
	fail("The test case is a prototype.");
    }

    /**
     * Test of getRevenuesNode method, of class GeneralLedger.
     */
    @Test
    public void testGetRevenuesNode() {
	System.out.println("getRevenuesNode");
	GeneralLedger instance = new GeneralLedger();
	AccountTreeNode expResult = null;
	AccountTreeNode result = instance.getRevenuesNode();
	assertEquals(expResult, result);
	// TODO review the generated test code and remove the default call to fail.
	fail("The test case is a prototype.");
    }

    /**
     * Test of getAccount method, of class GeneralLedger.
     */
    @Test
    public void testGetAccount_int() {
	System.out.println("getAccount");
	int pRow = 0;
	GeneralLedger instance = new GeneralLedger();
	Account expResult = null;
	Account result = instance.getAccount(pRow);
	assertEquals(expResult, result);
	// TODO review the generated test code and remove the default call to fail.
	fail("The test case is a prototype.");
    }

    /**
     * Test of getAccountNodeAtRow method, of class GeneralLedger.
     */
    @Test
    public void testGetAccountNodeAtRow() {
	System.out.println("getAccountNodeAtRow");
	int pRow = 0;
	GeneralLedger instance = new GeneralLedger();
	AccountTreeNode expResult = null;
	AccountTreeNode result = instance.getAccountNodeAtRow(pRow);
	assertEquals(expResult, result);
	// TODO review the generated test code and remove the default call to fail.
	fail("The test case is a prototype.");
    }

    /**
     * Test of getRowOfAccount method, of class GeneralLedger.
     */
    @Test
    public void testGetRowOfAccount() {
	System.out.println("getRowOfAccount");
	Account pAcct = null;
	GeneralLedger instance = new GeneralLedger();
	int expResult = 0;
	int result = instance.getRowOfAccount(pAcct);
	assertEquals(expResult, result);
	// TODO review the generated test code and remove the default call to fail.
	fail("The test case is a prototype.");
    }

    /**
     * Test of insertChildAccount method, of class GeneralLedger.
     */
    @Test
    public void testInsertChildAccount() {
	System.out.println("insertChildAccount");
	int pRow = 0;
	Account pAcct = null;
	GeneralLedger instance = new GeneralLedger();
	instance.insertChildAccount(pRow, pAcct);
	// TODO review the generated test code and remove the default call to fail.
	fail("The test case is a prototype.");
    }

    /**
     * Test of removeAccount method, of class GeneralLedger.
     */
    @Test
    public void testRemoveAccount() {
	System.out.println("removeAccount");
	int pRow = 0;
	GeneralLedger instance = new GeneralLedger();
	instance.removeAccount(pRow);
	// TODO review the generated test code and remove the default call to fail.
	fail("The test case is a prototype.");
    }

    /**
     * Test of canAccountBeRemoved method, of class GeneralLedger.
     */
    @Test
    public void testCanAccountBeRemoved() {
	System.out.println("canAccountBeRemoved");
	int pRow = 0;
	GeneralLedger instance = new GeneralLedger();
	boolean expResult = false;
	boolean result = instance.canAccountBeRemoved(pRow);
	assertEquals(expResult, result);
	// TODO review the generated test code and remove the default call to fail.
	fail("The test case is a prototype.");
    }

    /**
     * Test of canAccountHaveChildren method, of class GeneralLedger.
     */
    @Test
    public void testCanAccountHaveChildren() {
	System.out.println("canAccountHaveChildren");
	int pRow = 0;
	GeneralLedger instance = new GeneralLedger();
	boolean expResult = false;
	boolean result = instance.canAccountHaveChildren(pRow);
	assertEquals(expResult, result);
	// TODO review the generated test code and remove the default call to fail.
	fail("The test case is a prototype.");
    }

    /**
     * Test of isAccountTopLevel method, of class GeneralLedger.
     */
    @Test
    public void testIsAccountTopLevel() {
	System.out.println("isAccountTopLevel");
	int pRow = 0;
	GeneralLedger instance = new GeneralLedger();
	boolean expResult = false;
	boolean result = instance.isAccountTopLevel(pRow);
	assertEquals(expResult, result);
	// TODO review the generated test code and remove the default call to fail.
	fail("The test case is a prototype.");
    }

    /**
     * Test of getTransactionnableAccountFullNames method, of class GeneralLedger.
     */
    @Test
    public void testGetTransactionnableAccountFullNames() {
	System.out.println("getTransactionnableAccountFullNames");
	GeneralLedger instance = new GeneralLedger();
	String[] expResult = null;
	String[] result = instance.getTransactionnableAccountFullNames();
	assertEquals(expResult, result);
	// TODO review the generated test code and remove the default call to fail.
	fail("The test case is a prototype.");
    }

    /**
     * Test of getAccountFullNames method, of class GeneralLedger.
     */
    @Test
    public void testGetAccountFullNames() {
	System.out.println("getAccountFullNames");
	boolean pExcludeNonTransactionnable = false;
	GeneralLedger instance = new GeneralLedger();
	String[] expResult = null;
	String[] result = instance.getAccountFullNames(pExcludeNonTransactionnable);
	assertEquals(expResult, result);
	// TODO review the generated test code and remove the default call to fail.
	fail("The test case is a prototype.");
    }

    /**
     * Test of getAccountFullName method, of class GeneralLedger.
     */
    @Test
    public void testGetAccountFullName() {
	System.out.println("getAccountFullName");
	Account pAcct = null;
	GeneralLedger instance = new GeneralLedger();
	String expResult = "";
	String result = instance.getAccountFullName(pAcct);
	assertEquals(expResult, result);
	// TODO review the generated test code and remove the default call to fail.
	fail("The test case is a prototype.");
    }

    /**
     * Test of getAccount method, of class GeneralLedger.
     */
    @Test
    public void testGetAccount_String() {
	System.out.println("getAccount");
	String pFullName = "";
	GeneralLedger instance = new GeneralLedger();
	Account expResult = null;
	Account result = instance.getAccount(pFullName);
	assertEquals(expResult, result);
	// TODO review the generated test code and remove the default call to fail.
	fail("The test case is a prototype.");
    }

    /**
     * Test of addNewDefaultAccounts method, of class GeneralLedger.
     */
    @Test
    public void testAddNewDefaultAccounts() {
	System.out.println("addNewDefaultAccounts");
	GeneralLedger instance = new GeneralLedger();
	instance.addNewDefaultAccounts();
	// TODO review the generated test code and remove the default call to fail.
	fail("The test case is a prototype.");
    }

}