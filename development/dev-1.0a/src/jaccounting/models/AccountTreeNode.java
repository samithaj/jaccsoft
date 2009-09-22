/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jaccounting.models;

import jaccounting.JAccounting;
import java.util.Enumeration;
import java.util.List;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 *
 * @author bouba
 */
public class AccountTreeNode extends DefaultMutableTreeNode {

    public AccountTreeNode(Account pAccount) {
	super(pAccount);
    }

    boolean canBeEdited() {
	return !isTopLevel();
    }

    boolean canBeRemoved() {
	return !isTopLevel();
    }

    boolean canHaveChildren() {
	return !isRoot();
    }

    boolean isAccountGroup() {
	if (isRoot()) return true;
	return (getChildCount() > 0);
    }

    boolean isTopLevel() {
	return (getLevel() <= 1);
    }

    void buildFullNames(List<String> pList, String pPrefix, boolean pExcludeNonTransactionnable) {
	Account vAcct = ((Account) getUserObject());
	String vFullName = pPrefix + vAcct.getName();
	if (!pExcludeNonTransactionnable || (pExcludeNonTransactionnable && vAcct.isTransactionsEnabled())) {
	    pList.add(vFullName);
	}
	Enumeration vChildren = children();
	while (vChildren.hasMoreElements()) {
	    ((AccountTreeNode) vChildren.nextElement()).buildFullNames(pList, vFullName+".", pExcludeNonTransactionnable);
	}
    }

    public String getFullName() {
	String rFullName= "";

	if (!isRoot()) {
	    if (!isTopLevel()) rFullName = ((AccountTreeNode) getParent()).getFullName();
	    if (rFullName.length() > 0) {
		rFullName += ".";
	    }
	    rFullName += ((Account) getUserObject()).getName();
	}

	return rFullName;
    }

    Account getAccount(String pFullName) {
	Account rAcct = null;
	String vName = pFullName;
	int vIndex = pFullName.indexOf(".");

	if (vIndex != -1) {
	    vName = pFullName.substring(0, vIndex);
	}
	if (((Account) getUserObject()).getName().equals(vName)) {
	    if (vIndex == -1) rAcct = (Account) getUserObject();
	    else {
		vName = pFullName.substring(vIndex+1);
		Enumeration vChildren = children();
		while (vChildren.hasMoreElements()) {
		    rAcct = ((AccountTreeNode) vChildren.nextElement()).getAccount(vName);
		    if (rAcct != null) break;
		}
	    }
	}

	return rAcct;
    }

    Account getChildAccountWithName(String pName) {
	Account rAcct = null;

	Enumeration vChildren = children();
	while (vChildren.hasMoreElements()) {
	    rAcct = (Account) ((AccountTreeNode) vChildren.nextElement()).getUserObject();
	    if (rAcct.getName().equals(pName)) break;
	    else rAcct = null;
	}

	return rAcct;
    }

    boolean isAccountNameUniqueAmongChildren(String pName) {
	return (getChildAccountWithName(pName) == null);
    }

    void remove() throws NotTransactionnableAccountException {
	List<Transaction> vTransactions = ((Account)getUserObject()).getAccountTransactions();
	Enumeration vChildren = children();

	while (vChildren.hasMoreElements()) {
	    ((AccountTreeNode) vChildren.nextElement()).remove();
	}
	removeFromParent();
	if (vTransactions.size() > 0) {
	    JAccounting.getApplication().getModelsMngr().getData().getJournal().removeTransactions(vTransactions);
	}
    }

}
