/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jaccounting.views;

import jaccounting.JAccounting;
import jaccounting.controllers.GeneralLedgerController;
import jaccounting.models.Account;
import jaccounting.models.AccountTreeNode;
import jaccounting.models.GeneralLedger;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.Enumeration;
import java.util.Observable;
import java.util.Observer;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import org.jdesktop.application.ResourceMap;

/**
 *
 * @author bouba
 */
public class GeneralLedgerView extends JPanel implements Observer {

    private GeneralLedgerController controller;
    private GeneralLedger mModel;
    private AccountNamesView accountNamesView;
    private AccountMetaDatasView accountDescriptionsView;
    private AccountMetaDatasView accountBalancesView;
    private boolean inProcessingRowSelection;

    public GeneralLedgerView(GeneralLedgerController controller, GeneralLedger model) {
	this.controller = controller;
	this.mModel = model;
	inProcessingRowSelection = false;

	initComponents();
	this.mModel.addObserver(this);
    }

    private void initComponents() {
	ResourceMap vRmap = JAccounting.getApplication().getContext().getResourceMap(this.getClass());

	setLayout(new BorderLayout());

	// header bar
	JPanel vHeader = new JPanel();
	vHeader.setLayout(new GridLayout(1, 3));
	vHeader.setBorder(new EmptyBorder(0, 0, 10, 0));
	vHeader.add(new JLabel(vRmap.getString("headers.accountName")));
	vHeader.add(new JLabel(vRmap.getString("headers.accountDescription")));
	vHeader.add(new JLabel(vRmap.getString("headers.accountBalance")));

	// content
	JPanel vContent = new JPanel();
	// set as grid layout
	vContent.setLayout(new GridLayout(1, 3));
	// create columns
	// first columns names
	AccountNamesView vAccountNames = new AccountNamesView(buildAccountNamesTree());
	//AccountNamesView vAccountNames = new AccountNamesView(mModel.getRoot());

	// second column descriptions
	AccountMetaDatasView vAccountDescriptions = new AccountMetaDatasView(0, buildAccountDescriptionsList(), buildAccountDescriptionsList());

	// third column balances
	AccountMetaDatasView vAccountBalances = new AccountMetaDatasView(1, buildAccountBalancesList(), buildAccountBalancesList());

	vContent.add(vAccountNames);
	vContent.add(vAccountDescriptions);
	vContent.add(vAccountBalances);

	// wrap content in scroll pane
	JScrollPane vContentPane = new JScrollPane(vContent);
	vContentPane.setBorder(new EmptyBorder(0, 0, 0, 0));

	add(vHeader, BorderLayout.NORTH);
	add(vContentPane, BorderLayout.CENTER);
	/*add(new JPanel(), BorderLayout.EAST);
	add(new JPanel(), BorderLayout.WEST);
	add(new JPanel(), BorderLayout.SOUTH);*/

	accountNamesView = vAccountNames;
	accountDescriptionsView = vAccountDescriptions;
	accountBalancesView = vAccountBalances;

	accountNamesView.expandAll();
	accountNamesView.addDefaultListeners();
    }

    private TreeNode buildAccountNamesTree() {
	DefaultMutableTreeNode rRoot = new DefaultMutableTreeNode((String)mModel.getRoot().getUserObject());
	buildAccountNameNode(mModel.getRoot(), rRoot);
	return rRoot;
    }

    private ListModel buildAccountDescriptionsList() {
	Enumeration vEnum;
	Account vAcct;
	DefaultMutableTreeNode vNode;
	DefaultListModel rList = new DefaultListModel();

	rList.addElement(" ");
	vEnum = mModel.getRoot().preorderEnumeration();
	vEnum.nextElement(); // skip root
	while (vEnum.hasMoreElements()) {
	    vNode = (DefaultMutableTreeNode) vEnum.nextElement();
	    vAcct = (Account) vNode.getUserObject();
	    rList.addElement((vAcct.getDescription().length()>0?vAcct.getDescription():" "));
	}

	return rList;
    }

    private ListModel buildAccountBalancesList() {
	Enumeration vEnum;
	Account vAcct;
	DefaultMutableTreeNode vNode;
	DefaultListModel rList = new DefaultListModel();

	rList.addElement(" ");
	vEnum = mModel.getRoot().preorderEnumeration();
	vEnum.nextElement(); // skip root
	while (vEnum.hasMoreElements()) {
	    vNode = (DefaultMutableTreeNode) vEnum.nextElement();
	    vAcct = (Account) vNode.getUserObject();
	    rList.addElement(vAcct.getBalance());
	}

	return rList;
    }

