/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jaccounting.controllers;

import jaccounting.JAccounting;
import jaccounting.exceptions.ErrorCode;
import jaccounting.exceptions.GenericException;
import jaccounting.models.Account;
import jaccounting.models.GeneralLedger;
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
 *
 * @author bouba
 */
public class GeneralLedgerController extends BaseController {

    private boolean openAccountLedgerEnabled;
    private boolean openNewAccountBoxEnabled;
    private boolean openEditAccountBoxEnabled;
    private boolean deleteAccountEnabled;

    private GeneralLedgerController() {
	super();
	enableAccountActions(false);
    }

    private static class GeneralLedgerControllerHolder {
	private static final GeneralLedgerController INSTANCE = new GeneralLedgerController();
    }

    public static GeneralLedgerController getInstance() {
	return GeneralLedgerControllerHolder.INSTANCE;
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

    protected GeneralLedger getModel() {
	return JAccounting.getApplication().getModelsMngr().getData().getGeneralLedger();
    }

    protected GeneralLedgerView getView() {
	ResourceMap vRmap = JAccounting.getApplication().getContext().getResourceMap(GeneralLedgerView.class);
	JTabbedPane vTabsCont = JAccounting.getApplication().getMainView().getTabsContainer();
	int vIndex = vTabsCont.indexOfTab(vRmap.getString("title"));

	if (vIndex != -1) {
	    return (GeneralLedgerView) vTabsCont.getComponentAt(vIndex);
	}

	return null;
    }

    public void openGeneralLedger() {
	ResourceMap vRmap = JAccounting.getApplication().getContext().getResourceMap(GeneralLedgerView.class);
	GeneralLedger vModel = getModel();
	
	JTabbedPane vTabsCont = JAccounting.getApplication().getMainView().getTabsContainer();
	int vIndex = vTabsCont.indexOfTab(vRmap.getString("title"));

	// if general ledger view does not exists create it
	if (vIndex == -1) {
	    vTabsCont.insertTab(vRmap.getString("title"), null,
		    new GeneralLedgerView(this, vModel),
		    vRmap.getString("titleTip"), 0);
	    vTabsCont.setSelectedIndex(0);
	// show general ledger view
	} else {
	    vTabsCont.setSelectedIndex(vIndex);
	}
    }

    protected ModifyAccountBox getOrCreateModifyAccountBox() {
	ModifyAccountBox rBox = JAccounting.getApplication().getMainView().getModifyAccountBox();

	if (rBox == null) {
	    rBox = new ModifyAccountBox(JAccounting.getApplication().getMainFrame(), this);
	    rBox.setLocationRelativeTo(JAccounting.getApplication().getMainFrame());
	    JAccounting.getApplication().getMainView().setModifyAccountBox(rBox);
	}

	return rBox;
    }

    @Action(enabledProperty = "openNewAccountBoxEnabled")
    public void openNewAccountBox() {
	GeneralLedgerView vView = getView();
	// get currently selected account
	Account vAcct = vView.getCurrentlySelectedAccount();
	if (vAcct == null) {
	    return;
	}

	ModifyAccountBox vBox = getOrCreateModifyAccountBox();

	vBox.setIsNew(true);
	try {
	    vBox.setModel(Account.createAccount(vAcct.getType()));
	    vBox.initFormFields();

	    JAccounting.getApplication().show(vBox);
	} catch (GenericException ex) {
	    Logger.getLogger(GeneralLedgerController.class.getName()).log(Level.SEVERE, null, ex);
	}
    }

    @Action(enabledProperty = "openEditAccountBoxEnabled")
    public void openEditAccountBox() {
	ModifyAccountBox vBox = getOrCreateModifyAccountBox();
	GeneralLedgerView vView = getView();

	// get currently selected account
	Account vAcct = vView.getCurrentlySelectedAccount();
	if (vAcct == null) {
	    return;
	}

	vBox.setIsNew(false);
	vBox.setModel(vAcct);
	vBox.initFormFields();

	JAccounting.getApplication().show(vBox);
    }

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
	if (vRow == -1) return;
	GeneralLedger vModel = getModel();
	try {
	    // remove account node
	    vModel.removeAccount(vRow);
	} catch (GenericException ex) {
	    Logger.getLogger(GeneralLedgerController.class.getName()).log(Level.SEVERE, null, ex);
	}
    }

    @Action(enabledProperty = "openAccountLedgerEnabled")
    public void openAccountLedger() {
	GeneralLedgerView vView = getView();
	// get currently selected account
	Account vAcct = vView.getCurrentlySelectedAccount();
	if (vAcct == null) return;
	// open its ledger through AccountLedgerController
	AccountLedgerController.getInstance().openAccountLedger(vAcct);
    }

    @Action
    public void modifyAccount() {
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
	    } catch (NumberFormatException vEx) {
		vNumber = -1;
		if (vBox.getNumberTextField().getText().length() > 0) {
		    vErrors.put("number", ErrorCode.INVALID_NUMBER_FORMAT);
		}
	    }

	    if (vErrors.isEmpty()) {
		// attempt to update account from modify box field values
		vErrors = vAcct.update(vNumber, vBox.getNameTextField().getText(),
			vBox.getDescriptionTextField().getText());
	    }

	    if (vErrors.isEmpty()) {
		if (vIsNew) {
		    addAccount(vAcct);
		}
		vBox.dispose();
	    } else {
		vBox.displayErrors(vErrors);
	    }
	}
    }

    protected void addAccount(Account pAccount) {
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

    public void accountSelected(int pRow) {
	if (pRow == 0) {
	    noAccountSelected();
	} else {
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

    public void noAccountSelected() {
	// disable actions
	enableAccountActions(false);
    }

    protected void enableAccountActions(boolean pVal) {
	openNewAccountBoxEnabled = pVal;
	support.firePropertyChange("openNewAccountBoxEnabled", !pVal, pVal);
	enableEditAccount(pVal);
	enableOpenAccountLedger(pVal);
	enableDeleteAccount(pVal);
    }

    protected void enableEditAccount(boolean pVal) {
	openEditAccountBoxEnabled = pVal;
	support.firePropertyChange("openEditAccountBoxEnabled", !pVal, pVal);
    }

    protected void enableDeleteAccount(boolean pVal) {
	deleteAccountEnabled = pVal;
	support.firePropertyChange("deleteAccountEnabled", !pVal, pVal);
    }

    protected void enableOpenAccountLedger(boolean pVal) {
	openAccountLedgerEnabled = pVal;
	support.firePropertyChange("openAccountLedgerEnabled", !pVal, pVal);
    }

    public String[] getTransactionnableAccountFullNames() {
	return getModel().getTransactionnableAccountFullNames();
    }

    public String getAccountFullName(Account pAcct) {
	return getModel().getAccountFullName(pAcct);
    }

    public Account getFullNameAccount(String pFullName) {
	return getModel().getAccount(pFullName);
    }

}
