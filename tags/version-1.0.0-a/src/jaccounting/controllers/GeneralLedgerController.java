/*
 * GeneralLedgerController.java		1.0.0		09/2009
 * This file contains the controller class of the JAccounting application responsible
 * for general ledger related actions.
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
import jaccounting.ErrorCode;
import jaccounting.models.Account;
import jaccounting.models.GeneralLedger;
import jaccounting.models.InvalidAccountTypeException;
import jaccounting.views.GeneralLedgerView;
import jaccounting.views.ModifyAccountBox;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTabbedPane;
import org.jdesktop.application.Action;
import org.jdesktop.application.ResourceMap;

/**
 * GeneralLedgerController is the controller singleton class managing the general ledger
 * interface represented by a GeneralLedgerView. It provides actions to open the
 * general ledger interface, edit an account selected in the general ledger, add a new account
 * under the currently selected account and delete an account. It also serves as
 * an intermediary between the GeneralLedger model and other controllers to limit
 * coupling.
 *
 * @author	    Boubacar Diallo
 * @version	    1.0.0
 * @see		    jaccounting.views.GeneralLedgerView
 * @see		    jaccounting.models.GeneralLedger
 * @see		    jaccounting.models.Account
 * @since	    1.0.0
 */
public class GeneralLedgerController extends BaseController {

    /** flag to enable/disable the openAccountLedger action */
    private boolean openAccountLedgerEnabled;

    /** flag to enable/disable the openNewAccountBox action */
    private boolean openNewAccountBoxEnabled;

    /** flag to enable/disable the openEditAccountBox action */
    private boolean openEditAccountBoxEnabled;

    /** flag to enable/disable the deleteAccount action */
    private boolean deleteAccountEnabled;


    private GeneralLedgerController() {
	super();
	enableAccountActions(false);
    }

    private static class InstanceHolder {
	private static final GeneralLedgerController INSTANCE = new GeneralLedgerController();
    }


    /**
     * Gets the unique instance of GeneralLedgerController.
     *
     * @return		the unique instance of GeneralLedgerController
     * @since		1.0.0
     */
    public static GeneralLedgerController getInstance() {
	return InstanceHolder.INSTANCE;
    }

    public boolean isOpenNewAccountBoxEnabled() {
	return openNewAccountBoxEnabled;
    }

    public boolean isDeleteAccountEnabled() {
	return deleteAccountEnabled;
    }

    public boolean isOpenAccountLedgerEnabled() {
	return openAccountLedgerEnabled;
    }

    public boolean isOpenEditAccountBoxEnabled() {
	return openEditAccountBoxEnabled;
    }

    private GeneralLedger getModel() {
	return JAccounting.getApplication().getModelsMngr().getData().getGeneralLedger();
    }

    private GeneralLedgerView getView() {
	ResourceMap vRmap = JAccounting.getApplication().getContext()
					.getResourceMap(GeneralLedgerView.class);
	JTabbedPane vTabsCont = JAccounting.getApplication().getMainView()
					    .getTabsContainer();
	int vIndex = vTabsCont.indexOfTab(vRmap.getString("title"));

	return (vIndex != -1) ? (GeneralLedgerView) vTabsCont.getComponentAt(vIndex) : null;
    }

    /**
     * Opens the general ledger interface. This methods creates a general ledger
     * interface and adds it to the content's tabbed pane if does not exist, then
     * selects its tab.
     *
     * @see		    jaccounting.views.GeneralLedgerView
     * @since		    1.0.0
     */
    public void openGeneralLedger() {
	ResourceMap vRmap = JAccounting.getApplication().getContext().getResourceMap(GeneralLedgerView.class);
	GeneralLedger vModel = getModel();
	JTabbedPane vTabsCont = JAccounting.getApplication().getMainView().getTabsContainer();
	int vIndex = vTabsCont.indexOfTab(vRmap.getString("title"));

	// create general ledger view
	if (vIndex == -1) {
	    JAccounting.getApplication().getProgressReporter()
			.reportUsingKey("messages.insertingGeneralLedgerTab");
	    vTabsCont.insertTab(vRmap.getString("title"), null,
				new GeneralLedgerView(this, vModel),
				vRmap.getString("titleTip"), 0);
	    vTabsCont.setSelectedIndex(0);
	    JAccounting.getApplication().getProgressReporter().reportFinished();
	} 
	// show general ledger view
	else {
	    vTabsCont.setSelectedIndex(vIndex);
	}
    }

