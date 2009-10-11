/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jaccounting;

import jaccounting.exceptions.GenericException;
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
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

/**
 * @author bouba
 */
public class PersistenceHandler {

    private Map<Account, BigInteger> accountIds;
    private Map<Transaction, BigInteger> transactionIds;
    private Map<TransactionEntry, BigInteger> transactionEntryIds;
    private Map<BigInteger, Account> idAccounts;
    private Map<BigInteger, Transaction> idTransactions;
    private Map<BigInteger, TransactionEntry> idTransactionEntries;
    private BigInteger lastAccountId;
    private BigInteger lastTransactionId;
    private BigInteger lastTransactionEntryId;
    private Iterator accountElementsIterator;
    private Iterator transactionElementsIterator;
    private Iterator transactionEntryElementsIterator;

    public PersistenceHandler() {
	reset();
    }

    private void reset() {
	accountIds = new HashMap();
	transactionIds = new HashMap();
	transactionEntryIds = new HashMap();
	idAccounts = new HashMap();
	idTransactions = new HashMap();
	idTransactionEntries = new HashMap();
	lastAccountId = BigInteger.ZERO;
	lastTransactionId = BigInteger.ZERO;
	lastTransactionEntryId = BigInteger.ZERO;
    }

    private BigInteger getIdOfAccount(Account pAcct) {
	BigInteger rId;

	if ((rId = accountIds.get(pAcct)) == null) {
	    rId = lastAccountId = lastAccountId.add(BigInteger.ONE);
	    accountIds.put(pAcct, rId);
	}

	return rId;
    }

    private Account getAccountOfId(BigInteger pId) {
	Account rAcct;

	if ((rAcct = idAccounts.get(pId)) == null) {
	    boolean vStop = false;
	    Element vEl;
	    BigInteger vId;
	    List<TransactionEntry> vEntries = new ArrayList();
	    ListIterator vIt;

	    while (!vStop && accountElementsIterator.hasNext()) {
		vEl = (Element) accountElementsIterator.next();
		vId = new BigInteger(vEl.getAttributeValue("id"));
		String vName = "";
		String vDescription = "";
		int vNumber = -1;
		double vBalance = 0.0;
		Account.Type vType = null;
		boolean vTransEnabled = false;
		Element vKids = vEl.getChild("List");
		Element vKid;
		String vKidName;
		String vText;
		TransactionEntry vEntry;
		int vInd = 0;

		vIt = vEl.getChildren().listIterator();
		while (vIt.hasNext()) {
		    vKid = (Element)vIt.next();
		    vKidName = vKid.getAttributeValue("name");
		    vText = vKid.getTextTrim();
		    if (vKidName.equals("number")) {
			vNumber = Integer.parseInt(vText);
		    } else if (vKidName.equals("name")) {
			vName = vText;
		    } else if (vKidName.equals("description")) {
			vDescription = vText;
		    } else if (vKidName.equals("balance")) {
			vBalance = Double.parseDouble(vText);
		    } else if (vKidName.equals("type")) {
			vType = Enum.valueOf(Account.Type.class, vText);
		    } else if (vKidName.equals("transactionsEnabled")) {
			vTransEnabled = Boolean.parseBoolean(vText);
		    }
<<<<<<< .mine
		}
		vEntry = unserializeTransactionEntry("listEntry-"+vInd++, vKids);
			while (vEntry != null) {
			    vEntries.add(vEntry);
		    vEntry = unserializeTransactionEntry("listEntry-"+vInd++, vKids);
			}
=======
		}
		vEntry = unserializeTransactionEntry("listEntry-"+vInd++, vKids);
		while (vEntry != null) {
		    vEntries.add(vEntry);
		    vEntry = unserializeTransactionEntry("listEntry-"+vInd++, vKids);
		}
>>>>>>> .r71
		try {
		    rAcct = Account.createAccount(vNumber, vName, vDescription, vBalance,
						 vType, vEntries, vTransEnabled);
		    idAccounts.put(vId, rAcct);
		    if (vId.equals(pId)) vStop = true;
		} catch (GenericException ex) {

		}
<<<<<<< .mine
		}
=======
>>>>>>> .r71
	    }

