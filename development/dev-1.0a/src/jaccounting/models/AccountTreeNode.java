/*
 * AccountTreeNode.java		    1.0.0	    09/2009
 * This file contains the account list's tree node class of the JAccounting application.
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

import jaccounting.JAccounting;
import java.util.Enumeration;
import java.util.List;
import javax.swing.tree.DefaultMutableTreeNode;

/***
 * AccountTreeNode is the class representing a tree node in the accounts tree. An
 * AccountTreeNode object has an Account as its {@code userObject}. It is responsible
 * for figuring out its Account's full name i.e. its Account's name preceded with
 * its parents' Account's names.
 *
 * @author	    Boubacar Diallo
 * @version	    1.0.0
 * @see		    GeneralLedger
 * @see		    Account
 * @since	    1.0.0
 */
public class AccountTreeNode extends DefaultMutableTreeNode {

    /**
     * TODO: Currently Account full names are computed everything by function
     * calls and not cached. A better way would be to have an instance variable
     * here that will hang on to that full name. We could also use a hashmap
     * to map full names to AccountTreeNode objects for faster lookup. The danger
     * with the above suggestions is to forget updating this setup when an Account
     * has been edited and its name changed. The node can simply observe the
     * Account for changes and compare its full name property's last token
     * against the Account's new name before refreshing the above setup for its
     * subtree.
     */

    /**
     * Sole constructor. This constructor initializes this AccountTreeNode with
     * the given Account object.
     *
     * @param pAccount		    the Account object
     * @see			    Account
     * @since			    1.0.0
     */
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
	Enumeration vChildren = children();

	/**
	 * Do a sort of in-order traversal of this node's subtree; we visit the
	 * root, add its full name to the list and repeat the operation for the
	 * children.
	 */

	if (!pExcludeNonTransactionnable
		|| (pExcludeNonTransactionnable && vAcct.isTransactionsEnabled())) {
	    pList.add(vFullName);
	}
	while (vChildren.hasMoreElements()) {
	    ((AccountTreeNode) vChildren.nextElement()).buildFullNames(pList,
		    vFullName+".", pExcludeNonTransactionnable);
	}
    }

    String getFullName() {
	String rFullName= "";

	/**
	 * The full name is basically this node's Account's name preceded with
	 * this node's parent's Account's full name and seperated by a dot.
	 */

	if (!isRoot()) {
	    if (!isTopLevel()) {
		rFullName = ((AccountTreeNode) getParent()).getFullName();
	    }
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

	/**
	 * This is another in-order traversal, where we visit the root, check to
	 * see if the given name matches this node's Account's name; if so we
	 * are done; if not make sure the first token of the given name matches
	 * this node's Account's name and repeat the operation through its children
	 * with the remaining tokens of the given name.
	 */

	if (vIndex != -1) {
	    /**
	     * the full name being sort of a path of account names seperated by
	     * dots, we consider the first element of the path.
	     */
	    vName = pFullName.substring(0, vIndex);
	}
	if (((Account) getUserObject()).getName().equals(vName)) {
	    /**
	     * we are on the right track; this nodes Account's name is the first
	     * element of the path.
	     */
	    if (vIndex == -1) {
		/** the first element is the only element of the path in this case */
		rAcct = (Account) getUserObject();
	    }
	    else {
		/** we proceed further to the next elements of the path */
		vName = pFullName.substring(vIndex+1);
		Enumeration vChildren = children();

		while (vChildren.hasMoreElements()) {
		    rAcct = ((AccountTreeNode) vChildren.nextElement())
			    .getAccount(vName);
		    if (rAcct != null) {
			break;
		    }
		}
	    }
	}

	return rAcct;
    }

    Account getChildAccountWithName(String pName) {
	Account rAcct = null;
	Enumeration vChildren = children();

	while (vChildren.hasMoreElements()) {
	    rAcct = (Account) ((AccountTreeNode) vChildren.nextElement())
				.getUserObject();
	    if (rAcct.getName().equals(pName)) {
		break;
	    }
	    else {
		rAcct = null;
	    }
	}

	return rAcct;
    }

    boolean isAccountNameUniqueAmongChildren(String pName) {
	return (getChildAccountWithName(pName) == null);
    }

    void remove() throws NotTransactionnableAccountException {
	List<Transaction> vTransactions = ((Account)getUserObject())
					    .getAccountTransactions();
	Enumeration vChildren = children();

	while (vChildren.hasMoreElements()) {
	    ((AccountTreeNode) vChildren.nextElement()).remove();
	}
	removeFromParent();
	if (vTransactions.size() > 0) {
	    JAccounting.getApplication().getModelsMngr().getData().getJournal()
		    .removeTransactions(vTransactions);
	}
    }

}