    private void buildAccountNameNode(DefaultMutableTreeNode pAcctNode, DefaultMutableTreeNode pRoot) {
	DefaultMutableTreeNode vRoot = pRoot;
	Enumeration vChildren = pAcctNode.children();

	if (pAcctNode.getParent() != null) {
	    Account vAcct = (Account) pAcctNode.getUserObject();
	    vRoot = new DefaultMutableTreeNode(vAcct.getName());
	    pRoot.add(vRoot);
	    vAcct.addObserver(this);
	}

	while (vChildren.hasMoreElements()) {
	    buildAccountNameNode((AccountTreeNode) vChildren.nextElement(), vRoot);
	}

	return;
    }

    private void accountNamesSelectionChanged(TreePath pNew, TreePath pOld) {
	synchronized (this) {
	    if (inProcessingRowSelection) {
		return;
	    }
	    inProcessingRowSelection = true;
	}

	if (pNew != null) {
	    int vRow = accountNamesView.getRowForPath(pNew);
	    selectAccountMetaDatasRow(vRow, -1);
	    controller.accountSelected(vRow);
	} else if (pOld != null) {
	    selectAccountMetaDatasRow(-1, -1);
	    controller.noAccountSelected();
	}

	synchronized (this) {
	    inProcessingRowSelection = false;
	}
    }

    private void accountMetaDatasSelectionChanged(int pColIndex, int pNew) {
	synchronized (this) {
	    if (inProcessingRowSelection) {
		return;
	    }
	    inProcessingRowSelection = true;
	}

	int vNew = pNew;
	if (pNew != -1) {
	    if (pColIndex == 0) {
		vNew = accountDescriptionsView.getSelectedIndex();
	    } else if (pColIndex == 1) {
		vNew = accountBalancesView.getSelectedIndex();
	    }

	    selectAccountNamesRow(vNew);
	    selectAccountMetaDatasRow(vNew, pColIndex);
	    controller.accountSelected(vNew);
	}

	synchronized (this) {
	    inProcessingRowSelection = false;
	}
    }

    private void selectAccountMetaDatasRow(int pRow, int pExceptCol) {
	if (pExceptCol != 0) {
	    if (pRow != -1) {
		accountDescriptionsView.setSelectedIndex(pRow);
	    } else {
		accountDescriptionsView.clearSelection();
	    }
	}

	if (pExceptCol != 1) {
	    if (pRow != -1) {
		accountBalancesView.setSelectedIndex(pRow);
	    } else {
		accountBalancesView.clearSelection();
	    }
	}
    }

    private void selectAccountNamesRow(int pRow) {
	accountNamesView.setSelectionRow(pRow);
    }

    private void accountNamesNodeWillExpand(TreePath pPath) {
	int vCt = accountNamesView.getModel().getChildCount(pPath.getLastPathComponent());
	int vRow = accountNamesView.getRowForPath(pPath);

	for (int i = 1; i <= vCt; i++) {
	    accountDescriptionsView.fakeShowElementAt(vRow + i);
	    accountBalancesView.fakeShowElementAt(vRow + i);
	}

	if (accountNamesView.isSelectionEmpty()) {
	    selectAccountMetaDatasRow(-1, -1);
	    controller.noAccountSelected();
	}
    }

    private void accountNamesNodeWillCollapse(TreePath pPath) {
	int vCt = accountNamesView.getModel().getChildCount(pPath.getLastPathComponent());
	int vRow = accountNamesView.getRowForPath(pPath);

	for (int i = 1; i <= vCt; i++) {
	    accountDescriptionsView.fakeHideElementAt(vRow + i);
	    accountBalancesView.fakeHideElementAt(vRow + i);
	}

	if (accountNamesView.isSelectionEmpty()) {
	    selectAccountMetaDatasRow(-1, -1);
	    controller.noAccountSelected();
	}
    }

    public int getCurrentlySelectedRow() {
	int rRow = -1;
	int[] vSelRows = accountNamesView.getSelectionRows();

	if (vSelRows != null && vSelRows.length > 0) {
	    rRow = vSelRows[0];
	}

	return rRow;
    }

    public Account getCurrentlySelectedAccount() {
	// get currently selected row
	int vSelRow = getCurrentlySelectedRow();
	// get its account
	return mModel.getAccount(vSelRow);
    }

