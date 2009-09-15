/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
 *
 * @author bouba
 */
public class AccountLedgerView extends JPanel implements Observer {

    private AccountLedgerController controller;
    private Account mModel;
    private EntriesView entries;

    public AccountLedgerView(AccountLedgerController controller, Account mModel) {
	this.controller = controller;
	this.mModel = mModel;

	initComponents();
	this.mModel.addObserver(this);
    }

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

    private JTable buildEntriesView() {
	Object[][] vData = new Object[mModel.getEntries().size()][6];
	ResourceMap vRmap = JAccounting.getApplication().getContext().getResourceMap(this.getClass());
	int vIndex = 0;
	TransactionEntry vEntry;
	Transaction vTrans;
	Account vAcct;

	String[] vColNames = { vRmap.getString("columnNames.date"), vRmap.getString("columnNames.debitor"),
			       vRmap.getString("columnNames.creditor"),
			       vRmap.getString("columnNames.refNo"), vRmap.getString("columnNames.amount"),
			       vRmap.getString("columnNames.balance")};

	ListIterator vIt = mModel.getEntries().listIterator();
	while (vIt.hasNext()) {
	    vEntry = (TransactionEntry) vIt.next();
	    vTrans = vEntry.getTransaction();
	    vAcct = vEntry.getTransferAccount();

	    vData[vIndex][0] = vRmap.getString("dateText", vTrans.getDate());
	    if (vEntry.getType() == TransactionEntry.Type.DEBIT) {
		vData[vIndex][1] = vRmap.getString("debitorText", vAcct.getName(),
				    vTrans.getMemo());
	    } else if (vEntry.getType() == TransactionEntry.Type.CREDIT) {
		vData[vIndex][2] = vRmap.getString("creditorText", vAcct.getName(),
				    vTrans.getMemo());
	    }
	    vData[vIndex][3] = vRmap.getString("refNoText", vTrans.getRefNo());
	    vData[vIndex][4] = vRmap.getString("amountText", vTrans.getAmount());
	    vData[vIndex][5] = vRmap.getString("balanceText", vEntry.getAccountBalance());
	    
	    vIndex++;
	}

	entries = new EntriesView(vData, vColNames);
	return entries;
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
