/*
 * JournalController.java		1.0.0		09/2009
 * This file contains the controller class of the JAccounting application responsible
 * for journal related actions.
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
import jaccounting.GenericException;
import jaccounting.models.Account;
import jaccounting.models.Journal;
import jaccounting.models.Transaction;
import jaccounting.views.JournalView;
import jaccounting.views.ModifyTransactionBox;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTabbedPane;
import org.jdesktop.application.Action;
import org.jdesktop.application.ResourceMap;

/**
 * JournalController is the controller singleton class managing the journal
 * interface represented by a JournalView object. It provides actions to open the
 * journal interface, edit a transaction selected, add a new transaction under
 * the currently selected transaction and delete a transaction.
 *
 * @author	    Boubacar Diallo
 * @version	    1.0.0
 * @see		    jaccounting.views.JournalView
 * @see		    jaccounting.models.Journal
 * @see		    jaccounting.models.Transaction
 * @since	    1.0.0
 */
public class JournalController extends BaseController {

    /** flag to enable/disable the openNewTransactionBox action */
    private boolean openNewTransactionBoxEnabled;

    /** flag to enable/disable the openEditTransaction action */
    private boolean openEditTransactionBoxEnabled;

    /** flag to enable/disable the deleteTransaction action */
    private boolean deleteTransactionEnabled;


    private JournalController() {
	super();
	enableTransactionActions(true);
	enableEditTransaction(false);
	enableDeleteTransaction(false);
    }

    private static class InstanceHolder {
	private static final JournalController INSTANCE = new JournalController();
    }


    /**
     * Gets the unique instance of JournalController.
     *
     * @return		the unique instance of JournalController
     * @since		1.0.0
     */
    public static JournalController getInstance() {
	return InstanceHolder.INSTANCE;
    }

    public boolean isDeleteTransactionEnabled() {
	return deleteTransactionEnabled;
    }

    public boolean isOpenEditTransactionBoxEnabled() {
	return openEditTransactionBoxEnabled;
    }

    public boolean isOpenNewTransactionBoxEnabled() {
	return openNewTransactionBoxEnabled;
    }

    private Journal getModel() {
	return JAccounting.getApplication().getModelsMngr().getData().getJournal();
    }

    private JournalView getView() {
	ResourceMap vRmap = JAccounting.getApplication().getContext()
					.getResourceMap(JournalView.class);
	JTabbedPane vTabsCont = JAccounting.getApplication().getMainView()
					    .getTabsContainer();
	int vIndex = vTabsCont.indexOfTab(vRmap.getString("title"));

	return (vIndex != -1) ? (JournalView) vTabsCont.getComponentAt(vIndex) : null;
    }

    private ModifyTransactionBox getOrCreateModifyTransactionBox() {
	ModifyTransactionBox rBox = JAccounting.getApplication().getMainView().getModifyTransactionBox();

	if (rBox == null) {
	    rBox = new ModifyTransactionBox(JAccounting.getApplication().getMainFrame(), this);
	    rBox.setLocationRelativeTo(JAccounting.getApplication().getMainFrame());
	    JAccounting.getApplication().getMainView().setModifyTransactionBox(rBox);
	}

	return rBox;
    }

    private void enableTransactionActions(boolean pVal) {
	openNewTransactionBoxEnabled = pVal;
	support.firePropertyChange("openNewTransactionBoxEnabled", !pVal, pVal);
	enableEditTransaction(pVal);
	enableDeleteTransaction(pVal);
    }

    private void enableDeleteTransaction(boolean pVal) {
	deleteTransactionEnabled = pVal;
	support.firePropertyChange("deleteTransactionEnabled", !pVal, pVal);
    }

    private void enableEditTransaction(boolean pVal) {
	openEditTransactionBoxEnabled = pVal;
	support.firePropertyChange("openEditTransactionBoxEnabled", !pVal, pVal);
    }

