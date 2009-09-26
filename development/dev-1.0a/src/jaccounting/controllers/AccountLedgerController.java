/*
 * AccountLedgerController.java		1.0.0		09/2009
 * This file contains the controller class of the JAccounting application responsible
 * for an account's ledger actions.
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

package jaccounting.controllers;

import jaccounting.JAccounting;
import jaccounting.models.Account;
import jaccounting.views.AccountLedgerView;
import javax.swing.JTabbedPane;

/**
 * AccountLedgerController is the controller singleton class that manages an account
 * ledger inteerface. Its instance takes care of opening account ledger interfaces
 * represented by {@link jaccounting.views.AccountLedgerView AccountLedgerView}
 * as tabs in the content's tabbed pane of the main frame.
 *
 * @author	    Boubacar Diallo
 * @version	    1.0.0
 * @see		    jaccounting.views.AccountLedgerView
 * @see		    jaccounting.models.Account
 * @since	    1.0.0
 */
public class AccountLedgerController {

    private AccountLedgerController() {
	super();
    }

    private static class InstanceHolder {
        private static final AccountLedgerController INSTANCE = new AccountLedgerController();
    }

    /**
     * Gets the unique instance of the AccountLedgerController.
     *
     * @return		the unique instance of the AccountLedgerController
     * @since		1.0.0
     */
    public static AccountLedgerController getInstance() {
        return InstanceHolder.INSTANCE;
    }

    /**
     * Opens an account ledger interface for an Account in a tab of the content's
     * tabbed pane of the main frame.
     *
     * @param pAccount		    the Account whose ledger interface to open
     * @since			    1.0.0
     */
    public void openAccountLedger(Account pAccount) {
	JTabbedPane vTabsCont = JAccounting.getApplication().getMainView().getTabsContainer();
	int vIndex = getAccountLedgerTabIndex(pAccount);

	if (vIndex == -1) {
	    JAccounting.getApplication().getProgressReporter()
			.reportUsingKey("messages.insertingAccountLedgerTab");
	    vTabsCont.addTab(pAccount.getName(), null,
		    new AccountLedgerView(this, pAccount),
		    GeneralLedgerController.getInstance().getAccountFullName(pAccount));
	    vTabsCont.setSelectedIndex(vTabsCont.getTabCount()-1);
	    JAccounting.getApplication().getProgressReporter().reportFinished();
	}
	else {
	    vTabsCont.setSelectedIndex(vIndex);
	}
    }

    private int getAccountLedgerTabIndex(Account pAccount) {
	JTabbedPane vTabsCont = JAccounting.getApplication().getMainView().getTabsContainer();
	int rIndex = vTabsCont.getTabCount() -1;
	String vFullName = GeneralLedgerController.getInstance().getAccountFullName(pAccount);

	/** Avoid the first the 2 tabs which are the general ledger and journal tabs */
	while (rIndex >= 2) {
	    if (vFullName.equals(vTabsCont.getToolTipTextAt(rIndex))) {
		break;
	    }
	    rIndex--;
	}
	if (rIndex < 2) rIndex = -1;

	return rIndex;
    }

}
