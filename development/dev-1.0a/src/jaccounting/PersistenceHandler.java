/*
 * ModelsMngr.java	    1.0.0	    09/2009
 * This file contains the main class of the JAccounting application.
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

package jaccounting;

import jaccounting.exceptions.GenericException;
import jaccounting.exceptions.UnPersistenceFailureException;
import jaccounting.models.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import javax.swing.tree.DefaultMutableTreeNode;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.filter.ElementFilter;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

/**
 * PersistenceHandler is the class handling the persistence operations on the
 * application data. A PersistenceHandler object is delegated those operations
 * by the {@link ModelsMngr}. It uses the jdom library to serialize a Data object
 * into a specific xml format and to parse such xml back into an application Data
 * object.
 *
 * @author	    Boubacar Diallo
 * @version	    1.0.0
 * @see		    ModelsMngr
 * @see		    jaccounting.models.Data
 * @since	    1.0.0
 */
public class PersistenceHandler {

    /**
     * When persisting a Data object, we generate ids for the Accounts as we cycle
     * through the GeneralLedger to serialize them and store those ids in the
     * accountIds map indexed by the Account object. As we serialize other data
     * objects referencing an Account, we pull the id of the Account from the
     * above map and store the id in place instead of reserializing the Account
     * object -which had already been serialized when dealing with the GeneralLedger.
     * The same is done for Transactions and TransactionEntries with respective
     * maps transactionIds and transactionEntryIds. In summary, Accounts are
     * serialized as part of the GeneralLedger and their ids are stored anywhere
     * else they are referenced. The TransactionEntries are stored as part of the
     * Account they belong to and the Transactions are stored as part of the Journal.
     * In addition the ids get stored as part of the xml.
     *
     * The general format of the xml is that variables are store in nodes named
     * after their type except for nodes that do not store the actual object's
     * serialization but rather a reference to its xml id in which case the node
     * name is prefix with "ref-". Nodes are given a name attribute that corresponds
     * to the variable name and in case of objects that are referenced, an id
     * attribute is stored as well.
     *
     * When unpersiting the contents of a file into a Data object, we retrieve all
     * the nodes named "Account", unserialize their content as Account objects
     * and store the resulting objects into idAccounts indexed by the xml id. As
     * part of the unserialization of an Account we unserialize its TransactionEntries
     * and store them in the idTransactionEntries map. However we do not fully
     * unserialize a TransactionEntry as we live out its transferAccount property
     * to avoid getting into unpleasant cycles -i.e unserialize Account leads to
     * unserialize TransactionEntry which leads to unserialize its transferAccount Account
     * and so on. We also leave out the transaction property for the same reasons.
     * Transactions are unserialized when dealing with the Journal; since the
     * TransactionEntries are already unserialized at that time we use the stored
     * ids in xml to get them the idTransactionEntries map and at this time set
     * their transaction property to the Transaction we are unserializing. When
     * done with the GeneralLedger and the Journal, we cycle one last time through
     * the nodes named "TransactionEntry", use the stored id in the xml to retrieve
     * the corresponding TransactionEntry objects from the idTransactionEntries map,
     * use the stored id of the transferAccount in the xml to get the right Account
     * from the idAccounts map and update the TransactionEntry object.
     *
     * After persiting/unpersisting we safely reset all maps and last id values
     * trackers to save memory.
     */


    /**	mapping of Accounts to their xml ids; used when persisting. */
    private Map<Account, BigInteger> accountIds;

    /**	mapping of Transactions to their xml ids; used when persisting. */
    private Map<Transaction, BigInteger> transactionIds;

    /**
     * mapping of Transaction Entries to their xml ids; used when persisting
     * and unpersisting.
     */
    private Map<TransactionEntry, BigInteger> transactionEntryIds;

    /**	mapping of xml ids to their Accounts; used when unpersisting. */
    private Map<BigInteger, Account> idAccounts;

    /**	mapping of xml ids to their Transactions; used when unpersisting. */
    private Map<BigInteger, Transaction> idTransactions;