    private ModifyAccountBox getOrCreateModifyAccountBox() {
	ModifyAccountBox rBox = JAccounting.getApplication().getMainView().getModifyAccountBox();

	if (rBox == null) {
	    rBox = new ModifyAccountBox(JAccounting.getApplication().getMainFrame(), this);
	    rBox.setLocationRelativeTo(JAccounting.getApplication().getMainFrame());
	    JAccounting.getApplication().getMainView().setModifyAccountBox(rBox);
	}

	return rBox;
    }

    /**
     * Opens the modal dialog box to add an account. This method temporarily
     * creates an Account model of the same type as the currently selected account
     * in the general ledger interface and attaches it to the account editor box.
     *
     * @see		jaccounting.views.ModifyAccountBox
     * @see		jaccounting.views.GeneralLedgerView#getCurrentlySelectedAccount()
     * @see		jaccounting.models.Account#createAccount(jaccounting.models.Account.Type)
     * @since		1.0.0
     */
    @Action(enabledProperty = "openNewAccountBoxEnabled")
    public void openNewAccountBox() {
	GeneralLedgerView vView = getView();
	// get currently selected account
	Account vAcct = vView.getCurrentlySelectedAccount();
	if (vAcct == null) {
	    Logger.getLogger(this.getClass().getName())
		    .log(Level.WARNING, "Tried to open account editor while no account was selected.");
	    return;
	}
	// get account editor box
	ModifyAccountBox vBox = getOrCreateModifyAccountBox();

	vBox.setIsNew(true);
	try {
	    vBox.setModel(Account.createAccount(vAcct.getType()));
	    vBox.initFormFields();

	    JAccounting.getApplication().show(vBox);
	} 
	catch (InvalidAccountTypeException ex) {
	    Logger.getLogger(GeneralLedgerController.class.getName())
		    .log(Level.SEVERE, "Tried to create account while parent account '"
					+ vAcct.getName() + "' had invalid type.", ex);
	} 

    }

    /**
     * Opens the modal dialog box to edit the selected account in the general
     * ledger interface.
     *
     * @see		jaccounting.views.ModifyAccountBox
     * @see		jaccounting.views.GeneralLedgerView#getCurrentlySelectedAccount()
     * @since		1.0.0
     */
    @Action(enabledProperty = "openEditAccountBoxEnabled")
    public void openEditAccountBox() {
	GeneralLedgerView vView = getView();
	// get currently selected account
	Account vAcct = vView.getCurrentlySelectedAccount();
	if (vAcct == null) {
	    Logger.getLogger(this.getClass().getName())
		    .log(Level.WARNING, "Tried to edit account while no account was selected.");
	    return;
	}
	// get account editor box
	ModifyAccountBox vBox = getOrCreateModifyAccountBox();

	vBox.setIsNew(false);
	vBox.setModel(vAcct);
	vBox.initFormFields();

	JAccounting.getApplication().show(vBox);
    }

    /**
     * Deletes the selected account in the general ledger interface. This method
     * asks for user confirmation before asking the GeneralLedger model to delete
     * the account.
     *
     * @see		jaccounting.models.GeneralLedger#removeAccount(int) 
     * @see		jaccounting.views.GeneralLedgerView#getCurrentlySelectedAccount()
     * @since		1.0.0
     */
    @Action(enabledProperty = "deleteAccountEnabled")
    public void deleteAccount() {
	ResourceMap vRmap = JAccounting.getApplication().getContext().getResourceMap(this.getClass());
	String vMessage = vRmap.getString("confirmActionMessages.deleteAccount");
	// confirm deletion
	if (!JAccounting.getApplication().getMainView().showConfirmActionBox(vMessage)) {
	    return;
	}

	GeneralLedgerView vView = getView();
	// get currently selected row
	int vRow = vView.getCurrentlySelectedRow();
	if (vRow == -1) {
	    Logger.getLogger(this.getClass().getName())
		    .log(Level.WARNING, "Tried to delete account while no account was selected.");
	    return;
	}

	GeneralLedger vModel = getModel();
	// remove account node
	JAccounting.getApplication().getProgressReporter()
		    .reportUsingKey("messages.removingAccount");
	vModel.removeAccount(vRow);
	
	JAccounting.getApplication().getProgressReporter().reportFinished();
    }

