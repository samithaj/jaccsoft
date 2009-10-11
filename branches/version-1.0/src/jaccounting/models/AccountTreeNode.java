/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jaccounting.models;

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

    boolean canBeRemoved() {
	return !this.isTopLevel();
    }

    boolean canHaveChildren() {
	return !this.isRoot();
    }

    boolean isAccountGroup() {
	if (this.isRoot()) return true;
	return (this.getChildCount() > 0);
    }

    boolean isTopLevel() {
	return (this.getLevel() <= 1);
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
<<<<<<< .mine
		    rAcct = ((AccountTreeNode) vChildren.nextElement()).getAccount(vName);
		    if (rAcct != null) break;
		    }
=======
		    rAcct = ((AccountTreeNode) vChildren.nextElement()).getAccount(vName);
		    if (rAcct != null) break;
>>>>>>> .r71
		}
	    }

	return rAcct;
    }

    Account getChildAccountWithName(String pName) {
	Account rAcct = null;

	Enumeration vChildren = children();
	while (vChildren.hasMoreElements()) {
<<<<<<< .mine
	    rAcct = (Account) ((AccountTreeNode) vChildren.nextElement()).getUserObject();
	    if (rAcct.getName().equals(pName)) break;
	    else rAcct = null;
	    }
=======
	    rAcct = (Account) ((AccountTreeNode) vChildren.nextElement()).getUserObject();
	    if (rAcct.getName().equals(pName)) break;
	    else rAcct = null;
	}
>>>>>>> .r71

	return rAcct;
    }

    boolean isAccountNameUniqueAmongChildren(String pName) {
	return (getChildAccountWithName(pName) == null);
    }

<<<<<<< .mine
	}
=======
}
>>>>>>> .r71
