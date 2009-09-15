/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jaccounting.controllers;

import jaccounting.JAccounting;
import jaccounting.exceptions.ErrorCode;
import jaccounting.models.Account;
import jaccounting.models.Journal;
import jaccounting.models.Transaction;
import jaccounting.views.JournalView;
import jaccounting.views.ModifyTransactionBox;
import java.beans.PropertyChangeSupport;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JTabbedPane;
import org.jdesktop.application.Action;
import org.jdesktop.application.ResourceMap;

/**
 *
 * @author bouba
 */
public class JournalController extends BaseController {

    private boolean openNewTransactionBoxEnabled;
    private boolean openEditTransactionBoxEnabled;
    private boolean deleteTransactionEnabled;

    private JournalController() {
	support = new PropertyChangeSupport(this);
	enableTransactionActions(true);
	enableEditTransaction(false);
	enableDeleteTransaction(false);
    }

    private static class JournalControllerHolder {
	private static final JournalController INSTANCE = new JournalController();
    }

    public static JournalController getInstance() {
	return JournalControllerHolder.INSTANCE;
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

    protected Journal getModel() {
	return JAccounting.getApplication().getModelsMngr().getData().getJournal();
    }

    protected JournalView getView() {
	ResourceMap vRmap = JAccounting.getApplication().getContext().getResourceMap(JournalView.class);
	JTabbedPane vTabsCont = JAccounting.getApplication().getView().getTabsContainer();
	int vIndex = vTabsCont.indexOfTab(vRmap.getString("title"));

	if (vIndex != -1) {
	    return (JournalView) vTabsCont.getComponentAt(vIndex);
	}

	return null;
    }

    protected ModifyTransactionBox getOrCreateModifyTransactionBox() {
	ModifyTransactionBox rBox = JAccounting.getApplication().getView().getModifyTransactionBox();

	if (rBox == null) {
	    rBox = new ModifyTransactionBox(JAccounting.getApplication().getMainFrame(), this);
	    rBox.setLocationRelativeTo(JAccounting.getApplication().getMainFrame());
	    JAccounting.getApplication().getView().setModifyTransactionBox(rBox);
	}

	return rBox;
    }

    protected void enableTransactionActions(boolean pVal) {
	openNewTransactionBoxEnabled = pVal;
	support.firePropertyChange("openNewTransactionBoxEnabled", !pVal, pVal);
	enableEditTransaction(pVal);
	enableDeleteTransaction(pVal);
    }

    protected void enableDeleteTransaction(boolean pVal) {
	deleteTransactionEnabled = pVal;
	support.firePropertyChange("deleteTransactionEnabled", !pVal, pVal);
    }

    protected void enableEditTransaction(boolean pVal) {
	openEditTransactionBoxEnabled = pVal;
	support.firePropertyChange("openEditTransactionBoxEnabled", !pVal, pVal);
    }

    public void openJournal() {
	ResourceMap vRmap = JAccounting.getApplication().getContext().getResourceMap(JournalView.class);
	JTabbedPane vTabsCont = JAccounting.getApplication().getView().getTabsContainer();
	int vIndex = vTabsCont.indexOfTab(vRmap.getString("title"));

	if (vIndex == -1) {
	    Journal vModel = getModel();

	    vTabsCont.insertTab(vRmap.getString("title"), null,
		    new JournalView(this, vModel),
		    vRmap.getString("titleTip"), 1);
	    vTabsCont.setSelectedIndex(1);
	} else {
	    vTabsCont.setSelectedIndex(vIndex);
	}
    }

     @Action(enabledProperty = "openNewTransactionBoxEnabled")
     public void openNewTransactionBox() {
	// get form box
	ModifyTransactionBox vBox = getOrCreateModifyTransactionBox();
	// set model
	vBox.setIsNew(true);
	vBox.setModel(Transaction.createTransaction());
	vBox.initFormFields();
	// show box
	JAccounting.getApplication().show(vBox);
     }

     @Action(enabledProperty = "openEditTransactionBoxEnabled")
     public void openEditTransactionBox() {
	ModifyTransactionBox vBox = getOrCreateModifyTransactionBox();
	JournalView vView = getView();

	// get currently selected transaction
	Transaction vTrans = vView.getCurrentlySelectedTransaction();
	if (vTrans == null) {
	    return;
	}

	vBox.setIsNew(false);
	vBox.setModel(vTrans);
	vBox.initFormFields();

	JAccounting.getApplication().show(vBox);
     }

     @Action(enabledProperty = "deleteTransactionEnabled")
     public void deleteTransaction() {
	ResourceMap vRmap = JAccounting.getApplication().getContext().getResourceMap(this.getClass());
	String vMessage = vRmap.getString("confirmActionMessages.deleteTransaction");
	// confirm deletion
	if (!JAccounting.getApplication().getView().showConfirmActionBox(vMessage)) {
	    return;
	}
	JournalView vView = getView();
	int vRow = vView.getCurrentlySelectedRow();
	if (vRow == -1) return;
	Journal vModel = getModel();
	// remove account node
	vModel.removeTransaction(vRow);
     }

     @Action
     public void modifyTransaction() {
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
	    } catch (NumberFormatException vEx) {
		vAmount = 0.0;
		vErrors.put("amount", ErrorCode.INVALID_NUMBER_FORMAT);
	    }

	    if (vErrors.isEmpty()) {
		// get the selected accounts
		vDebitAccount = getFullNameAccount((String)vBox.getDebitAccountInput().getSelectedItem());
		vCreditAccount = getFullNameAccount((String)vBox.getCreditAccountInput().getSelectedItem());
		// attempt to update transaction from modify box field values
		vErrors = vTransaction.updateProperties((Date)vBox.getDateInput().getValue(),
							vBox.getRefNoInput().getText(),
							vBox.getMemoInput().getText(),
							vAmount, vDebitAccount, vCreditAccount);
	    }

	    if (vErrors.isEmpty()) {
		if (vIsNew) {
		    addTransaction(vTransaction);
		}
		vBox.dispose();
	    } else {
		vBox.displayErrors(vErrors);
	    }
	}
    }

    private void addTransaction(Transaction pTransaction) {
	Journal vModel = getModel();
	vModel.addTransaction(pTransaction);
    }

    public void transactionSelected(int pRow) {
	enableTransactionActions(true);
    }

    public void noTransactionSelected() {
	enableTransactionActions(true);
	enableDeleteTransaction(false);
	enableEditTransaction(false);
    }

    public String[] getTransactionnableAccountFullNames() {
	return GeneralLedgerController.getInstance().getTransactionnableAccountFullNames();
    }

    public String getAccountFullName(Account pAcct) {
	return GeneralLedgerController.getInstance().getAccountFullName(pAcct);
    }

    Account getFullNameAccount(String pFullName) {
	return GeneralLedgerController.getInstance().getFullNameAccount(pFullName);
    }

}
