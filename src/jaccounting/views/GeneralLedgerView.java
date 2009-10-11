/*
 * GeneralLedgerView.java		1.0.0		09/2009
 * This file contains the general ledger interface class of the JAccounting application.
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
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
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
 * GeneralLedgerView is the gui class for the general ledger interface. A
 * GeneralLedgerView object lists all the accounts in the GeneralLedger model. It
 * notifies the GeneralLedgerController of selection changes from the user.
 *
 * @author	    Boubacar Diallo
 * @version	    1.0.0
 * @see		    jaccounting.controllers.GeneralLedgerController
 * @see		    jaccounting.models.GeneralLedger
 * @since	    1.0.0
 */
public class GeneralLedgerView extends JPanel implements Observer {

    /**
     * This class attempts to fake TreeTable gui by using a JTee and JLists. When
     * a row is selected in either JTree or JList, the exact row is selected in
     * the remaining other components. Since there is a potential for recursing, i.e
     * selection of a row in JTree leads to selection of row in JList which leads
     * to selection of row in JTree, a synchronized flag inProcessingRowSelection
     * is used to avoid repeating selection in a given component.
     */

    /** this view's controller; a GeneralLedgerController */
    private GeneralLedgerController controller;

    /** this view's model; a GeneralLedger */
    private GeneralLedger appModel;

    /** the gui subcomponent for listing account names */
    private AccountNamesView accountNamesView;

    /** the gui subcomponent for listing account descriptions */
    private AccountMetaDatasView accountDescriptionsView;

    /** the gui subcomponent for listing account balances */
    private AccountMetaDatasView accountBalancesView;

    /** special flag used to avoid recursive selection change events handling */
    private boolean inProcessingRowSelection;


    /**
     * Sole constructor. This constructor initializes this view's controller and
     * model, creates its gui components and observes its model object.
     *
     * @param controller	    the controller; a GeneralLedgerController
     * @param model		    the model; a GeneralLedger
     * @see			    jaccounting.controllers.GeneralLedgerController
     * @see			    jaccounting.models.GeneralLedger
     * @since			    1.0.0
     */
    public GeneralLedgerView(GeneralLedgerController controller, GeneralLedger model) {
	this.controller = controller;
	this.appModel = model;

	initComponents();
	this.appModel.addObserver(this);
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
	vContent.setLayout(new GridLayout(1, 3));
	// create columns
	// first names column
	AccountNamesView vAccountNames = new AccountNamesView(buildAccountNamesTree());
	// second descriptions column
	AccountMetaDatasView vAccountDescriptions = new AccountMetaDatasView(0,
		buildAccountDescriptionsList(), buildAccountDescriptionsList());
	// third balances column
	AccountMetaDatasView vAccountBalances = new AccountMetaDatasView(1,
		buildAccountBalancesList(), buildAccountBalancesList());

	vContent.add(vAccountNames);
	vContent.add(vAccountDescriptions);
	vContent.add(vAccountBalances);

	// wrap content in scroll pane
	JScrollPane vContentPane = new JScrollPane(vContent);
	vContentPane.setBorder(new EmptyBorder(0, 0, 0, 0));

	add(vHeader, BorderLayout.NORTH);
	add(vContentPane, BorderLayout.CENTER);

	accountNamesView = vAccountNames;
	accountDescriptionsView = vAccountDescriptions;
	accountBalancesView = vAccountBalances;

	accountNamesView.expandAll();
	accountNamesView.addDefaultListeners();
    }

    private TreeNode buildAccountNamesTree() {
	DefaultMutableTreeNode rRoot = new DefaultMutableTreeNode((String)appModel.getRoot().getUserObject());
	buildAccountNameNode(appModel.getRoot(), rRoot);
	return rRoot;
    }