    /**	mapping of xml ids to their Transaction Entries; used when unpersisting. */
    private Map<BigInteger, TransactionEntry> idTransactionEntries;

    /**	last generated xml id of an Account; used when persisting. */
    private BigInteger lastAccountId;

    /**	last generated xml id of a Transaction; used when persisting. */
    private BigInteger lastTransactionId;

    /**	last generated xml id of a Transaction Entry; used when persisting. */
    private BigInteger lastTransactionEntryId;

    /**
     * iterator for the list of all xml elements of node name "Account";
     * used when unpersisting.
     */
    private Iterator accountElementsIterator;

    /**
     * iterator for the list of all xml elements of node name "Transaction";
     * used when unpersisting.
     */
    private Iterator transactionElementsIterator;

    /**
     * iterator for the list of all xml elements of node name "Transaction Entry";
     * used when unpersisting.
     */
    private Iterator transactionEntryElementsIterator;


    /**
     * Sole Constructor. Resets instance variables to their default values.
     * @since		    1.0.0
     */
    public PersistenceHandler() {
	reset();
    }


    private void reset() {
	accountIds = new HashMap<Account, BigInteger>();
	transactionIds = new HashMap<Transaction, BigInteger>();
	transactionEntryIds = new HashMap<TransactionEntry, BigInteger>();

	idAccounts = new HashMap<BigInteger, Account>();
	idTransactions = new HashMap<BigInteger, Transaction>();
	idTransactionEntries = new HashMap<BigInteger, TransactionEntry>();
	
	lastAccountId = BigInteger.ZERO;
	lastTransactionId = BigInteger.ZERO;
	lastTransactionEntryId = BigInteger.ZERO;

	accountElementsIterator = null;
	transactionElementsIterator = null;
	transactionEntryElementsIterator = null;
    }

    private BigInteger getIdOfAccount(Account pAcct) {
	BigInteger rId;

	/**
	 * Find the id of the Account from the accountIds map. If that Account's
	 * id hasn't been added yet, generate an id for it by incrementing
	 * lastAccountId and add it to the map.
	 */
	if ((rId = accountIds.get(pAcct)) == null) {
	    rId = lastAccountId = lastAccountId.add(BigInteger.ONE);
	    accountIds.put(pAcct, rId);
	}

	return rId;
    }

    private Account getAccountOfId(BigInteger pId) throws UnPersistenceFailureException {
	Account rAcct;

	/**
	 * Find the Account with the passed id in the idAccounts map. If the
	 * the Account with that id hasn't been added yet, we cyle through the
	 * xml nodes named "Account" and unserialize them until we reach the
	 * Account with our id.
	 */
	if ((rAcct = idAccounts.get(pId)) == null) {
	    boolean vStop = false;	    // stop flag when we reach our Account
	    Element vEl;		    // current "Account" node
	    BigInteger vId;		    // id of current "Account" node
	    ListIterator vIt;		    // current "Account" node's children iterator

	    while (!vStop && accountElementsIterator.hasNext()) {
		/** unserialized values of Account properties */
		String vName = "";
		String vDescription = "";
		int vNumber = -1;
		double vBalance = 0.0;
		Account.Type vType = null;
		boolean vTransEnabled = false;
		List<TransactionEntry> vEntries = new ArrayList();

		Element vKid;		// child node being examined
		String vKidName;	// child node's name attribute value
		String vText;		// child node's text content
		TransactionEntry vEntry;// currently unserialized TransactionEntry
		int vInd = 0;		// currently unserialized TransactionEntry index

		vEl = (Element) accountElementsIterator.next();
		vId = new BigInteger(vEl.getAttributeValue("id"));
		vIt = vEl.getChildren().listIterator();

		/**
		 * Cycle through this "Account" node's children and unserialize
		 * the Account's properties before later creating the Account
		 */
		while (vIt.hasNext()) {
		    vKid = (Element)vIt.next();
		    vKidName = vKid.getAttributeValue("name");
		    vText = vKid.getTextTrim();
		    if (vKidName.equals("number")) {
			vNumber = Integer.parseInt(vText);
		    }
		    else if (vKidName.equals("name")) {
			vName = vText;
		    }
		    else if (vKidName.equals("description")) {
			vDescription = vText;
		    }
		    else if (vKidName.equals("balance")) {
			vBalance = Double.parseDouble(vText);
		    }
		    else if (vKidName.equals("type")) {
			vType = Enum.valueOf(Account.Type.class, vText);
		    }
		    else if (vKidName.equals("transactionsEnabled")) {
			vTransEnabled = Boolean.parseBoolean(vText);
		    }
		    else if (vKidName.equals("entries")) {
			vEntry = unserializeTransactionEntry("listEntry-"+vInd++, vKid);
			while (vEntry != null) {
			    vEntries.add(vEntry);
			    vEntry = unserializeTransactionEntry("listEntry-"+vInd++, vKid);
			}
		    }
		}

		/**
		 * Create our Account from the unserialized properties values,
		 * add it to the idAccounts map and stop when we found the Account
		 * matching our pId.
		 */
		try {
		    rAcct = Account.createAccount(vNumber, vName, vDescription, vBalance,
						 vType, vEntries, vTransEnabled);
		    idAccounts.put(vId, rAcct);
		    if (vId.equals(pId)) vStop = true;
		}
		catch (GenericException ex) {
		    throw new UnPersistenceFailureException();
		}
	    }
	}

	return rAcct;
    }

