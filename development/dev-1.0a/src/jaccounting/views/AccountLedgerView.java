/*
 * AccountLedgerView.java		1.0.0		09/2009
 * This file contains the account ledger interface class of the JAccounting application.
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

package jaccounting.views;

import jaccounting.JAccounting;
import jaccounting.controllers.AccountLedgerController;
import jaccounting.models.Account;
import jaccounting.models.Transaction;
import jaccounting.models.TransactionEntry;
import java.awt.GridLayout;
import java.util.ListIterator;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import org.jdesktop.application.ResourceMap;

/**
 * AccountLedgerView is the gui class for the account ledger interface. An
 * AccountLedgerView object displays all transaction entries associated with
 * an account.
 *
 * @author	    Boubacar Diallo
 * @version	    1.0.0
 * @see		    jaccounting.controllers.AccountLedgerController
 * @see		    jaccounting.models.Account
 * @since	    1.0.0
 */
public class AccountLedgerView extends JPanel implements Observer {

    private AccountLedgerController controller;

    /** the Account being represented by this view */
    private Account appModel;

    /** the gui object listing the entries in an Account */
    private EntriesView entriesView;


    /**
     * Sole constructor. This constructor initializes this view's controller and
     * model, creates its gui components and observes its model object.
     *
     * @param controller	    this view's controller; an AccountLedgerController
     * @param appModel		    this view's model; an Account
     * @see			    jaccounting.controllers.AccountLedgerController
     * @see			    jaccounting.models.Account
     * @since			    1.0.0
     */
    public AccountLedgerView(AccountLedgerController controller, Account appModel) {
	this.controller = controller;
	this.appModel = appModel;

	initComponents();
	this.appModel.addObserver(this);
    }


    /**
     * Handles change notifications from this view's model. This method simply
     * re-initializes this view and redraws it.
     *
     * @param o			the object being observed by this view
     * @param arg		additional information about the change
     * @since			1.0.0
     */
    public void update(Observable o, Object arg) {
	handleModelChanged();
    }

    private void handleModelChanged() {
	removeAll();
	initComponents();
    }

    private void initComponents() {
	setLayout(new GridLayout(1,1));

	JScrollPane vPane = new JScrollPane(buildEntriesView());
	add(vPane);
    }

    private EntriesView buildEntriesView() {
	Object[][] vData = new Object[appModel.getEntries().size()][6];
	ResourceMap vRmap = JAccounting.getApplication().getContext().getResourceMap(this.getClass());
	int vIndex = 0;
	TransactionEntry vEntry;
	Transaction vTrans;
	Account vAcct;
	ListIterator vIt = appModel.getEntries().listIterator();
	String[] vColNames = { vRmap.getString("columnNames.date"), vRmap.getString("columnNames.debitor"),
			       vRmap.getString("columnNames.creditor"),
			       vRmap.getString("columnNames.refNo"), vRmap.getString("columnNames.amount"),
			       vRmap.getString("columnNames.balance")};

	while (vIt.hasNext()) {
	    vEntry = (TransactionEntry) vIt.next();
	    vTrans = vEntry.getTransaction();
	    vAcct = vEntry.getTransferAccount();

	    vData[vIndex][0] = vRmap.getString("dateText", vTrans.getDate());
	    if (vEntry.getType() == TransactionEntry.Type.DEBIT) {
		vData[vIndex][1] = vRmap.getString("debitorText", vAcct.getName(),
				    vTrans.getMemo());
	    }
	    else if (vEntry.getType() == TransactionEntry.Type.CREDIT) {
		vData[vIndex][2] = vRmap.getString("creditorText", vAcct.getName(),
				    vTrans.getMemo());
	    }
	    vData[vIndex][3] = vRmap.getString("refNoText", vTrans.getRefNo());
	    vData[vIndex][4] = vRmap.getString("amountText", vTrans.getAmount());
	    vData[vIndex][5] = vRmap.getString("balanceText", vEntry.getAccountBalance());
	    
	    vIndex++;
	}

	entriesView = new EntriesView(vData, vColNames);
	return entriesView;
    }


    private class EntriesView extends JTable {

	private EntriesView(Object[][] pData, String[] pColNames) {
	    super(new EntriesViewTableModel(pData, pColNames));
	    customize();
	}

	
	private void customize() {
	    setColumnSelectionAllowed(false);
	    setDragEnabled(false);
	    setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	    setCellSelectionEnabled(false);
	    setRowSelectionAllowed(true);
	    setRowHeight(getRowHeight()*2);
	    //setAutoResizeMode(AUTO_RESIZE_OFF);

	    TableColumnModel vColModel = getColumnModel();
	    int vWidth = vColModel.getColumn(0).getPreferredWidth();
	    vColModel.getColumn(0).setPreferredWidth(vWidth*3);
	    vColModel.getColumn(1).setPreferredWidth(vWidth*5);
	    vColModel.getColumn(2).setPreferredWidth(vWidth*5);
	    vColModel.getColumn(3).setPreferredWidth(vWidth);
	    vColModel.getColumn(4).setPreferredWidth(vWidth*2);
	    vColModel.getColumn(5).setPreferredWidth(vWidth*2);
	}

    }

    private class EntriesViewTableModel extends DefaultTableModel {

	EntriesViewTableModel(Object[][] pData, String[] pColNames) {
	    super(pData, pColNames);
	}

	@Override
	public boolean isCellEditable(int row, int col){ return false; }
    }
}