    public void selectAccountAtRow(int pRow) {
	if (pRow > 0) {
	    selectAccountNamesRow(pRow);
	}
    }

    public void update(Observable o, Object arg) {
	handleModelChanged();
    }

    private void handleModelChanged() {
	int vRow = getCurrentlySelectedRow();
	controller.noAccountSelected();
	removeAll();
	initComponents();
	selectAccountAtRow(vRow);
    }

    private class AccountNamesView extends JTree implements TreeSelectionListener, TreeWillExpandListener {

	public AccountNamesView(TreeNode pAccountNames) {
	    super(pAccountNames);
	    setShowsRootHandles(true);
	    //setCellRenderer(new AccountNamesViewCellRenderer());
	    setSelectionModel(new AccountNamesViewSelectionModel());
	    setRootVisible(true);
	}

	public void addDefaultListeners() {
	    addTreeSelectionListener(this);
	    addTreeWillExpandListener(this);
	}

	public void valueChanged(TreeSelectionEvent e) {
	    accountNamesSelectionChanged(e.getNewLeadSelectionPath(), e.getOldLeadSelectionPath());
	}

	public void expandAll() {
	    for (int i = 0; i < getRowCount(); i++) {
		expandRow(i);
	    }
	}

	public void treeWillExpand(TreeExpansionEvent event) throws ExpandVetoException {
	    accountNamesNodeWillExpand(event.getPath());
	}

	public void treeWillCollapse(TreeExpansionEvent event) throws ExpandVetoException {
	    accountNamesNodeWillCollapse(event.getPath());
	}

	private class AccountNamesViewSelectionModel extends DefaultTreeSelectionModel {

	    public AccountNamesViewSelectionModel() {
		super();
		setSelectionMode(SINGLE_TREE_SELECTION);
	    }
	}

	/*private class AccountNamesViewCellRenderer extends JPanel implements TreeCellRenderer {
	private JLabel nameField;

	private JLabel descriptionField;

	private JLabel balanceField;

	public AccountNamesViewCellRenderer() {
	nameField = new JLabel();
	descriptionField = new JLabel();
	balanceField = new JLabel();

	setLayout(new BorderLayout());
	add(nameField, BorderLayout.WEST);
	add(descriptionField, BorderLayout.CENTER);
	add(balanceField, BorderLayout.EAST);
	}

	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
	if (selected) {
	setBackground(Color.BLUE);
	} else {
	setBackground(Color.white);
	}
	String vName;
	String vDescription;
	String vBalance;

	DefaultMutableTreeNode vNode = (DefaultMutableTreeNode) value;
	try {
	Account vAcct = (Account) vNode.getUserObject();
	vName = vAcct.getName();
	vDescription = vAcct.getDescription();
	vBalance = ""+vAcct.getBalance();
	} catch (ClassCastException vEx) {
	try {
	String[] vHeaders = (String[]) vNode.getUserObject();
	vName = vHeaders[0];
	vDescription = vHeaders[1];
	vBalance = vHeaders[2];
	} catch (ClassCastException vEx2) {
	vName = vNode.getUserObject().toString();
	vDescription = vName;
	vBalance = vName;
	}
	}

	nameField.setText(vName);
	descriptionField.setText(vDescription);
	balanceField.setText(vBalance);

	return this;
	}

	}*/
    }

    private class AccountMetaDatasView extends JList implements ListSelectionListener {

	private int colIndex;
	private ListModel nonMutableModel;

	public AccountMetaDatasView(int pColIndex, ListModel pMetaDatas, ListModel pNonMutableModel) {
	    super(pMetaDatas);
	    addListSelectionListener(this);
	    setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	    //setCellRenderer(new AccountMetaDatasViewCellRenderer());
	    colIndex = pColIndex;
	    nonMutableModel = pNonMutableModel;
	}

	public void valueChanged(ListSelectionEvent e) {
	    accountMetaDatasSelectionChanged(colIndex, e.getLastIndex());
	}

	public void fakeHideElementAt(int pRow) {
	    DefaultListModel vData = (DefaultListModel) getModel();
	    vData.remove(pRow);
	}

	public void fakeShowElementAt(int pRow) {
	    DefaultListModel vData = (DefaultListModel) getModel();
	    vData.insertElementAt(nonMutableModel.getElementAt(pRow), pRow);
	}

	/*private class AccountMetaDatasViewCellRenderer extends DefaultListCellRenderer {
	public AccountMetaDatasViewCellRenderer() {
	//setVisible(false);
	}
	}*/
    }
}