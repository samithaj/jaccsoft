/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jaccounting.controllers;

import jaccounting.JAccounting;
import jaccounting.models.Account;
import jaccounting.views.AccountLedgerView;
import javax.swing.JTabbedPane;

/**
 *
 * @author bouba
 */
public class AccountLedgerController {

    private AccountLedgerController() {
	super();
    }

    private static class AccountLedgerControllerHolder {
        private static final AccountLedgerController INSTANCE = new AccountLedgerController();
    }

    public static AccountLedgerController getInstance() {
        return AccountLedgerControllerHolder.INSTANCE;
    }

    public void openAccountLedger(Account pAccount) {
	JTabbedPane vTabsCont = JAccounting.getApplication().getMainView().getTabsContainer();
	int vIndex = getAccountLedgerTabIndex(pAccount);

	if (vIndex == -1) {
	    vTabsCont.addTab(pAccount.getName(), null,
		    new AccountLedgerView(this, pAccount),
		    GeneralLedgerController.getInstance().getAccountFullName(pAccount));
	    vTabsCont.setSelectedIndex(vTabsCont.getTabCount()-1);
	} else {
	    vTabsCont.setSelectedIndex(vIndex);
	}
    }

    private int getAccountLedgerTabIndex(Account pAccount) {
	JTabbedPane vTabsCont = JAccounting.getApplication().getMainView().getTabsContainer();
	int rIndex = vTabsCont.getTabCount() -1;
	String vFullName = GeneralLedgerController.getInstance().getAccountFullName(pAccount);

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
