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

    private GeneralLedger generalLedger;

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
	DefaultMutableTreeNode rt = new DefaultMutableTreeNode("Sample Root");

	AssetAccount ac = new AssetAccount(-1, "Sample Assets", "", 0.0, false);
	LiabilityAccount lc = new LiabilityAccount(-1, "Sample Liabilities", "", 0.0, false);
	RevenueAccount rc = new RevenueAccount(-1, "Sample Revenues", "", 0.0, false);
	ExpenseAccount ec = new ExpenseAccount(-1, "Sample Expenses", "", 0.0, false);
	EquityAccount cc = new EquityAccount(-1, "Sample Equity", "", 0.0, false);

	AccountTreeNode a = new AccountTreeNode(ac);
	a.add(new AccountTreeNode(new AssetAccount(-1, "Sample Asset", "", 0.0, true)));
	AccountTreeNode l = new AccountTreeNode(lc);
	l.add(new AccountTreeNode(new LiabilityAccount(-1, "Sample Liability", "", 0.0, true)));
	AccountTreeNode c = new AccountTreeNode(cc);
	c.add(new AccountTreeNode(new EquityAccount(-1, "Sample Sub Equity", "", 0.0, true)));
	AccountTreeNode r = new AccountTreeNode(rc);
	r.add(new AccountTreeNode(new RevenueAccount(-1, "Sample Revenue", "", 0.0, true)));
	AccountTreeNode e = new AccountTreeNode(ec);
	e.add(new AccountTreeNode(new ExpenseAccount(-1, "Sample Expense", "", 0.0, true)));

	rt.add(a);
	rt.add(l);
	rt.add(c);
	rt.add(r);
	rt.add(e);

	generalLedger = new GeneralLedgerMock(rt, a, l, r, e, c);
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getAccountNodeAtRow method, of class GeneralLedger.
     */
    @Test
    public void testGetAccountNodeAtRow_Gets_Node_At_Row_4() {
	System.out.println("getAccountNodeAtRow");
	int pRow = 4;
	AccountTreeNode expResult = (AccountTreeNode) generalLedger.getLiabilitiesNode().getChildAt(0);

	AccountTreeNode result = generalLedger.getAccountNodeAtRow(pRow);

	assertEquals(expResult, result);
    }

    /**
     * Test of getRowOfAccount method, of class GeneralLedger.
     */
    @Test
    public void testGetRowOfAccount() {
	System.out.println("getRowOfAccount");
	Account pAcct = (Account) ((AccountTreeNode) generalLedger.getEquityNode().getChildAt(0)).getUserObject();
	int expResult = 6;

	int result = generalLedger.getRowOfAccount(pAcct);

	assertEquals(expResult, result);
    }

    /**
     * Test of insertChildAccount method, of class GeneralLedger.
     */
    @Test
    public void testInsertChildAccount() {
	System.out.println("insertChildAccount");
	int pRow = 7;
	Account pAcct = new RevenueAccount(-1, "Sample Revenue 2", "", 0.0, true);
	
	generalLedger.insertChildAccount(pRow, pAcct);

	assertEquals(pAcct, generalLedger.getAccount(9));
    }

    /**
     * Test of removeAccount method, of class GeneralLedger.
     */
    @Test(expected=ArrayIndexOutOfBoundsException.class)
    public void testRemoveAccount() throws Exception {
	System.out.println("removeAccount");
	int pRow = 10;
	
	generalLedger.removeAccount(pRow);

	assertNull(generalLedger.getExpensesNode().getChildAt(0));
    }

    /**
     * Test of getTransactionnableAccountFullNames method, of class GeneralLedger.
     */
    @Test
    public void testGetTransactionnableAccountFullNames() {
	System.out.println("getTransactionnableAccountFullNames");
	String[] expResult = { "Sample Assets.Sample Asset", "Sample Liabilities.Sample Liability",
	    "Sample Equity.Sample Sub Equity", "Sample Revenues.Sample Revenue",
	    "Sample Expenses.Sample Expense"};

	String[] result = generalLedger.getTransactionnableAccountFullNames();
	
	assertEquals(expResult, result);
    }

    /**
     * Test of getAccountFullNames method, of class GeneralLedger.
     */
    @Test
    public void testGetAccountFullNames_Gets_All_Account_Fullnames() {
	System.out.println("getAccountFullNames");
	boolean pExcludeNonTransactionnable = false;
	String[] expResult = { "Sample Assets", "Sample Assets.Sample Asset", "Sample Liabilities",
	    "Sample Liabilities.Sample Liability", "Sample Equity", "Sample Equity.Sample Sub Equity", 
	    "Sample Revenues", "Sample Revenues.Sample Revenue", "Sample Expenses",
	    "Sample Expenses.Sample Expense"};

	String[] result = generalLedger.getAccountFullNames(pExcludeNonTransactionnable);

	assertEquals(expResult, result);
    }

    /**
     * Test of getAccountFullName method, of class GeneralLedger.
     */
    @Test
    public void testGetAccountFullName() {
	System.out.println("getAccountFullName");
	Account pAcct = (Account) ((AccountTreeNode) generalLedger.getAssetsNode().getChildAt(0)).getUserObject();
	String expResult = "Sample Assets.Sample Asset";

	String result = generalLedger.getAccountFullName(pAcct);

	assertEquals(expResult, result);
    }

    /**
     * Test of getAccount method, of class GeneralLedger.
     */
    @Test
    public void testGetAccount_String() {
	System.out.println("getAccount");
	String pFullName = "Sample Revenues.Sample Revenue";
	Account expResult = (Account) ((AccountTreeNode) generalLedger.getRevenuesNode().getChildAt(0)).getUserObject();

	Account result = generalLedger.getAccount(pFullName);

	assertEquals(expResult, result);
    }


    public class GeneralLedgerMock extends GeneralLedger {

	public GeneralLedgerMock(DefaultMutableTreeNode root, AccountTreeNode assetsNode,
		AccountTreeNode liabilitiesNode, AccountTreeNode revenuesNode,
		AccountTreeNode expensesNode, AccountTreeNode equityNode) {
	    super(root, assetsNode, liabilitiesNode, revenuesNode, expensesNode, equityNode);
	}

	@Override
	protected void setChangedAndNotifyObservers() {
	}
    }
}