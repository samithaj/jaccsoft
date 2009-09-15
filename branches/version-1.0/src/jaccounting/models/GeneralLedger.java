/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jaccounting.models;

import jaccounting.JAccounting;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import javax.swing.tree.DefaultMutableTreeNode;
import org.jdesktop.application.ResourceMap;

/**
 *
 * @author bouba
 */
public class GeneralLedger extends BaseModel {

    protected DefaultMutableTreeNode root;
    protected AccountTreeNode assetsNode;
    protected AccountTreeNode liabilitiesNode;
    protected AccountTreeNode revenuesNode;
    protected AccountTreeNode expensesNode;
    protected AccountTreeNode equityNode;

    public GeneralLedger() {
	ResourceMap vRmap = JAccounting.getApplication().getContext().getResourceMap(this.getClass());

	AssetAccount vAssets = new AssetAccount(-1, vRmap.getString("accountNames.assets"),
		vRmap.getString("accountDescriptions.assets"),
		0.0, false);
	LiabilityAccount vLiabilities = new LiabilityAccount(-1, vRmap.getString("accountNames.liabilities"),
		vRmap.getString("accountDescriptions.liabilities"),
		0.0, false);
	RevenueAccount vRevenues = new RevenueAccount(-1, vRmap.getString("accountNames.revenues"),
		vRmap.getString("accountDescriptions.revenues"),
		0.0, false);
	ExpenseAccount vExpenses = new ExpenseAccount(-1, vRmap.getString("accountNames.expenses"),
		vRmap.getString("accountDescriptions.expenses"),
		0.0, false);
	EquityAccount vEquity = new EquityAccount(-1, vRmap.getString("accountNames.equity"),
		vRmap.getString("accountDescriptions.equity"),
		0.0, false);

	assetsNode = new AccountTreeNode(vAssets);
	liabilitiesNode = new AccountTreeNode(vLiabilities);
	revenuesNode = new AccountTreeNode(vRevenues);
	expensesNode = new AccountTreeNode(vExpenses);
	equityNode = new AccountTreeNode(vEquity);

	root = new DefaultMutableTreeNode(vRmap.getString("accountNames.root"));
	root.add(assetsNode);
	root.add(liabilitiesNode);
	root.add(equityNode);
	root.add(revenuesNode);
	root.add(expensesNode);
    }

    public GeneralLedger(DefaultMutableTreeNode root, AccountTreeNode assetsNode, 
	    AccountTreeNode liabilitiesNode, AccountTreeNode revenuesNode,
	    AccountTreeNode expensesNode, AccountTreeNode equityNode) {
	this.root = root;
	this.assetsNode = assetsNode;
	this.liabilitiesNode = liabilitiesNode;
	this.revenuesNode = revenuesNode;
	this.expensesNode = expensesNode;
	this.equityNode = equityNode;
	this.root.add(this.assetsNode);
	this.root.add(this.liabilitiesNode);
	this.root.add(this.equityNode);
	this.root.add(this.revenuesNode);
	this.root.add(this.expensesNode);
    }

    public DefaultMutableTreeNode getRoot() {
	return root;
    }

    public AccountTreeNode getAssetsNode() {
	return assetsNode;
    }

    public AccountTreeNode getEquityNode() {
	return equityNode;
    }

    public AccountTreeNode getExpensesNode() {
	return expensesNode;
    }

    public AccountTreeNode getLiabilitiesNode() {
	return liabilitiesNode;
    }

    public AccountTreeNode getRevenuesNode() {
	return revenuesNode;
    }

    public Account getAccount(int pRow) {
	return (Account) getAccountNodeAtRow(pRow).getUserObject();
    }

    public AccountTreeNode getAccountNodeAtRow(int pRow) {
	Enumeration vEnum = root.preorderEnumeration();

	for (int vI = 0; vI < pRow; vI++) {
	    if (vEnum.hasMoreElements()) {
		vEnum.nextElement();
	    } else {
		return null;
	    }
	}

	return (AccountTreeNode) vEnum.nextElement();
    }

    public int getRowOfAccount(Account pAcct) {
	Enumeration vEnum = root.preorderEnumeration();
	int rRow = -1;

	// skip root
	vEnum.nextElement();
	int vCurrRow = 0;

	while (vEnum.hasMoreElements()) {
	    vCurrRow++;
	    if (((Account)((AccountTreeNode)vEnum.nextElement()).getUserObject()).equals(pAcct)) {
		rRow = vCurrRow;
		break;
	    }
	}

	return rRow;
    }