    private void setEntriesTransferAccount() throws UnPersistenceFailureException {
	Element vEl;		// the "TransactionEntry" node being examined
	BigInteger vId;		// its id attribute value
	TransactionEntry vEnt;	// the TransactionEntry matching that id from the map
	BigInteger vAcctId;	// its "ref-Account" child node content
	Account vAcct;		// the Account matching that value from the map

	/**
	 * Cycle through all nodes named "TransactionEntry", get the TransactionEntry
	 * from the idTransactionEntries map using the id attribute, get the
	 * transferAccount Account from the idAccounts map using its "ref-Account"
	 * child node's content and set the TransactionEntry's transferAccount
	 * property.
	 * See class implementation note at beginning for why.
	 */
	while (transactionEntryElementsIterator.hasNext()) {
	    vEl = (Element) transactionEntryElementsIterator.next();
	    vId = new BigInteger(vEl.getAttributeValue("id"));
	    vEnt = idTransactionEntries.get(vId);
	    if (vEnt == null) {
		/*Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Failed to retrieve Transaction Entry of id " + vId);
		continue;*/
		throw new UnPersistenceFailureException();
	    }

	    vAcctId = new BigInteger(vEl.getChildTextTrim("ref-Account"));
	    vAcct = idAccounts.get(vAcctId);
	    if (vAcct == null) {
		/*Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Failed to retrieve Account of id " + vAcctId);
		continue;*/
		throw new UnPersistenceFailureException();
	    }

	    vEnt.initTransferAccount(vAcct);
	}
    }

    private BigInteger getIdOfTransaction(Transaction pVal) {
	BigInteger rId;

	/**
	 * Find the id of the Transaction from the transactionIds map. If that Transaction's
	 * id hasn't been added yet, generate an id for it by incrementing
	 * lastTransactionId and add it to the map.
	 */
	if ((rId = transactionIds.get(pVal)) == null) {
	    rId = lastTransactionId = lastTransactionId.add(BigInteger.ONE);
	    transactionIds.put(pVal, rId);
	}

	return rId;
    }