    /**
     * Opens the account ledger interface of the currently selected account in
     * the general ledger interface. This method delegates the job to {@link
     * AccountLedgerController#openAccountLedger(jaccounting.models.Account)}.
     *
     * @see		AccountLedgerController#openAccountLedger(jaccounting.models.Account)
     * @see		jaccounting.views.GeneralLedgerView#getCurrentlySelectedAccount()
     * @since		1.0.0
     */
    @Action(enabledProperty = "openAccountLedgerEnabled")
    public void openAccountLedger() {
	GeneralLedgerView vView = getView();
	// get currently selected account
	Account vAcct = vView.getCurrentlySelectedAccount();
	if (vAcct == null) {
	    Logger.getLogger(this.getClass().getName())
		    .log(Level.WARNING, "Tried to open account ledger while no account was selected.");
	    return;
	}
	// open its ledger through AccountLedgerController
	JAccounting.getApplication().getProgressReporter()
			.reportUsingKey("messages.openingAccountLedger");
	AccountLedgerController.getInstance().openAccountLedger(vAcct);
	JAccounting.getApplication().getProgressReporter().reportFinished();
    }

    /**
     * Updates the properties of the account being edited/added in the editor box. If
     * errors occured while updating i.e. some of the entered values are invalid,
     * this method displays error messages in the account editor box; otherwise
     * it disposes of the editor box. If the account in the editor box is new, it
     * requests the temporary account to be permanently inserted into the GeneralLedger
     * model as a child of the currently selected account.
     *
     * @see		jaccounting.views.ModifyAccountBox
     * @see		jaccounting.models.Account#update(int, java.lang.String,
     *							    java.lang.String)
     * @see		jaccounting.views.GeneralLedgerView#getCurrentlySelectedAccount()
     * @see		jaccounting.models.GeneralLedger#insertChildAccount(int,
     *							    jaccounting.models.Account)
     * @since		1.0.0
     */
    @Action
    public void updateAccount() {
	ModifyAccountBox vBox = JAccounting.getApplication().getMainView().getModifyAccountBox();

	if (vBox != null) {
	    boolean vIsNew = vBox.getIsNew();
	    Account vAcct = vBox.getModel();
	    Map<String, ErrorCode> vErrors = new HashMap<String, ErrorCode>();
	    int vNumber;

	    vBox.clearErrors();
	    // attempt to convert numerics
	    try {
		vNumber = Integer.parseInt(vBox.getNumberTextField().getText());
	    }
	    catch (NumberFormatException vEx) {
		vNumber = -1;
		if (vBox.getNumberTextField().getText().length() > 0) {
		    vErrors.put("number", ErrorCode.INVALID_NUMBER_FORMAT);
		}
	    }

	    // TODO: CHECK THAT ACCOUNT NAME ENTERED IS UNIQUE AMONG ITS FUTURE SIBLINGS

	    if (vErrors.isEmpty()) {
		JAccounting.getApplication().getProgressReporter()
			.reportUsingKey("messages.updatingAccount");
		// attempt to update account from modify box field values
		vErrors = vAcct.update(vNumber, vBox.getNameTextField().getText(),
					vBox.getDescriptionTextField().getText());
		JAccounting.getApplication().getProgressReporter().reportFinished();
	    }

	    if (vErrors.isEmpty()) {
		if (vIsNew) {
		    addAccount(vAcct);
		}
		vBox.dispose();
	    }
	    else {
		vBox.displayErrors(vErrors);
	    }
	}
    }

