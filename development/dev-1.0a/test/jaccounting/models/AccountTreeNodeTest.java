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
public class AccountTreeNodeTest {

    public AccountTreeNodeTest() {
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
     * Test of canBeRemoved method, of class AccountTreeNode.
     */
    @Test
    public void testCanBeRemoved() {
	System.out.println("canBeRemoved");
	AccountTreeNode instance = null;
	boolean expResult = false;
	boolean result = instance.canBeRemoved();
	assertEquals(expResult, result);
	// TODO review the generated test code and remove the default call to fail.
	fail("The test case is a prototype.");
    }

    /**
     * Test of canHaveChildren method, of class AccountTreeNode.
     */
    @Test
    public void testCanHaveChildren() {
	System.out.println("canHaveChildren");
	AccountTreeNode instance = null;
	boolean expResult = false;
	boolean result = instance.canHaveChildren();
	assertEquals(expResult, result);
	// TODO review the generated test code and remove the default call to fail.
	fail("The test case is a prototype.");
    }

    /**
     * Test of isAccountGroup method, of class AccountTreeNode.
     */
    @Test
    public void testIsAccountGroup() {
	System.out.println("isAccountGroup");
	AccountTreeNode instance = null;
	boolean expResult = false;
	boolean result = instance.isAccountGroup();
	assertEquals(expResult, result);
	// TODO review the generated test code and remove the default call to fail.
	fail("The test case is a prototype.");
    }

    /**
     * Test of isTopLevel method, of class AccountTreeNode.
     */
    @Test
    public void testIsTopLevel() {
	System.out.println("isTopLevel");
	AccountTreeNode instance = null;
	boolean expResult = false;
	boolean result = instance.isTopLevel();
	assertEquals(expResult, result);
	// TODO review the generated test code and remove the default call to fail.
	fail("The test case is a prototype.");
    }

    /**
     * Test of buildFullNames method, of class AccountTreeNode.
     */
    @Test
    public void testBuildFullNames() {
	System.out.println("buildFullNames");
	List<String> pList = null;
	String pPrefix = "";
	boolean pExcludeNonTransactionnable = false;
	AccountTreeNode instance = null;
	instance.buildFullNames(pList, pPrefix, pExcludeNonTransactionnable);
	// TODO review the generated test code and remove the default call to fail.
	fail("The test case is a prototype.");
    }

    /**
     * Test of getFullName method, of class AccountTreeNode.
     */
    @Test
    public void testGetFullName() {
	System.out.println("getFullName");
	AccountTreeNode instance = null;
	String expResult = "";
	String result = instance.getFullName();
	assertEquals(expResult, result);
	// TODO review the generated test code and remove the default call to fail.
	fail("The test case is a prototype.");
    }

    /**
     * Test of getAccount method, of class AccountTreeNode.
     */
    @Test
    public void testGetAccount() {
	System.out.println("getAccount");
	String pFullName = "";
	AccountTreeNode instance = null;
	Account expResult = null;
	Account result = instance.getAccount(pFullName);
	assertEquals(expResult, result);
	// TODO review the generated test code and remove the default call to fail.
	fail("The test case is a prototype.");
    }

    /**
     * Test of getChildAccountWithName method, of class AccountTreeNode.
     */
    @Test
    public void testGetChildAccountWithName() {
	System.out.println("getChildAccountWithName");
	String pName = "";
	AccountTreeNode instance = null;
	Account expResult = null;
	Account result = instance.getChildAccountWithName(pName);
	assertEquals(expResult, result);
	// TODO review the generated test code and remove the default call to fail.
	fail("The test case is a prototype.");
    }

    /**
     * Test of isAccountNameUniqueAmongChildren method, of class AccountTreeNode.
     */
    @Test
    public void testIsAccountNameUniqueAmongChildren() {
	System.out.println("isAccountNameUniqueAmongChildren");
	String pName = "";
	AccountTreeNode instance = null;
	boolean expResult = false;
	boolean result = instance.isAccountNameUniqueAmongChildren(pName);
	assertEquals(expResult, result);
	// TODO review the generated test code and remove the default call to fail.
	fail("The test case is a prototype.");
    }

}