    public void insertChildAccount(int pRow, Account pAcct) {
	AccountTreeNode vAcctNode = getAccountNodeAtRow(pRow);
	if (vAcctNode != null && vAcctNode.canHaveChildren() && vAcctNode.isAccountNameUniqueAmongChildren(pAcct.getName())) {
	    // add new node
	    vAcctNode.add(new AccountTreeNode(pAcct));
	    setChangedAndNotifyObservers();
	}
    }

    public void removeAccount(int pRow) {
	AccountTreeNode vAcctNode = getAccountNodeAtRow(pRow);
	if (vAcctNode != null && vAcctNode.canBeRemoved()) {
	    vAcctNode.removeFromParent();
	    List<Transaction> vTransactions = ((Account)vAcctNode.getUserObject()).getAccountTransactions();
	    if (vTransactions != null) {
		JAccounting.getApplication().getModelsMngr().getData().getJournal().removeTransactions(vTransactions);
	    }
	    setChangedAndNotifyObservers();
	}
    }

    public boolean canAccountBeRemoved(int pRow) {
	AccountTreeNode vAcctNode = getAccountNodeAtRow(pRow);
	if (vAcctNode != null) return vAcctNode.canBeRemoved();
	return false;
    }

    public boolean canAccountHaveChildren(int pRow) {
	AccountTreeNode vAcctNode = getAccountNodeAtRow(pRow);
	if (vAcctNode != null) return vAcctNode.canHaveChildren();
	return false;
    }

    public boolean isAccountTopLevel(int pRow) {
	AccountTreeNode vAcctNode = getAccountNodeAtRow(pRow);
	if (vAcctNode != null) return vAcctNode.isTopLevel();
	return false;
    }

    public String[] getTransactionnableAccountFullNames() {
	return getAccountFullNames(true);
    }

    String[] getAccountFullNames(boolean pExcludeNonTransactionnable) {
	List<String> rAcctsList = new ArrayList();
	String[] rAccts = { };

	assetsNode.buildFullNames(rAcctsList, "", pExcludeNonTransactionnable);
	liabilitiesNode.buildFullNames(rAcctsList, "", pExcludeNonTransactionnable);
	equityNode.buildFullNames(rAcctsList, "", pExcludeNonTransactionnable);
	revenuesNode.buildFullNames(rAcctsList, "", pExcludeNonTransactionnable);
	expensesNode.buildFullNames(rAcctsList, "", pExcludeNonTransactionnable);

	rAccts = rAcctsList.toArray(rAccts);

	return rAccts;
    }

    public String getAccountFullName(Account pAcct) {
	String rName = "";
	int vRow = getRowOfAccount(pAcct);

	if (vRow != -1) {
	    rName = (getAccountNodeAtRow(vRow)).getFullName();
	}

	return rName;
    }

    public Account getAccount(String pFullName) {
	Account rAcct;

	rAcct = assetsNode.getAccount(pFullName);
	if (rAcct != null) {
	    return rAcct;
	}

	rAcct = liabilitiesNode.getAccount(pFullName);
	if (rAcct != null) {
	    return rAcct;
	}

	rAcct = equityNode.getAccount(pFullName);
	if (rAcct != null) {
	    return rAcct;
	}

	rAcct = revenuesNode.getAccount(pFullName);
	if (rAcct != null) {
	    return rAcct;
	}

	rAcct = expensesNode.getAccount(pFullName);
	if (rAcct != null) {
	    return rAcct;
	}

	return rAcct;
    }

    /*public boolean canAccountHaveTransactions(int pRow) {
	Account vAcct = getAccount(pRow);
	if (vAcct != null) return vAcct.isTransactionsEnabled();
	return false;
    }*/
    
    public void addNewDefaultAccounts() {
	ResourceMap vRmap = JAccounting.getApplication().getContext().getResourceMap(GeneralLedger.class);

	AssetAccount vCurrAssets = new AssetAccount(-1, vRmap.getString("accountNames.currentAssets"),
		vRmap.getString("accountDescriptions.currentAssets"),
		0.0, true);

	LiabilityAccount vCurrLiabilities = new LiabilityAccount(-1, vRmap.getString("accountNames.currentLiabilities"),
		vRmap.getString("accountDescriptions.currentLiabilities"),
		0.0, true);

	EquityAccount vInitialCapital = new EquityAccount(-1, vRmap.getString("accountNames.initialCapital"),
		vRmap.getString("accountDescriptions.initialCapital"),
		0.0, true);

	assetsNode.add(new AccountTreeNode(vCurrAssets));
	liabilitiesNode.add(new AccountTreeNode(vCurrLiabilities));
	equityNode.add(new AccountTreeNode(vInitialCapital));

	//setChangedAndNotifyObservers();
    }
    
}