    private DefaultListModel buildAccountDescriptionsList() {
	Account vAcct;
	DefaultMutableTreeNode vNode;
	DefaultListModel rList = new DefaultListModel();
	Enumeration vEnum = appModel.getRoot().preorderEnumeration();

	rList.addElement(" ");		// root equivalent
	vEnum.nextElement();		// skip root
>>>>>>> .r71
	while (vEnum.hasMoreElements()) {
	    vNode = (DefaultMutableTreeNode) vEnum.nextElement();
	    vAcct = (Account) vNode.getUserObject();
	    rList.addElement(((vAcct.getDescription().length()) > 0
				? vAcct.getDescription()
				: " "));
	}

	return rList;
    }

    private DefaultListModel buildAccountBalancesList() {
	Account vAcct;
	DefaultMutableTreeNode vNode;
	DefaultListModel rList = new DefaultListModel();
	Enumeration vEnum = appModel.getRoot().preorderEnumeration();

	rList.addElement(" ");		// root equivalent
	vEnum.nextElement();		// skip root
>>>>>>> .r71
	while (vEnum.hasMoreElements()) {
	    vNode = (DefaultMutableTreeNode) vEnum.nextElement();
	    vAcct = (Account) vNode.getUserObject();
	    rList.addElement(String.valueOf(vAcct.getBalance()));
	}

	return rList;
    }

    private void buildAccountNameNode(DefaultMutableTreeNode pAcctNode, DefaultMutableTreeNode pRoot) {
	DefaultMutableTreeNode vRoot = pRoot;
	Enumeration vChildren = pAcctNode.children();

	/** Avoid root node since it is not an Account object */
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
	/**
	 * If this selection change in the names tree is due a change in the
	 * selection in the descriptions or balances list, we exit to avoid
	 * being caught in a recursion.
	 */
	synchronized (this) {
	    if (inProcessingRowSelection) {
		return;
	    }
	    inProcessingRowSelection = true;
	}

	if (pNew != null) {	// case when a new names tree node was selected
	    /**
	     * We get the absolute row because the names tree's rows do not match the
	     * rows in the GeneralLedger tree when some of its nodes are collapsed.
	     */
	    int vRow = accountNamesView.getAbsoluteRowForPath(pNew);
	    /** select the matching rows in the descriptions list and balances list */
	    selectAccountMetaDatasRow(vRow, -1);
	    /** advise the controller of the newly selected account */
	    controller.accountSelected(vRow);
	} 
	else if (pOld != null) {    // case when no names tree node was selected
	    /** deselect selected rows in the descriptions and balances lists */
	    selectAccountMetaDatasRow(-1, -1);
	    /* advise the controller */
	    controller.noAccountSelected();
	}

	synchronized (this) {
	    inProcessingRowSelection = false;
	}
    }

