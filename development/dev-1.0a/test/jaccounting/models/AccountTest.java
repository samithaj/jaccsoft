/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jaccounting.models;

import jaccounting.exceptions.ErrorCode;
import jaccounting.exceptions.GenericException;
import jaccounting.models.Account.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
public class AccountTest {

    private Account assetAccountWithNoEntries;

    private Account accountWithNoEntries;

    public AccountTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
	accountWithNoEntries = new AccountImpl();
	accountWithNoEntries.number = 100;
	accountWithNoEntries.name = "Sample Account Name";
	accountWithNoEntries.description = "Sample Account Description";
	accountWithNoEntries.transactionsEnabled = true;
	accountWithNoEntries.entries = new ArrayList<TransactionEntry>();

	assetAccountWithNoEntries = new AccountImpl();
	assetAccountWithNoEntries.number = 1000;
	assetAccountWithNoEntries.name = "Sample Account Name";
	assetAccountWithNoEntries.description = "Sample Account Description";
	assetAccountWithNoEntries.transactionsEnabled = true;
	assetAccountWithNoEntries.type = Type.ASSET;
	assetAccountWithNoEntries.entries = new ArrayList<TransactionEntry>();
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of createAccount method, of class Account.
     */
    @Test
    public void testCreateAccount_AccountType_Creates_An_Asset_Account() throws Exception {
	System.out.println("createAccount");
	Type pType = Account.Type.ASSET;
	Account result = Account.createAccount(pType);
	assertTrue(result instanceof AssetAccount);
    }

     /**
     * Test of createAccount method, of class Account.
     */
    @Test
    public void testCreateAccount_AccountType_Creates_A_Liability_Account() throws Exception {
	System.out.println("createAccount");
	Type pType = Account.Type.LIABILITY;
	Account result = Account.createAccount(pType);
	assertTrue(result instanceof LiabilityAccount);
    }

     /**
     * Test of createAccount method, of class Account.
     */
    @Test
    public void testCreateAccount_AccountType_Creates_An_Equity_Account() throws Exception {
	System.out.println("createAccount");
	Type pType = Account.Type.EQUITY;
	Account result = Account.createAccount(pType);
	assertTrue(result instanceof EquityAccount);
    }

     /**
     * Test of createAccount method, of class Account.
     */
    @Test
    public void testCreateAccount_AccountType_Creates_A_Revenue_Account() throws Exception {
	System.out.println("createAccount");
	Type pType = Account.Type.REVENUE;
	Account result = Account.createAccount(pType);
	assertTrue(result instanceof RevenueAccount);
    }

     /**
     * Test of createAccount method, of class Account.
     */
    @Test
    public void testCreateAccount_AccountType_Creates_An_Expense_Account() throws Exception {
	System.out.println("createAccount");
	Type pType = Account.Type.EXPENSE;
	Account result = Account.createAccount(pType);
	assertTrue(result instanceof ExpenseAccount);
    }

    /**
     * Test of createAccount method, of class Account.
     */
    @Test
    public void testCreateAccount_7args_Creates_A_Correct_Asset_Account() throws Exception {
	System.out.println("createAccount");
	int number = 10;
	String name = "Sample Account Name";
	String description = "Sample Account Description";
	double balance = 20.06;
	Type type = Account.Type.ASSET;
	List<TransactionEntry> entries = null;
	boolean allowTransactions = false;

	Account result = Account.createAccount(number, name, description, balance, type, entries, allowTransactions);

	assertTrue(result instanceof AssetAccount);
	assertEquals(10, result.number);
	assertEquals("Sample Account Name", result.name);
	assertEquals("Sample Account Description", result.description);
	assertEquals(20.06, result.balance, 0.0);
	assertEquals(false, result.transactionsEnabled);
	assertNull(result.entries);
    }

    /**
     * Test of updateProperties method, of class Account.
     */
    @Test
    public void testUpdateProperties_Rejects_Negative_Account_Number() {
	System.out.println("updateProperties");
	int number = -101;
	String name = "New Sample Account Name";
	String description = "New Sample Account Description";

	Map result = accountWithNoEntries.updateProperties(number, name, description);

	assertTrue(result.size() == 1);
	assertTrue(result.containsKey("number"));
	assertTrue(result.containsValue(ErrorCode.NEGATIVE_ACCOUNT_NUMBER));
	assertFalse(accountWithNoEntries.name.equals(name));
	assertFalse(accountWithNoEntries.description.equals(description));
    }