    /**
     * Opens the journal interface. This methods creates a journal interface and
     * adds it to the content's tabbed pane if does not exist, then selects its tab.
     *
     * @see		    jaccounting.views.JournalView
     * @since		    1.0.0
     */
    public void openJournal() {
	ResourceMap vRmap = JAccounting.getApplication().getContext().getResourceMap(JournalView.class);
	JTabbedPane vTabsCont = JAccounting.getApplication().getMainView().getTabsContainer();
	int vIndex = vTabsCont.indexOfTab(vRmap.getString("title"));

	if (vIndex == -1) {
	    Journal vModel = getModel();

	    JAccounting.getApplication().getProgressReporter()
			.reportUsingKey("messages.insertingJournalTab");
	    vTabsCont.insertTab(vRmap.getString("title"), null,
				new JournalView(this, vModel),
				vRmap.getString("titleTip"), 1);
	    vTabsCont.setSelectedIndex(1);
	    JAccounting.getApplication().getProgressReporter().reportFinished();
	}
	else {
	    vTabsCont.setSelectedIndex(vIndex);
	}
    }

    /**
     * Opens the modal dialog box to add a transaction. This method temporarily
     * creates a Transaction model and attaches it to the transaction editor box.
     *
     * @see		jaccounting.views.ModifyTransactionBox
     * @see		jaccounting.models.Transaction#createTransaction()
     * @since		1.0.0
     */
     @Action(enabledProperty = "openNewTransactionBoxEnabled")
     public void openNewTransactionBox() {
	// get editor box
	ModifyTransactionBox vBox = getOrCreateModifyTransactionBox();
	// set model
	vBox.setIsNew(true);
	vBox.setModel(Transaction.createTransaction());
	vBox.initFormFields();
	// show box
	JAccounting.getApplication().show(vBox);
     }

     /**
      * Opens the modal dialog box to edit the selected transaction in the journal
      * interface.
      *
      * @see		jaccounting.views.ModifyTransactionBox
      * @see		jaccounting.views.JournalView#getCurrentlySelectedTransaction()
      * @since		1.0.0
      */
     @Action(enabledProperty = "openEditTransactionBoxEnabled")
     public void openEditTransactionBox() {
	ModifyTransactionBox vBox = getOrCreateModifyTransactionBox();
	JournalView vView = getView();
	// get currently selected transaction
	Transaction vTrans = vView.getCurrentlySelectedTransaction();
	if (vTrans == null) {
	    Logger.getLogger(this.getClass().getName())
		    .log(Level.WARNING, "Tried to edit transaction while no transaction was selected.");
	    return;
	}

	vBox.setIsNew(false);
	vBox.setModel(vTrans);
	vBox.initFormFields();

	JAccounting.getApplication().show(vBox);
     }

     /**
      * Deletes the selected transaction in the journal interface. This method
      * asks for user confirmation before asking the Journal model to delete
      * the transaction.
      *
      * @see		jaccounting.models.Journal#removeTransaction(int)
      * @see		jaccounting.views.JournalView#getCurrentlySelectedRow()
      * @since		1.0.0
      */
     @Action(enabledProperty = "deleteTransactionEnabled")
     public void deleteTransaction() {
	ResourceMap vRmap = JAccounting.getApplication().getContext().getResourceMap(this.getClass());
	String vMessage = vRmap.getString("confirmActionMessages.deleteTransaction");
	// confirm deletion
	if (!JAccounting.getApplication().getMainView().showConfirmActionBox(vMessage)) {
	    return;
	}

	JournalView vView = getView();
	int vRow = vView.getCurrentlySelectedRow();
	if (vRow == -1) {
	    Logger.getLogger(this.getClass().getName())
		    .log(Level.WARNING, "Tried to delete transaction while no " +
			    "transaction was selected.");
	    return;
	}

	Journal vModel = getModel();
	// remove transaction from journal
	JAccounting.getApplication().getProgressReporter()
		    .reportUsingKey("messages.removingTransaction");
	vModel.removeTransaction(vRow);
	
	JAccounting.getApplication().getProgressReporter().reportFinished();
     }