    private Transaction getTransactionOfId(BigInteger pId) {
	Transaction rTrans;

	/**
	 * Find the Transaction with the passed id in the idTransactions map. If the
	 * the Transaction with that id hasn't been added yet, cyle through the
	 * xml nodes named "Transaction" and unserialize them until we reach the
	 * Transaction with our id.
	 */
	if ((rTrans = idTransactions.get(pId)) == null) {
	    boolean vStop = false;	// stop flag when we reach our Transaction
	    Element vEl;		// current "Transaction" node
	    BigInteger vId;		// id of current "Transaction" node
	    ListIterator vIt;		// current "Transaction" node's children iterator

	    while (!vStop && transactionElementsIterator.hasNext()) {
		/** unserialized values of Transaction properties */
		Date vDate = null;
		String vRefNo = "";
		String vMemo = "";
		double vAmount = 0.0;
		TransactionEntry vDebitEntry = null;
		TransactionEntry vCreditEntry = null;

		Element vKid;		// child node being examined
		String vKidName;	// child node's name attribute value
		String vText;		// child node's text content

		vEl = (Element) transactionElementsIterator.next();
		vId = new BigInteger(vEl.getAttributeValue("id"));
		vIt = vEl.getChildren().listIterator();

		/**
		 * Cycle through this "Transaction" node's children and unserialize
		 * the Transaction's properties before later creating the Transaction
		 */
		while (vIt.hasNext()) {
		    vKid = (Element)vIt.next();
		    vKidName = vKid.getAttributeValue("name");
		    vText = vKid.getTextTrim();
		    if (vKidName.equals("date")) {
			vDate = new Date(vText);
		    }
		    else if (vKidName.equals("refNo")) {
			vRefNo = vText;
		    }
		    else if (vKidName.equals("memo")) {
			vMemo = vText;
		    }
		    else if (vKidName.equals("amount")) {
			vAmount = Double.parseDouble(vText);
		    }
		    else if (vKidName.equals("debitEntry")) {
			vDebitEntry = getTransactionEntryOfId(new BigInteger(vText));
		    }
		    else if (vKidName.equals("creditEntry")) {
			vCreditEntry = getTransactionEntryOfId(new BigInteger(vText));
		    }
		}

		/**
		 * Create our Transaction from the unserialized properties values,
		 * add it to the idTransactions map and stop when we found the Transaction
		 * matching our pId.
		 */
		rTrans = Transaction.createTransaction(vDate, vRefNo, vMemo, vAmount, vDebitEntry, vCreditEntry);
		idTransactions.put(vId, rTrans);
		if (vId.equals(pId)) vStop = true;
	    }
	}

	return rTrans;
    }

    private BigInteger getIdOfTransactionEntry(TransactionEntry pVal) {
	BigInteger rId;

	/**
	 * Find the id of the TransactionEntry from the transactionEntryIds map.
	 * If that TransactionEntry's id hasn't been added yet, generate an id
	 * for it by incrementing lastTransactionEntryId and add it to the map.
	 */
	if ((rId = transactionEntryIds.get(pVal)) == null) {
	    rId = lastTransactionEntryId = lastTransactionEntryId.add(BigInteger.ONE);
	    transactionEntryIds.put(pVal, rId);
	}

	return rId;
    }

    private TransactionEntry getTransactionEntryOfId(BigInteger pId) {
	TransactionEntry rEntry;

	/**
	 * Find the TransactionEntry with the passed id in the idTransactionEntries map. If the
	 * the TransactionEntry with that id hasn't been added yet, cyle through the
	 * xml nodes named "TransactionEntry" and unserialize them until we reach the
	 * TransactionEntry with our id.
	 */
	if ((rEntry = idTransactionEntries.get(pId)) == null) {
	    boolean vStop = false;	// stop flag when we reach our TransactionEntry
	    Element vEl;		// current "TransactionEntry" node
	    BigInteger vId;		// id of current "TransactionEntry" node

	    while (!vStop && transactionEntryElementsIterator.hasNext()) {
		vEl = (Element) transactionEntryElementsIterator.next();
		vId = new BigInteger(vEl.getAttributeValue("id"));
		
		rEntry = new TransactionEntry(null, null, Enum.valueOf(TransactionEntry.Type.class,
									vEl.getChildTextTrim("Enum")),
						Double.parseDouble(vEl.getChildTextTrim("double")));
		idTransactionEntries.put(vId, rEntry);
		//transactionEntryIds.put(rEntry, vId);
		if (vId.equals(pId)) vStop = true;
	    }
	}

	return rEntry;
    }

