 /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
 *
 * @author bouba
 */
public class JournalView extends JPanel implements Observer {

    private JournalController controller;
    private Journal mModel;
    private TransactionsView transactions;


    public JournalView(JournalController controller, Journal model) {
	this.controller = controller;
	this.mModel = model;

	initComponents();
	this.mModel.addObserver(this);
    }

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
	transactions = new TransactionsView(vData, vColNames);

	JScrollPane vPane = new JScrollPane(transactions);
	add(vPane);
    }

    private Object[][] buildGridData() {
	Object[][] rData = new Object[mModel.getTransactions().size()][5];
	ResourceMap vRmap = JAccounting.getApplication().getContext().getResourceMap(this.getClass());
	int vIndex = 0;
	Transaction vTrans;
	
	ListIterator vIt = mModel.getTransactions().listIterator();
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
	int vRow = transactions.getSelectedRow();
	if (vRow >= 0) {
	    controller.transactionSelected(vRow);
	} else {
	    controller.noTransactionSelected();
	}
    }

    public Transaction getCurrentlySelectedTransaction() {
	int vIndex = getCurrentlySelectedRow();
	if (vIndex == -1) return null;
	return mModel.getTransaction(vIndex);
    }

    public int getCurrentlySelectedRow() {
	return transactions.getSelectedRow();
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