    private void accountMetaDatasSelectionChanged(int pColIndex, int pNew) {
	/**
	 * pColIndex = 0 => descriptions list column
	 * pColIndex = 1 => balances list column
	 */

	/**
	 * If this selection change in a balances or descriptions list is due a
	 * change in the selection in another gui subcomponent, we exit to avoid
	 * being caught in a recursion.
	 */
	synchronized (this) {
	    if (inProcessingRowSelection) {
		return;
	    }
	    inProcessingRowSelection = true;
	}

	int vNew = pNew;
	if (pNew != -1) {
	    synchronized (this) {
		/** for safety, get the selected index from the proper object */
		if (pColIndex == 0) {
		    vNew = accountDescriptionsView.getSelectedIndex();
		} else if (pColIndex == 1) {
		    vNew = accountBalancesView.getSelectedIndex();
		}
>>>>>>> .r71

	    /** selects the names tree at the selected index */
	    selectAccountNamesRow(vNew);
	    /**
	     * selects other list columns except the originator column of this
	     * selection change event
	     */
	    selectAccountMetaDatasRow(vNew, pColIndex);
	    /** notify our beloved controller */
	    controller.accountSelected(vNew);
	}

	synchronized (this) {
	    inProcessingRowSelection = false;
	}
    }

    private synchronized void selectAccountMetaDatasRow(int pRow, int pExceptCol) {
	/**
	 * Select the provided index in all list columns except pExceptCol
	 * pColIndex = 0 => descriptions list column
	 * pColIndex = 1 => balances list column
	 * pColIndex = -1 => no list column i.e. select in all columns.
	 */

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

    private synchronized void selectAccountNamesRow(int pRow) {
	accountNamesView.setSelectionRow(pRow);
    }

    private void accountNamesNodeExpanded(TreePath pPath) {
	/**
	 * AFTER a names tree node has been fully expanded, we show the
	 * corresponding rows in the descriptions and balances lists
	 */
	updateVisibleMetaDatasRow(pPath);

	/** just in case the node expansion resulted in a selection clearance that we did not catch */
	if (accountNamesView.isSelectionEmpty()) {
	    selectAccountMetaDatasRow(-1, -1);
	    controller.noAccountSelected();
	}
    }

    private void accountNamesNodeWillCollapse(TreePath pPath) {
	/**
	 * BEFORE a names tree node has been fully collapsed, we hide the
	 * corresponding rows in the descriptions and balances lists
	 */
	updateInVisibleMetaDatasRow(pPath);
    }

    private void accountNamesNodeCollapsed(TreePath pPath) {
	/**
	 * AFTER a names tree node has been fully collapsed, we do not do much
	 * except check to see if the node collapse resulted in a selection
	 * clearance that we did not catch
	 */
>>>>>>> .r71
	if (accountNamesView.isSelectionEmpty()) {
	    selectAccountMetaDatasRow(-1, -1);
	    controller.noAccountSelected();
	}
    }

    private void updateInVisibleMetaDatasRow(TreePath pPath) {
	int vRow = accountNamesView.getRowForPath(pPath)+1;
	TreePath vPath;
	int vRowToHide = vRow;

	/**
	 * Hide the descriptions and balances lists rows of the collapsed
	 * names tree node's children.
	 */
	while ((vPath = accountNamesView.getPathForRow(vRow)) !=  null
		&& pPath.isDescendant(vPath)) {
	    accountDescriptionsView.fakeHideElementAt(vRowToHide);
	    accountBalancesView.fakeHideElementAt(vRowToHide);
	    vRow++;
	}
    }

    private void updateVisibleMetaDatasRow(TreePath pPath) {
	int vRow = accountNamesView.getRowForPath(pPath)+1;
	TreePath vPath;
	int vRowToShow;

	/**
	 * Show the descriptions and balances lists rows of the expanded
	 * names tree node's children.
	 */
	while ((vPath = accountNamesView.getPathForRow(vRow)) !=  null
		&& pPath.isDescendant(vPath)) {
	    vRowToShow = accountNamesView.getAbsoluteRowForPath(vPath);
	    accountDescriptionsView.fakeShowElementAt(vRow, vRowToShow);
	    accountBalancesView.fakeShowElementAt(vRow, vRowToShow);
	    vRow++;
	}
    }

    /**
     * Gets the currently selected row number.
     *
     * @return		    the currently selected row or -1
     * @since		    1.0.0
     */
    public synchronized int getCurrentlySelectedRow() {
	int rRow = -1;
	TreePath vSelPath = accountNamesView.getSelectionPath();

	/**
	 * We need the get the absolute row of the path in the GeneralLedger model
	 * to avoid confusing our controller of what account is selected.
	 */
	if (vSelPath != null) {
	    rRow = accountNamesView.getAbsoluteRowForPath(vSelPath);
	}

	return rRow;
    }

    /**
     * Gets the currently selected Account object. This method effectively gets
     * the currently selected row and asks the GeneralLedger for the corresponding
     * Account object.
     *
     * @return		    the currently selected Account
     * @see		    #getCurrentlySelectedRow()
     * @see		    jaccounting.models.GeneralLedger#getAccount(int)
     * @since		    1.0.0
     */
    public Account getCurrentlySelectedAccount() {
	int vSelRow = getCurrentlySelectedRow();
	return appModel.getAccount(vSelRow);
    }

    /**
     * Selects a row number.
     *
     * @since		    1.0.0
     */
    public void selectAccountAtRow(int pRow) {
	if (pRow > 0) {
	    selectAccountNamesRow(pRow);
	}
    }

    /**
     * Handles change notifications from this view's model. This method simply
     * re-initializes this view and redraws it before selecting the previously
     * selected row.
     *
     * @param o			the object being observed by this view
     * @param arg		additional information about the change
     * @since			1.0.0
     */
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

    private class AccountNamesView extends JTree implements TreeSelectionListener,
	    TreeExpansionListener, TreeWillExpandListener {

	private AccountNamesView(TreeNode pAccountNames) {
	    super(pAccountNames);
	    setShowsRootHandles(true);
	    setSelectionModel(new AccountNamesViewSelectionModel());
	    setRootVisible(true);
	}

	private void addDefaultListeners() {
	    addTreeSelectionListener(this);
	    addTreeExpansionListener(this);
	    addTreeWillExpandListener(this);
	}

	public void valueChanged(TreeSelectionEvent e) {
	    accountNamesSelectionChanged(e.getNewLeadSelectionPath(), e.getOldLeadSelectionPath());
	}

	private void expandAll() {
	    for (int i = 0; i < getRowCount(); i++) {
		expandRow(i);
	    }
	}

	private int getAbsoluteRowForPath(TreePath path) {
	    int rRow = -1;
	    int vInd;
	    String vPathName = path.toString();

	    /**
	     * Transform the path's string value to our usual account full
	     * name seperated by a dot and ask the GeneralLedger for the matching
	     * account's row.
	     */
	    vPathName = vPathName.replaceAll("^\\[", "");
	    vPathName = vPathName.replaceAll("\\]$", "");
	    vPathName = vPathName.replaceAll(",\\s{1}", ".");
	    vInd = vPathName.indexOf('.');
	    if (vInd > 0) {
		vPathName = vPathName.substring(vInd+1);
		if (vPathName.length() > 0) {
		    rRow = appModel.getRowOfAccount(appModel.getAccount(vPathName));
		}
	    }
	    else rRow = 0;

	    return rRow;
	}

	public void treeExpanded(TreeExpansionEvent event) {
	    accountNamesNodeExpanded(event.getPath());
	}

	public void treeCollapsed(TreeExpansionEvent event) {
	    accountNamesNodeCollapsed(event.getPath());
	}

	public void treeWillExpand(TreeExpansionEvent event) throws ExpandVetoException {
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

    }

    private class AccountMetaDatasView extends JList implements ListSelectionListener {

	/**
	 * This class uses a little trick to introduce show and hide functionality
	 * to the JList. Ordirnally, to hide an element from a JList the element
	 * needs to be deleted from the model and therefore is lost. To alleviate
	 * it, this class introduces a clone of its TreeModel that contains all
	 * the elements and never gets mutated. Elements are deleted from the usual
	 * TreeModel to implement the hide; elements are taken from the immutable
	 * TreeModel and inserted into the usual TreeModel.
	 */

	/**
	 * this meta data column's index;
	 * 0 => descriptions list column,
	 * 1 => balances list column
	 */
	private int colIndex;

	private DefaultListModel nonMutableModel;
	String vName;
	String vDescription;
	String vBalance;
=======
	private JLabel descriptionField;
>>>>>>> .r71

>>>>>>> .r71

	private AccountMetaDatasView(int pColIndex, DefaultListModel pMetaDatas, DefaultListModel pNonMutableModel) {
>>>>>>> .r71
	    super(pMetaDatas);
	    addListSelectionListener(this);
	    setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	    colIndex = pColIndex;
	    nonMutableModel = pNonMutableModel;
	}

	public void valueChanged(ListSelectionEvent e) {
	    accountMetaDatasSelectionChanged(colIndex, e.getLastIndex());
	}

	private synchronized void fakeHideElementAt(int pRow) {
	    DefaultListModel vData = (DefaultListModel) getModel();
	    vData.remove(pRow);
	}

	private synchronized void fakeShowElementAt(int pRow, int pRealPos) {
	    DefaultListModel vData = (DefaultListModel) getModel();
	    vData.add(pRow, nonMutableModel.get(pRealPos));
	}

>>>>>>> .r71
    }
	}*/
}