    private void addAccount(Account pAccount) {
	GeneralLedgerView vView = getView();
	GeneralLedger vModel = getModel();
	// get currently selected account row
	int vRow = vView.getCurrentlySelectedRow();
	if (vRow == -1) return;
	// insert new account under selected account's node
	vModel.insertChildAccount(vRow, pAccount);
	// select parent account
	vView.selectAccountAtRow(vRow);
    }

    /**
     * Handles bubbling of an account selection event from the general ledger
     * interface. This method looks at the selected row and enable/disable actions
     * accordingly.
     *
     * @param pRow	    the absolute row of the selected account in the GeneralLedger model
     * @see		    jaccounting.models.GeneralLedger#canAccountBeEdited(int)
     * @see		    jaccounting.models.GeneralLedger#canAccountBeRemoved(int)
     * @see		    jaccounting.models.GeneralLedger#isAccountTopLevel(int)
     * @since		    1.0.0
     */
    public void accountSelected(int pRow) {
	if (pRow == 0) {
	    noAccountSelected();
	}
	else {
	    // enable actions
	    enableAccountActions(true);
	    GeneralLedger vModel = getModel();
	    // disable edit for top nodes
	    if (!vModel.canAccountBeEdited(pRow)) {
		enableEditAccount(false);
	    }
	    // disable deletion for special accounts
	    if (!vModel.canAccountBeRemoved(pRow)) {
		enableDeleteAccount(false);
	    }
	    // disable opening for special accounts
	    if (vModel.isAccountTopLevel(pRow)) {
		enableOpenAccountLedger(false);
	    }
	}
    }

    /**
     * Handles bubbling of no account selection event from the general ledger
     * interface. This method disables all actions related to an account.
     *
     * @since		    1.0.0
     */
    public void noAccountSelected() {
	// disable actions
	enableAccountActions(false);
    }

    private void enableAccountActions(boolean pVal) {
	openNewAccountBoxEnabled = pVal;
	support.firePropertyChange("openNewAccountBoxEnabled", !pVal, pVal);
	enableEditAccount(pVal);
	enableOpenAccountLedger(pVal);
	enableDeleteAccount(pVal);
    }

    private void enableEditAccount(boolean pVal) {
	openEditAccountBoxEnabled = pVal;
	support.firePropertyChange("openEditAccountBoxEnabled", !pVal, pVal);
    }

    private void enableDeleteAccount(boolean pVal) {
	deleteAccountEnabled = pVal;
	support.firePropertyChange("deleteAccountEnabled", !pVal, pVal);
    }

    private void enableOpenAccountLedger(boolean pVal) {
	openAccountLedgerEnabled = pVal;
	support.firePropertyChange("openAccountLedgerEnabled", !pVal, pVal);
    }

    /**
     * Gets a list of the full names of all accounts that allow transactions. This
     * is a convinience methods for other controllers to access the GeneralLedger
     * model.
     *
     * @return		    the list of full names of accounts allowing transactions
     * @see		    jaccounting.models.GeneralLedger#getTransactionnableAccountFullNames()
     * @since		    1.0.0
     */
    public String[] getTransactionnableAccountFullNames() {
	return getModel().getTransactionnableAccountFullNames();
    }

    /**
     * Gets the full name of a given Account. This is a convinience methods for
     * other controllers to access the GeneralLedger model.
     *
     * @param pAcct	    the Account whose full name to retrieve
     * @return		    the full name of the Account
     * @see		    jaccounting.models.GeneralLedger#getAccountFullName(jaccounting.models.Account)
     * @since		    1.0.0
     */
    public String getAccountFullName(Account pAcct) {
	return getModel().getAccountFullName(pAcct);
    }

    /**
     * Gets the Account of a given full name. This is a convinience methods for
     * other controllers to access the GeneralLedger model.
     *
     * @param pFullName	    the full name of the Account
     * @return		    the Account of the given full name
     * @see		    jaccounting.models.GeneralLedger#getAccount(java.lang.String)
     * @since		    1.0.0
     */
    public Account getFullNameAccount(String pFullName) {
	return getModel().getAccount(pFullName);
    }

}