	return rAcct;
    }

    private BigInteger getIdOfTransaction(Transaction pVal) {
	BigInteger rId;

	if ((rId = transactionIds.get(pVal)) == null) {
	    rId = lastTransactionId = lastTransactionId.add(BigInteger.ONE);
	    transactionIds.put(pVal, rId);
	}

	return rId;
    }

    private Transaction getTransactionOfId(BigInteger pId) {
	Transaction rTrans;

	if ((rTrans = idTransactions.get(pId)) == null) {
	    boolean vStop = false;
	    Element vEl;
	    BigInteger vId;
	    ListIterator vIt;

	    while (!vStop && transactionElementsIterator.hasNext()) {
		vEl = (Element) transactionElementsIterator.next();
		vId = new BigInteger(vEl.getAttributeValue("id"));
		Date vDate = null;
		String vRefNo = "";
		String vMemo = "";
		double vAmount = 0.0;
		TransactionEntry vDebitEntry = null;
		TransactionEntry vCreditEntry = null;
		Element vKid;
		String vKidName;
		String vText;

		vIt = vEl.getChildren().listIterator();
		while (vIt.hasNext()) {
		    vKid = (Element)vIt.next();
		    vKidName = vKid.getAttributeValue("name");
		    vText = vKid.getTextTrim();
		    if (vKidName.equals("date")) {
			vDate = new Date(vText);
		    } else if (vKidName.equals("refNo")) {
			vRefNo = vText;
		    } else if (vKidName.equals("memo")) {
			vMemo = vText;
		    } else if (vKidName.equals("amount")) {
			vAmount = Double.parseDouble(vText);
		    } else if (vKidName.equals("debitEntry")) {
			vDebitEntry = getTransactionEntryOfId(new BigInteger(vText));
		    } else if (vKidName.equals("creditEntry")) {
			vCreditEntry = getTransactionEntryOfId(new BigInteger(vText));
		    }
		}
		rTrans = new Transaction(vDate, vRefNo, vMemo, vAmount, vDebitEntry, vCreditEntry);
		idTransactions.put(vId, rTrans);
		if (vId.equals(pId)) vStop = true;
	    }
	}

	return rTrans;
    }

    private BigInteger getIdOfTransactionEntry(TransactionEntry pVal) {
	BigInteger rId;

	if ((rId = transactionEntryIds.get(pVal)) == null) {
	    rId = lastTransactionEntryId = lastTransactionEntryId.add(BigInteger.ONE);
	    transactionEntryIds.put(pVal, rId);
	}

	return rId;
    }

    private TransactionEntry getTransactionEntryOfId(BigInteger pId) {
	TransactionEntry rEntry;

	if ((rEntry = idTransactionEntries.get(pId)) == null) {
	    boolean vStop = false;
	    Element vEl;
	    BigInteger vId;

	    while (!vStop && transactionEntryElementsIterator.hasNext()) {
		vEl = (Element) transactionEntryElementsIterator.next();
		vId = new BigInteger(vEl.getAttributeValue("id"));
<<<<<<< .mine
		rEntry = new TransactionEntry(getAccountOfId(new BigInteger(vEl.getChildTextTrim("ref-Account"))),
					     null,
					     Enum.valueOf(TransactionEntry.Type.class, vEl.getChildTextTrim("Enum")),
						Double.parseDouble(vEl.getChildTextTrim("double")));
=======
		rEntry = new TransactionEntry(getAccountOfId(new BigInteger(vEl.getChildTextTrim("ref-Account"))),
					     null,
					     Enum.valueOf(TransactionEntry.Type.class, vEl.getChildTextTrim("Enum")),
					     Double.parseDouble(vEl.getChildTextTrim("double")));
>>>>>>> .r71
		idTransactionEntries.put(vId, rEntry);
		if (vId.equals(pId)) vStop = true;
	    }
	}

	return rEntry;
    }

    public void persist(String pFilename) throws IOException {
	OutputStream vStream = JAccounting.getApplication().getContext().getLocalStorage().openOutputFile(pFilename);
	XMLOutputter vOut = new XMLOutputter(Format.getPrettyFormat());
	Document vDoc = new Document();
	Element vRoot = new Element("JAccounting");

	vDoc.setRootElement(vRoot);
	serializeData(ModelsMngr.getInstance().getData(), vRoot);
	vRoot.addContent(new Element("lastAccountId").setText(lastAccountId+""));
	vRoot.addContent(new Element("lastTransactionId").setText(lastTransactionId+""));
	vRoot.addContent(new Element("lastTransactionEntryId").setText(lastTransactionEntryId+""));
	vOut.output(vDoc, vStream);
	vStream.flush();
	vStream.close();
	reset();
    }

<<<<<<< .mine
    public Data unpersist(String pFilename) throws IOException, JDOMException {
	InputStream vStream =  JAccounting.getApplication().getContext().getLocalStorage().openInputFile(pFilename);
	Data rData;
	    SAXBuilder vBuilder = new SAXBuilder();
	Document vDoc = vBuilder.build(vStream);
	    Element vRoot = vDoc.getRootElement();
=======
    public Data unpersist(String pFilename) throws IOException, JDOMException {
	InputStream vStream =  JAccounting.getApplication().getContext().getLocalStorage().openInputFile(pFilename);
	Data rData;
	SAXBuilder vBuilder = new SAXBuilder();
	Document vDoc = vBuilder.build(vStream);
	Element vRoot = vDoc.getRootElement();

	accountElementsIterator = vRoot.getDescendants(new ElementFilter("Account"));
	transactionElementsIterator = vRoot.getDescendants(new ElementFilter("Transaction"));
	transactionEntryElementsIterator = vRoot.getDescendants(new ElementFilter("TransactionEntry"));
	rData = unserializeData(vRoot);
	reset();
	//Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Should unpersist now");
>>>>>>> .r71

<<<<<<< .mine
	    accountElementsIterator = vRoot.getDescendants(new ElementFilter("Account"));
	    transactionElementsIterator = vRoot.getDescendants(new ElementFilter("Transaction"));
	    transactionEntryElementsIterator = vRoot.getDescendants(new ElementFilter("TransactionEntry"));
	    rData = unserializeData(vRoot);
	    reset();
	//Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Should unpersist now");

=======
>>>>>>> .r71
	return rData;
    }

    private void serializeData(Data pData, Element pRoot) {
	Element vEl = new Element("Data");

	vEl.setAttribute("name", "data");
	serializeGeneralLedger(pData.getGeneralLedger(), vEl);
	serializeJournal(pData.getJournal(), vEl);
	pRoot.addContent(vEl);
    }

    private Data unserializeData(Element pRoot) {
	Element vEl = pRoot.getChild("Data");
	return new Data(unserializeJournal(vEl), unserializeGeneralLedger(vEl));
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

    private GeneralLedger unserializeGeneralLedger(Element pRoot) {
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

    private AccountTreeNode unserializeAccountTreeNode(String pName, Element pRoot) {
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

    private Account unserializeAccount(Element pRoot) {
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
<<<<<<< .mine
=======

	ListIterator vIt = pRoot.getChildren("Transaction").listIterator();
>>>>>>> .r71
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