     /**
      * Updates the properties of the transaction being edited/added in the editor box.
      * If errors occured while updating i.e. some of the entered values are invalid,
      * this method displays error messages in the transaction editor box; otherwise
      * it disposes of the editor box. If the transaction in the editor box is new, it
      * requests the temporary transaction to be permanently added to the Journal
      * model.
      *
      * @see		jaccounting.views.ModifyTransactionBox
      * @see		jaccounting.models.Transaction#update(java.util.Date, 
      *							      java.lang.String, 
      *							      java.lang.String,
      *							      double, 
      *							      jaccounting.models.Account, 
      *							      jaccounting.models.Account)
      * @see		jaccounting.models.Journal#addTransaction(jaccounting.models.Transaction)
      * @since		1.0.0
      */
     @Action
     public void updateTransaction() {
	ModifyTransactionBox vBox = getOrCreateModifyTransactionBox();

	if (vBox != null) {
	    boolean vIsNew = vBox.getIsNew();
	    Map<String, ErrorCode> vErrors = new HashMap<String, ErrorCode>();
	    Transaction vTransaction = vBox.getModel();
	    double vAmount;
	    Account vDebitAccount, vCreditAccount;

	     vBox.clearErrors();
	     // attempt to convert numerics
	    try {
		vAmount = Double.parseDouble(vBox.getAmountInput().getText());
	    }
	    catch (NumberFormatException vEx) {
		vAmount = 0.0;
		vErrors.put("amount", ErrorCode.INVALID_NUMBER_FORMAT);
	    }

	    if (vErrors.isEmpty()) {
		// get the selected accounts
		vDebitAccount = getFullNameAccount((String)vBox.getDebitAccountInput().getSelectedItem());
		vCreditAccount = getFullNameAccount((String)vBox.getCreditAccountInput().getSelectedItem());
		// attempt to update transaction from modify box field values
		JAccounting.getApplication().getProgressReporter()
			.reportUsingKey("messages.updatingTransaction");
		vErrors = vTransaction.update((Date)vBox.getDateInput().getValue(),
						vBox.getRefNoInput().getText(),
						vBox.getMemoInput().getText(),
						vAmount, vDebitAccount, vCreditAccount);
	    }

	    if (vErrors.isEmpty()) {
		if (vIsNew) {
		    addTransaction(vTransaction);
		}
		vBox.dispose();
	    }
	    else {
		vBox.displayErrors(vErrors);
	    }
	}
    }

    private void addTransaction(Transaction pTransaction) {
	Journal vModel = getModel();
	vModel.addTransaction(pTransaction);
    }

    /**
     * Handles bubbling of a transaction selection event from the journal
     * interface. This method enables all transaction related actions.
     *
     * @param pRow	    the row of the selected transaction in the Journal model
     * @since		    1.0.0
     */
    public void transactionSelected(int pRow) {
	enableTransactionActions(true);
    }

    /**
     * Handles bubbling of no transaction selection event from the journal
     * interface. This method disables all transaction related to an account.
     *
     * @since		    1.0.0
     */
    public void noTransactionSelected() {
	enableTransactionActions(true);
	enableDeleteTransaction(false);
	enableEditTransaction(false);
    }

    /**
     * Gets a list of the full names of all accounts that allow transactions. This
     * is a convinience methods for view objects under the JournalController's control.
     * It effectively requests the list from the GeneralLedgerController
     *
     * @return		    the list of full names of accounts allowing transactions
     * @see		    GeneralLedgerController#getTransactionnableAccountFullNames()
     * @since		    1.0.0
     */
    public String[] getTransactionnableAccountFullNames() {
	return GeneralLedgerController.getInstance().getTransactionnableAccountFullNames();
    }

    /**
     * Gets the full name of a given Account. This is a convinience methods for
     * view objects under the JournalController's control. It effectively requests
     * the full name from the GeneralLedgerController
     *
     * @param pAcct	    the Account whose full name to retrieve
     * @return		    the full name of the Account
     * @see		    GeneralLedgerController#getAccountFullName(jaccounting.models.Account)
     * @since		    1.0.0
     */
    public String getAccountFullName(Account pAcct) {
	return GeneralLedgerController.getInstance().getAccountFullName(pAcct);
    }

    private Account getFullNameAccount(String pFullName) {
	return GeneralLedgerController.getInstance().getFullNameAccount(pFullName);
    }

}