    /**
     * Saves a Data object to an OutputStream as an xml document.
     *
     * @param pData		the Data object to save
     * @param pStream		the OutputStream to save to
     * @throws IOException	if an I/O error ocurred
     * @see			jaccounting.models.Data
     * @since			1.0.0
     */
    public void persist(Data pData, OutputStream pStream) throws IOException {
	XMLOutputter vOut = new XMLOutputter();
	Document vDoc = new Document();
	Element vRoot = new Element("JAccounting");

	vDoc.setRootElement(vRoot);

	JAccounting.getApplication().getProgressReporter()
	    .reportUsingKey("messages.serializingData");
	serializeData(pData, vRoot);

	JAccounting.getApplication().getProgressReporter()
	    .reportUsingKey("messages.outputingXML");
	vOut.output(vDoc, pStream);

	pStream.flush();
	pStream.close();
	reset();
	JAccounting.getApplication().getProgressReporter().reportFinished();
    }

    /**
     * Parses out the xml contained in an InputStream into a Data object.
     *
     * @param pStream				the InputStream containing an xml
     *						representation of an application Data
     * @return					the Data object constructed from
     *						xml from InpputStream
     * @throws IOException			if an I/O error occured
     * @throws UnPersistenceFailureException	if an xml parsing error occured
     * @see					jaccounting.models.Data
     * @since					1.0.0
     */
    public Data unpersist(InputStream pStream) throws IOException, UnPersistenceFailureException {
	Data rData = null;

	try {
	    SAXBuilder vBuilder = new SAXBuilder();
	    JAccounting.getApplication().getProgressReporter()
		.reportUsingKey("messages.unserializingData");
	    Document vDoc = vBuilder.build(pStream);
	    Element vRoot = vDoc.getRootElement();

	    accountElementsIterator = vRoot.getDescendants(new ElementFilter("Account"));
	    transactionElementsIterator = vRoot.getDescendants(new ElementFilter("Transaction"));
	    transactionEntryElementsIterator = vRoot.getDescendants(new ElementFilter("TransactionEntry"));

	    JAccounting.getApplication().getProgressReporter()
		.reportUsingKey("messages.unserializingData");
	    rData = unserializeData(vRoot);
	    transactionEntryElementsIterator = vRoot.getDescendants(new ElementFilter("TransactionEntry"));
	    setEntriesTransferAccount();
	}
	catch (JDOMException jDOMException) {
	    throw new UnPersistenceFailureException();
	}
	finally {
	    reset();
	}

	JAccounting.getApplication().getProgressReporter().reportFinished();
	return rData;
    }

    private void serializeData(Data pData, Element pRoot) {
	Element vEl = new Element("Data");

	vEl.setAttribute("name", "data");
	serializeGeneralLedger(pData.getGeneralLedger(), vEl);
	serializeJournal(pData.getJournal(), vEl);
	
	pRoot.addContent(vEl);
    }

    private Data unserializeData(Element pRoot) throws UnPersistenceFailureException {
	Element vEl = pRoot.getChild("Data");
	GeneralLedger vLedg = unserializeGeneralLedger(vEl);
	Journal vJour = unserializeJournal(vEl);

	return new Data(vJour, vLedg);
    }

    private void serializeGeneralLedger(GeneralLedger pLedger, Element pRoot) {
	Element vEl = new Element("GeneralLedger");
	Element vRootEl = new Element("TreeNode");

	vEl.setAttribute("name", "generalLedger");
	vRootEl.setText((String) pLedger.getRoot().getUserObject());
	vRootEl.setAttribute("name", "root");
	vEl.addContent(vRootEl);
	
	serializeAccountTreeNode("assetsNode", pLedger.getAssetsNode(), vEl);
	serializeAccountTreeNode("liabilitiesNode", pLedger.getLiabilitiesNode(), vEl);
	serializeAccountTreeNode("equityNode", pLedger.getEquityNode(), vEl);
	serializeAccountTreeNode("revenuesNode", pLedger.getRevenuesNode(), vEl);
	serializeAccountTreeNode("expensesNode", pLedger.getExpensesNode(), vEl);

	pRoot.addContent(vEl);
    }