    /**
     * Test of updateProperties method, of class Account.
     */
    @Test
    public void testUpdateProperties_Rejects_Empty_Account_Name() {
	System.out.println("updateProperties");
	int number = 101;
	String name = "";
	String description = "New Sample Account Description";

	Map result = accountWithNoEntries.updateProperties(number, name, description);

	assertTrue(result.size() == 1);
	assertTrue(result.containsKey("name"));
	assertTrue(result.containsValue(ErrorCode.EMPTY_ACCOUNT_NAME));
	assertFalse(accountWithNoEntries.number == number);
	assertFalse(accountWithNoEntries.name.equals(name));
	assertFalse(accountWithNoEntries.description.equals(description));
    }

    /**
     * Test of updateProperties method, of class Account.
     */
    @Test
    public void testUpdateProperties_Rejects_Not_Alphanumeric_Name() {
	System.out.println("updateProperties");
	int number = 101;
	String name = "Sample.Account&Name";
	String description = "New Sample Account Description";

	Map result = accountWithNoEntries.updateProperties(number, name, description);

	assertTrue(result.size() == 1);
	assertTrue(result.containsKey("name"));
	assertTrue(result.containsValue(ErrorCode.NOT_ALPHANUMERIC_ACCOUNT_NAME));
	assertFalse(accountWithNoEntries.number == number);
	assertFalse(accountWithNoEntries.name.equals(name));
	assertFalse(accountWithNoEntries.description.equals(description));
    }

    /**
     * Test of updateProperties method, of class Account.
     */
    @Test
    public void testUpdateProperties_Accepts_Alphanumeric_Name_And_Positive_Number_And_Empty_Description() {
	System.out.println("updateProperties");
	int number = 1001;
	String name = "New Sample Account Name";
	String description = "";

	Map result = accountWithNoEntries.updateProperties(number, name, description);

	assertTrue(result.isEmpty());
	assertTrue(accountWithNoEntries.number == number);
	assertTrue(accountWithNoEntries.name.equals(name));
	assertTrue(accountWithNoEntries.description.equals(description));
    }

    /**
     * Test of addEntry method, of class Account.
     */
    @Test
    public void testAddEntry_Adds_Two_Entries_Consecutively() throws Exception {
	System.out.println("addEntry");
	TransactionEntry e1 = new TransactionEntry(null,
		new Transaction(new Date(), "Sample Ref No", "Sample Memo", 1000.50, null, null),
		TransactionEntry.Type.DEBIT, 0.0);
	TransactionEntry e2 = new TransactionEntry(null,
		new Transaction(new Date(), "Sample Ref No", "Sample Memo", 2000.50, null, null),
		TransactionEntry.Type.CREDIT, 0.0);

	assetAccountWithNoEntries.addEntry(e1);
	assetAccountWithNoEntries.addEntry(e2);

	assertTrue(assetAccountWithNoEntries.entries.size() == 2);
	assertEquals(-1000, assetAccountWithNoEntries.balance, 0.0);
	assertTrue(assetAccountWithNoEntries.entries.get(0).equals(e1));
	assertTrue(assetAccountWithNoEntries.entries.get(1).equals(e2));
	assertEquals(1000.50, e1.accountBalance, 0.0);
	assertEquals(-1000, e2.accountBalance, 0.0);
    }

    /**
     * Test of removeEntry method, of class Account.
     */
    @Test
    public void testRemoveEntry() throws GenericException {
	System.out.println("removeEntry");
	TransactionEntry e1 = new TransactionEntry(null,
		new Transaction(new Date(), "Sample Ref No", "Sample Memo", 1000.50, null, null),
		TransactionEntry.Type.DEBIT, 0.0);
	TransactionEntry e2 = new TransactionEntry(null,
		new Transaction(new Date(), "Sample Ref No", "Sample Memo", 2000.50, null, null),
		TransactionEntry.Type.CREDIT, 0.0);

	assetAccountWithNoEntries.addEntry(e1);
	assetAccountWithNoEntries.addEntry(e2);
	assetAccountWithNoEntries.removeEntry(e1);

	assertTrue(assetAccountWithNoEntries.entries.size() == 1);
	assertEquals(-2000.50, assetAccountWithNoEntries.balance, 0.0);
	assertTrue(assetAccountWithNoEntries.entries.get(0).equals(e2));
	assertEquals(-2000.50, e2.accountBalance, 0.0);
    }

    public class AccountImpl extends Account {

	public void applyDebit(double pAmount) {
	    balance += pAmount;
	}

	public void applyCredit(double pAmount) {
	    balance -= pAmount;
	}

	@Override
	protected void setChangedAndNotifyObservers() {
	}
    }

}