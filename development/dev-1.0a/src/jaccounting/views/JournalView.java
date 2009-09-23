/*
 * JournalView.java		1.0.0		09/2009
 * This file contains the journal interface class of the JAccounting application.
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
import jaccounting.controllers.JournalController;
import jaccounting.models.Journal;
import jaccounting.models.Transaction;
import java.awt.GridLayout;
import java.util.ListIterator;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import org.jdesktop.application.ResourceMap;

/**
 * JournalView is the gui class for the journal interface. A JournalView object
 * lists all the transactionsView in the Journal model. It notifies the JournalController
 * of selection changes from the user.
 *
 * @author	    Boubacar Diallo
 * @version	    1.0.0
 * @see		    jaccounting.controllers.JournalController
 * @see		    jaccounting.models.Journal
 * @since	    1.0.0
 */
public class JournalView extends JPanel implements Observer {

    private JournalController controller;

    private Journal appModel;

    private TransactionsView transactionsView;


    /**
     * Sole constructor. This constructor initializes this view's controller and
     * model, creates its gui components and observes its model object.
     *
     * @param controller	    the controller; a JournalController
     * @param model		    the model; a Journal
     * @see			    jaccounting.controllers.JournalController
     * @see			    jaccounting.models.Journal
     * @since			    1.0.0
     */
    public JournalView(JournalController controller, Journal model) {
	this.controller = controller;
	this.appModel = model;

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
	transactionsSelectionChanged();
    }

    private void initComponents() {
	ResourceMap vRmap = JAccounting.getApplication().getContext().getResourceMap(this.getClass());

	setLayout(new GridLayout(1,1));
	
	String[] vColNames = { vRmap.getString("columnNames.date"), vRmap.getString("columnNames.refNo"),
			       vRmap.getString("columnNames.particulars"), vRmap.getString("columnNames.debitAmount"),
			       vRmap.getString("columnNames.creditAmount") };
	Object[][] vData = buildGridData();
	transactionsView = new TransactionsView(vData, vColNames);

	JScrollPane vPane = new JScrollPane(transactionsView);
	add(vPane);
    }

    private Object[][] buildGridData() {
	Object[][] rData = new Object[appModel.getTransactions().size()][5];
	ResourceMap vRmap = JAccounting.getApplication().getContext().getResourceMap(this.getClass());
	int vIndex = 0;
	Transaction vTrans;
	
	ListIterator vIt = appModel.getTransactions().listIterator();
	while (vIt.hasNext()) {
	    vTrans = (Transaction) vIt.next();
	   
	    rData[vIndex][0] = vRmap.getString("dateText", vTrans.getDate());
	    rData[vIndex][1] = vRmap.getString("refNoText", vTrans.getRefNo());
	    rData[vIndex][2] = vRmap.getString("particularsText", vTrans.getDebitAccount().getName(),
				vTrans.getCreditAccount().getName(), vTrans.getMemo());
	    rData[vIndex][3] = vRmap.getString("debitAmountText", vTrans.getAmount());
	    rData[vIndex][4] = vRmap.getString("creditAmountText", vTrans.getAmount());
	    
	    vTrans.addObserver(this);
	    vIndex++;
	}
	
	return rData;
    }

    private void transactionsSelectionChanged() {
	int vRow = transactionsView.getSelectedRow();
	if (vRow >= 0) {
	    controller.transactionSelected(vRow);
	} else {
	    controller.noTransactionSelected();
	}
    }

    /**
     * Gets the currently selected Transaction object. This method effectively gets
     * the currently selected row and asks the Journal for the corresponding
     * Transaction object.
     *
     * @return		    the currently selected Transaction or null
     * @see		    #getCurrentlySelectedRow()
     * @see		    jaccounting.models.Journal#getTransaction(int)
     * @since		    1.0.0
     */
    public Transaction getCurrentlySelectedTransaction() {
	int vIndex = getCurrentlySelectedRow();
	return (vIndex == -1) ? null : appModel.getTransaction(vIndex);
    }

    /**
     * Gets the currently selected row number.
     *
     * @return		    the currently selected row or -1
     * @since		    1.0.0
     */
    public int getCurrentlySelectedRow() {
	return transactionsView.getSelectedRow();
    }

    private class TransactionsView extends JTable {

	private TransactionsView(Object[][] pData, String[] pColNames) {
	    super(new TransactionsViewTableModel(pData, pColNames));
	    customize();
	}

	private void customize() {
	    setColumnSelectionAllowed(false);
	    setDragEnabled(false);
	    setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	    setCellSelectionEnabled(false);
	    setRowSelectionAllowed(true);
	    setRowHeight(getRowHeight()*3);
	    //setAutoResizeMode(AUTO_RESIZE_OFF);

	    TableColumnModel vColModel = getColumnModel();
	    int vWidth = vColModel.getColumn(0).getPreferredWidth();
	    vColModel.getColumn(0).setPreferredWidth(vWidth*3);
	    vColModel.getColumn(1).setPreferredWidth(vWidth);
	    vColModel.getColumn(2).setPreferredWidth(vWidth*11);
	    vColModel.getColumn(3).setPreferredWidth(vWidth*2);
	    vColModel.getColumn(4).setPreferredWidth(vWidth*2);
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
	    super.valueChanged(e);
	    if (!e.getValueIsAdjusting()) {
		transactionsSelectionChanged();
	    }
	}

    }

    private class TransactionsViewTableModel extends DefaultTableModel {

	TransactionsViewTableModel(Object[][] pData, String[] pColNames) {
	    super(pData, pColNames);
	}

	@Override
	public boolean isCellEditable(int row, int col){ return false; }
    }

}