    private GeneralLedger unserializeGeneralLedger(Element pRoot) throws UnPersistenceFailureException {
	Element vEl = pRoot.getChild("GeneralLedger");
	DefaultMutableTreeNode vRoot = new DefaultMutableTreeNode(vEl.getChild("TreeNode").getTextTrim());

	return new GeneralLedger(vRoot, unserializeAccountTreeNode("assetsNode", vEl),
				 unserializeAccountTreeNode("liabilitiesNode", vEl),
				 unserializeAccountTreeNode("revenuesNode", vEl),
				 unserializeAccountTreeNode("expensesNode", vEl),
				 unserializeAccountTreeNode("equityNode", vEl));
    }

    private void serializeAccountTreeNode(String pName, AccountTreeNode pNode, Element pRoot) {
	Element vEl = new Element("AccountTreeNode").setAttribute("name", pName);
	Element vKid = new Element("Enumeration").setAttribute("name", "children");
	Enumeration vChildren = pNode.children();
	int vInd = 0;

	serializeAccount("userObject", (Account)pNode.getUserObject(), vEl);
	while (vChildren.hasMoreElements()) {
	    serializeAccountTreeNode("childElement-"+vInd++, (AccountTreeNode)vChildren.nextElement(), vKid);
	}
	vEl.addContent(vKid);
	
	pRoot.addContent(vEl);
    }

    private AccountTreeNode unserializeAccountTreeNode(String pName, Element pRoot) throws UnPersistenceFailureException {
	Element vEl;
	AccountTreeNode rNode = null;
	ListIterator vIt = pRoot.getChildren("AccountTreeNode").listIterator();

	while (vIt.hasNext()) {
	    vEl = (Element)vIt.next();
	    if (vEl.getAttributeValue("name").equals(pName)) {
		Element vKids = vEl.getChild("Enumeration");
		AccountTreeNode vNode;
		int vInd = 0;

		rNode = new AccountTreeNode(unserializeAccount(vEl));
		vNode = unserializeAccountTreeNode("childElement-"+vInd++, vKids);
		while (vNode != null) {
		    rNode.add(vNode);
		    vNode = unserializeAccountTreeNode("childElement-"+vInd++, vKids);
		}
	    }
	}

	return rNode;
    }

    private void serializeAccount(String pName, Account pAcct, Element pRoot) {
	Element vEl = new Element("Account");
	Element vKid = new Element("List");
	ListIterator vIt = pAcct.getEntries().listIterator();
	int vInd = 0;

	vEl.setAttribute("id", getIdOfAccount(pAcct).toString());
	vEl.setAttribute("name", pName);
	vKid.setAttribute("name", "entries");
	vEl.addContent(new Element("int").setAttribute("name", "number")
			    .setText(pAcct.getNumber()+""));
	vEl.addContent(new Element("String").setAttribute("name", "name")
			    .setText(pAcct.getName()));
	vEl.addContent(new Element("String").setAttribute("name", "description")
			    .setText(pAcct.getDescription()));
	vEl.addContent(new Element("double").setAttribute("name", "balance")
			    .setText(pAcct.getBalance()+""));
	vEl.addContent(new Element("boolean").setAttribute("name", "transactionsEnabled")
			    .setText(String.valueOf(pAcct.isTransactionsEnabled())));
	vEl.addContent(new Element("Enum").setAttribute("name", "type")
			    .setText(pAcct.getType().toString()));
	while (vIt.hasNext()) {
	    serializeTransactionEntry("listEntry-"+vInd++, (TransactionEntry)vIt.next(), vKid);
	}
	vEl.addContent(vKid);

	pRoot.addContent(vEl);
    }

    private Account unserializeAccount(Element pRoot) throws UnPersistenceFailureException {
	Element vEl = pRoot.getChild("Account");
	return getAccountOfId(new BigInteger(vEl.getAttributeValue("id")));
    }

    private void serializeJournal(Journal pJournal, Element pRoot) {
	Element vEl = new Element("Journal");
	Element vKid = new Element("List");
	ListIterator vIt = pJournal.getTransactions().listIterator();
	int vInd = 0;

	vEl.setAttribute("name", "journal");
	vKid.setAttribute("name", "transactions");
	while (vIt.hasNext()) {
	    serializeTransaction("listEntry-"+vInd++, (Transaction)vIt.next(), vKid);
	}
	vEl.addContent(vKid);

	pRoot.addContent(vEl);
    }

    private Journal unserializeJournal(Element pRoot) {
	List<Transaction> vTransactions = new ArrayList();
	Element vEl = pRoot.getChild("Journal");
	Element vKids = vEl.getChild("List");
	Transaction vTrans;
	int vInd = 0;

	vTrans = unserializeTransaction("listEntry-"+vInd++, vKids);
	while (vTrans != null) {
	    vTransactions.add(vTrans);
	    vTrans = unserializeTransaction("listEntry-"+vInd++, vKids);
	}

	return new Journal(vTransactions);
    }

    private void serializeTransaction(String pName, Transaction pTrans, Element pRoot) {
	Element vEl = new Element("Transaction");

	vEl.setAttribute("id", getIdOfTransaction(pTrans).toString());
	vEl.setAttribute("name", pName);
	vEl.addContent(new Element("String").setAttribute("name", "refNo")
			    .setText(pTrans.getRefNo()));
	vEl.addContent(new Element("String").setAttribute("name", "memo")
			    .setText(pTrans.getMemo()));
	vEl.addContent(new Element("double").setAttribute("name", "amount")
			    .setText(pTrans.getAmount()+""));
	vEl.addContent(new Element("Enum").setAttribute("name", "date")
			    .setText(pTrans.getDate().toString()));
	vEl.addContent(new Element("ref-TransactionEntry").setAttribute("name", "debitEntry")
			    .setText(getIdOfTransactionEntry(pTrans.getDebitEntry()).toString()));
	vEl.addContent(new Element("ref-TransactionEntry").setAttribute("name", "creditEntry")
			    .setText(getIdOfTransactionEntry(pTrans.getCreditEntry()).toString()));

	pRoot.addContent(vEl);
    }

    private Transaction unserializeTransaction(String pName, Element pRoot) {
	Element vEl;
	Transaction rTrans = null;
	ListIterator vIt = pRoot.getChildren("Transaction").listIterator();
	
	while (vIt.hasNext()) {
	    vEl = (Element)vIt.next();
	    if (vEl.getAttributeValue("name").equals(pName)) {
		rTrans = getTransactionOfId(new BigInteger(vEl.getAttributeValue("id")));
	    }
	}

	return rTrans;
    }
    
    private void serializeTransactionEntry(String pName, TransactionEntry pEntry, Element pRoot) {
	Element vEl = new Element("TransactionEntry");

	vEl.setAttribute("name", pName);
	vEl.setAttribute("id", getIdOfTransactionEntry(pEntry).toString());
	vEl.addContent(new Element("ref-Account").setAttribute("name", "transferAccount")
			    .setText(getIdOfAccount(pEntry.getTransferAccount()).toString()));
	vEl.addContent(new Element("ref-Transaction").setAttribute("name", "transaction")
			    .setText(getIdOfTransaction(pEntry.getTransaction()).toString()));
	vEl.addContent(new Element("double").setAttribute("name", "accountBalance")
			    .setText(pEntry.getAccountBalance()+""));
	vEl.addContent(new Element("Enum").setAttribute("name", "type")
			    .setText(pEntry.getType().toString()));

	pRoot.addContent(vEl);
    }

    private TransactionEntry unserializeTransactionEntry(String pName, Element pRoot) {
	Element vEl;
	TransactionEntry rEntry = null;
	ListIterator vIt = pRoot.getChildren("TransactionEntry").listIterator();

	while (vIt.hasNext()) {
	    vEl = (Element)vIt.next();
	    if (vEl.getAttributeValue("name").equals(pName)) {
		rEntry = getTransactionEntryOfId(new BigInteger(vEl.getAttributeValue("id")));
	    }
	}

	return rEntry;
    }

}
