/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jaccounting.models;

import jaccounting.JAccounting;
import jaccounting.ProgressReporter;
import jaccounting.exceptions.NotTransactionnableAccountException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import javax.swing.tree.DefaultMutableTreeNode;
import org.jdesktop.application.ResourceMap;

/**
 *
 * @author bouba
 */
public class GeneralLedger extends BaseModel {

    protected DefaultMutableTreeNode root;
    protected AccountTreeNode assetsNode;
    protected AccountTreeNode liabilitiesNode;
    protected AccountTreeNode revenuesNode;
    protected AccountTreeNode expensesNode;
    protected AccountTreeNode equityNode;

    public GeneralLedger() {
	ResourceMap vRmap = JAccounting.getApplication().getContext().getResourceMap(this.getClass());

	AssetAccount vAssets = new AssetAccount(-1, vRmap.getString("accountNames.assets"),
		vRmap.getString("accountDescriptions.assets"),
		0.0, false);
	LiabilityAccount vLiabilities = new LiabilityAccount(-1, vRmap.getString("accountNames.liabilities"),
		vRmap.getString("accountDescriptions.liabilities"),
		0.0, false);
	RevenueAccount vRevenues = new RevenueAccount(-1, vRmap.getString("accountNames.revenues"),
		vRmap.getString("accountDescriptions.revenues"),
		0.0, false);
	ExpenseAccount vExpenses = new ExpenseAccount(-1, vRmap.getString("accountNames.expenses"),
		vRmap.getString("accountDescriptions.expenses"),
		0.0, false);
	EquityAccount vEquity = new EquityAccount(-1, vRmap.getString("accountNames.equity"),
		vRmap.getString("accountDescriptions.equity"),
		0.0, false);

	assetsNode = new AccountTreeNode(vAssets);
	liabilitiesNode = new AccountTreeNode(vLiabilities);
	revenuesNode = new AccountTreeNode(vRevenues);
	expensesNode = new AccountTreeNode(vExpenses);
	equityNode = new AccountTreeNode(vEquity);

	root = new DefaultMutableTreeNode(vRmap.getString("accountNames.root"));
	root.add(assetsNode);
	root.add(liabilitiesNode);
	root.add(equityNode);
	root.add(revenuesNode);
	root.add(expensesNode);
    }

    public GeneralLedger(DefaultMutableTreeNode root, AccountTreeNode assetsNode, 
	    AccountTreeNode liabilitiesNode, AccountTreeNode revenuesNode,
	    AccountTreeNode expensesNode, AccountTreeNode equityNode) {
	this.root = root;
	this.assetsNode = assetsNode;
	this.liabilitiesNode = liabilitiesNode;
	this.revenuesNode = revenuesNode;
	this.expensesNode = expensesNode;
	this.equityNode = equityNode;
	this.root.add(this.assetsNode);
	this.root.add(this.liabilitiesNode);
	this.root.add(this.equityNode);
	this.root.add(this.revenuesNode);
	this.root.add(this.expensesNode);
    }

    public DefaultMutableTreeNode getRoot() {
	return root;
    }

    public AccountTreeNode getAssetsNode() {
	return assetsNode;
    }

    public AccountTreeNode getEquityNode() {
	return equityNode;
    }

    public AccountTreeNode getExpensesNode() {
	return expensesNode;
    }

    public AccountTreeNode getLiabilitiesNode() {
	return liabilitiesNode;
    }

    public AccountTreeNode getRevenuesNode() {
	return revenuesNode;
    }

    public Account getAccount(int pRow) {
	Account rAcct = null;
	AccountTreeNode vNode = getAccountNodeAtRow(pRow);

	if (vNode != null) rAcct = (Account) vNode.getUserObject();

	return rAcct;
    }

    public AccountTreeNode getAccountNodeAtRow(int pRow) {
	Enumeration vEnum = root.preorderEnumeration();

	vEnum.nextElement(); // skip root
	for (int vI = 1; vI < pRow; vI++) {
	    if (vEnum.hasMoreElements()) {
		vEnum.nextElement();
	    } else {
		break;
	    }
	}

	if (vEnum.hasMoreElements()) return (AccountTreeNode) vEnum.nextElement();
	else return null;
    }

    public int getRowOfAccount(Account pAcct) {
	Enumeration vEnum = root.preorderEnumeration();
	int rRow = -1;

	// skip root
	vEnum.nextElement();
	int vCurrRow = 0;

	while (vEnum.hasMoreElements()) {
	    vCurrRow++;
	    if (((Account)((AccountTreeNode)vEnum.nextElement()).getUserObject()).equals(pAcct)) {
		rRow = vCurrRow;
		break;
	    }
	}

	return rRow;
    }

    public void insertChildAccount(int pRow, Account pAcct) {
	AccountTreeNode vAcctNode = getAccountNodeAtRow(pRow);
	if (vAcctNode != null && vAcctNode.canHaveChildren() && vAcctNode.isAccountNameUniqueAmongChildren(pAcct.getName())) {
	    // add new node
	    vAcctNode.add(new AccountTreeNode(pAcct));
	    setChangedAndNotifyObservers();
	}
    }

    public void removeAccount(int pRow) throws NotTransactionnableAccountException {
	AccountTreeNode vAcctNode = getAccountNodeAtRow(pRow);
	if (vAcctNode != null && vAcctNode.canBeRemoved()) {
	    vAcctNode.remove();
	    setChangedAndNotifyObservers();
	}
    }

    public boolean canAccountBeEdited(int pRow) {
	AccountTreeNode vAcctNode = getAccountNodeAtRow(pRow);
	if (vAcctNode != null) return vAcctNode.canBeEdited();
	return false;
    }

    public boolean canAccountBeRemoved(int pRow) {
	AccountTreeNode vAcctNode = getAccountNodeAtRow(pRow);
	if (vAcctNode != null) return vAcctNode.canBeRemoved();
	return false;
    }

    public boolean canAccountHaveChildren(int pRow) {
	AccountTreeNode vAcctNode = getAccountNodeAtRow(pRow);
	if (vAcctNode != null) return vAcctNode.canHaveChildren();
	return false;
    }

    public boolean isAccountTopLevel(int pRow) {
	AccountTreeNode vAcctNode = getAccountNodeAtRow(pRow);
	if (vAcctNode != null) return vAcctNode.isTopLevel();
	return false;
    }

    public String[] getTransactionnableAccountFullNames() {
	return getAccountFullNames(true);
    }

    String[] getAccountFullNames(boolean pExcludeNonTransactionnable) {
	List<String> rAcctsList = new ArrayList();
	String[] rAccts = { };

	assetsNode.buildFullNames(rAcctsList, "", pExcludeNonTransactionnable);
	liabilitiesNode.buildFullNames(rAcctsList, "", pExcludeNonTransactionnable);
	equityNode.buildFullNames(rAcctsList, "", pExcludeNonTransactionnable);
	revenuesNode.buildFullNames(rAcctsList, "", pExcludeNonTransactionnable);
	expensesNode.buildFullNames(rAcctsList, "", pExcludeNonTransactionnable);

	rAccts = rAcctsList.toArray(rAccts);

	return rAccts;
    }

    public String getAccountFullName(Account pAcct) {
	String rName = "";
	int vRow = getRowOfAccount(pAcct);

	if (vRow != -1) {
	    rName = (getAccountNodeAtRow(vRow)).getFullName();
	}

	return rName;
    }

    public Account getAccount(String pFullName) {
	Account rAcct;

	rAcct = getAssetAccount(pFullName);
	if (rAcct != null) {
	    return rAcct;
	}

	rAcct = getLiabilityAccount(pFullName);
	if (rAcct != null) {
	    return rAcct;
	}

	rAcct = getEquityAccount(pFullName);
	if (rAcct != null) {
	    return rAcct;
	}

	rAcct = getRevenueAccount(pFullName);
	if (rAcct != null) {
	    return rAcct;
	}

	rAcct = getExpenseAccount(pFullName);
	
	return rAcct;
    }

    protected Account getAssetAccount(String pFullName) {
	return assetsNode.getAccount(pFullName);
    }

    protected Account getLiabilityAccount(String pFullName) {
	return liabilitiesNode.getAccount(pFullName);
    }

    protected Account getEquityAccount(String pFullName) {
	return equityNode.getAccount(pFullName);
    }

    protected Account getRevenueAccount(String pFullName) {
	return revenuesNode.getAccount(pFullName);
    }

    protected Account getExpenseAccount(String pFullName) {
	return expensesNode.getAccount(pFullName);
    }

    public void addNewDefaultAccounts() {
	addNewDefaultAssetAccounts();
	addNewDefaultLiabilityAccounts();
	addNewDefaultEquityAccounts();
	addNewDefaultRevenueAccounts();
	addNewDefaultExpenseAccounts();
    }

    private void addNewDefaultAssetAccounts() {
	ResourceMap vRmap = JAccounting.getApplication().getContext().getResourceMap(this.getClass());
	
	AssetAccount vCurrAssets = new AssetAccount(-1, vRmap.getString("defaultAccounts.currentAssets.name"),
		vRmap.getString("defaultAccounts.currentAssets.description"),
		0.0, true);
	AccountTreeNode vCurrAssetsNode = new AccountTreeNode(vCurrAssets);
	AssetAccount cash = new AssetAccount(-1, vRmap.getString("defaultAccounts.cash.name"),
		vRmap.getString("defaultAccounts.cash.description"),
		0.0, true);
	AccountTreeNode cashNode = new AccountTreeNode(cash);
	AssetAccount bankAccounts = new AssetAccount(-1, vRmap.getString("defaultAccounts.bankAccounts.name"),
		vRmap.getString("defaultAccounts.bankAccounts.description"),
		0.0, true);
	AccountTreeNode bankAccountsNode = new AccountTreeNode(bankAccounts);
	AssetAccount checkingAccount = new AssetAccount(-1, vRmap.getString("defaultAccounts.checkingAccount.name"),
		vRmap.getString("defaultAccounts.checkingAccount.description"),
		0.0, true);
	AccountTreeNode checkingAccountNode = new AccountTreeNode(checkingAccount);
	AssetAccount savingsAccount = new AssetAccount(-1, vRmap.getString("defaultAccounts.savingsAccount.name"),
		vRmap.getString("defaultAccounts.savingsAccount.description"),
		0.0, true);
	AccountTreeNode savingsAccountNode = new AccountTreeNode(savingsAccount);
	AssetAccount investments = new AssetAccount(-1, vRmap.getString("defaultAccounts.investments.name"),
		vRmap.getString("defaultAccounts.investments.description"),
		0.0, true);
	AssetAccount supplies = new AssetAccount(-1, vRmap.getString("defaultAccounts.supplies.name"),
		vRmap.getString("defaultAccounts.supplies.description"),
		0.0, true);
	AccountTreeNode suppliesNode = new AccountTreeNode(supplies);
	AssetAccount prepaidExpenses = new AssetAccount(-1, vRmap.getString("defaultAccounts.prepaidExpenses.name"),
		vRmap.getString("defaultAccounts.prepaidExpenses.description"),
		0.0, true);
	AccountTreeNode prepaidExpensesNode = new AccountTreeNode(prepaidExpenses);
	AssetAccount taxesRedeemable = new AssetAccount(-1, vRmap.getString("defaultAccounts.taxesRedeemable.name"),
		vRmap.getString("defaultAccounts.taxesRedeemable.description"),
		0.0, true);
	AccountTreeNode taxesRedeemableNode = new AccountTreeNode(taxesRedeemable);


	AssetAccount nonCurrentAssets = new AssetAccount(-1, vRmap.getString("defaultAccounts.nonCurrentAssets.name"),
		vRmap.getString("defaultAccounts.nonCurrentAssets.description"),
		0.0, true);
	AccountTreeNode nonCurrentAssetsNode = new AccountTreeNode(nonCurrentAssets);
	AssetAccount housingProperty = new AssetAccount(-1, vRmap.getString("defaultAccounts.housingProperty.name"),
		vRmap.getString("defaultAccounts.housingProperty.description"),
		0.0, true);
	AccountTreeNode housingPropertyNode = new AccountTreeNode(housingProperty);
	AssetAccount furnitures = new AssetAccount(-1, vRmap.getString("defaultAccounts.furnitures.name"),
		vRmap.getString("defaultAccounts.furnitures.description"),
		0.0, true);
	AccountTreeNode furnituresNode = new AccountTreeNode(furnitures);
	AssetAccount equipments = new AssetAccount(-1, vRmap.getString("defaultAccounts.equipments.name"),
		vRmap.getString("defaultAccounts.equipments.description"),
		0.0, true);
	AccountTreeNode equipmentsNode = new AccountTreeNode(equipments);
	AssetAccount vehicle = new AssetAccount(-1, vRmap.getString("defaultAccounts.vehicle.name"),
		vRmap.getString("defaultAccounts.vehicle.description"),
		0.0, true);
	AccountTreeNode vehicleNode = new AccountTreeNode(vehicle);
	AccountTreeNode investmentsNode = new AccountTreeNode(investments);
	AssetAccount bankCD = new AssetAccount(-1, vRmap.getString("defaultAccounts.bankCD.name"),
		vRmap.getString("defaultAccounts.bankCD.description"),
		0.0, true);
	AccountTreeNode bankCDNode = new AccountTreeNode(bankCD);
	AssetAccount brokerageAccount = new AssetAccount(-1, vRmap.getString("defaultAccounts.brokerageAccount.name"),
		vRmap.getString("defaultAccounts.brokerageAccount.description"),
		0.0, true);
	AccountTreeNode brokerageAccountNode = new AccountTreeNode(brokerageAccount);
	AssetAccount pensions = new AssetAccount(-1, vRmap.getString("defaultAccounts.pensions.name"),
		vRmap.getString("defaultAccounts.pensions.description"),
		0.0, true);
	AccountTreeNode pensionstNode = new AccountTreeNode(pensions);

	bankAccountsNode.add(checkingAccountNode);
	bankAccountsNode.add(savingsAccountNode);

	vCurrAssetsNode.add(cashNode);
	vCurrAssetsNode.add(bankAccountsNode);
	vCurrAssetsNode.add(suppliesNode);
	vCurrAssetsNode.add(prepaidExpensesNode);
	vCurrAssetsNode.add(taxesRedeemableNode);

	assetsNode.add(vCurrAssetsNode);

	investmentsNode.add(bankCDNode);
	investmentsNode.add(brokerageAccountNode);
	investmentsNode.add(pensionstNode);

	nonCurrentAssetsNode.add(housingPropertyNode);
	nonCurrentAssetsNode.add(furnituresNode);
	nonCurrentAssetsNode.add(equipmentsNode);
	nonCurrentAssetsNode.add(vehicleNode);
	nonCurrentAssetsNode.add(investmentsNode);

	assetsNode.add(nonCurrentAssetsNode);
    }

    private void addNewDefaultLiabilityAccounts() {
	ResourceMap vRmap = JAccounting.getApplication().getContext().getResourceMap(this.getClass());

	LiabilityAccount vCurrLiabilities = new LiabilityAccount(-1, vRmap.getString("defaultAccounts.currentLiabilities.name"),
		vRmap.getString("defaultAccounts.currentLiabilities.description"),
		0.0, true);
	AccountTreeNode vCurrLiabilitiesNode = new AccountTreeNode(vCurrLiabilities);
	LiabilityAccount creditCards = new LiabilityAccount(-1, vRmap.getString("defaultAccounts.creditCards.name"),
		vRmap.getString("defaultAccounts.creditCards.description"),
		0.0, true);
	AccountTreeNode creditCardsNode = new AccountTreeNode(creditCards);
	LiabilityAccount unpaidExpenses = new LiabilityAccount(-1, vRmap.getString("defaultAccounts.unpaidExpenses.name"),
		vRmap.getString("defaultAccounts.unpaidExpenses.description"),
		0.0, true);
	AccountTreeNode unpaidExpensesNode = new AccountTreeNode(unpaidExpenses);
	LiabilityAccount loansCurrentPortions = new LiabilityAccount(-1, vRmap.getString("defaultAccounts.loansCurrentPortions.name"),
		vRmap.getString("defaultAccounts.loansCurrentPortions.description"),
		0.0, true);
	AccountTreeNode loansCurrentPortionsNode = new AccountTreeNode(loansCurrentPortions);
	LiabilityAccount taxesPayable = new LiabilityAccount(-1, vRmap.getString("defaultAccounts.taxesPayable.name"),
		vRmap.getString("defaultAccounts.taxesPayable.description"),
		0.0, true);
	AccountTreeNode taxesPayableNode = new AccountTreeNode(taxesPayable);

	LiabilityAccount nonCurrentLiabilities = new LiabilityAccount(-1, vRmap.getString("defaultAccounts.nonCurrentLiabilities.name"),
		vRmap.getString("defaultAccounts.nonCurrentLiabilities.description"),
		0.0, true);
	AccountTreeNode nonCurrentLiabilitiesNode = new AccountTreeNode(nonCurrentLiabilities);
	LiabilityAccount loansNonCurrentPortions = new LiabilityAccount(-1, vRmap.getString("defaultAccounts.loansNonCurrentPortions.name"),
		vRmap.getString("defaultAccounts.loansNonCurrentPortions.description"),
		0.0, true);
	AccountTreeNode loansNonCurrentPortionsNode = new AccountTreeNode(loansNonCurrentPortions);
	LiabilityAccount mortgageLoan = new LiabilityAccount(-1, vRmap.getString("defaultAccounts.mortgageLoan.name"),
		vRmap.getString("defaultAccounts.mortgageLoan.description"),
		0.0, true);
	AccountTreeNode mortgageLoanNode = new AccountTreeNode(mortgageLoan);
	LiabilityAccount educationLoan = new LiabilityAccount(-1, vRmap.getString("defaultAccounts.educationLoan.name"),
		vRmap.getString("defaultAccounts.educationLoan.description"),
		0.0, true);
	AccountTreeNode educationLoanNode = new AccountTreeNode(educationLoan);
	LiabilityAccount vehicleLoan = new LiabilityAccount(-1, vRmap.getString("defaultAccounts.vehicleLoan.name"),
		vRmap.getString("defaultAccounts.vehicleLoan.description"),
		0.0, true);
	AccountTreeNode vehicleLoanNode = new AccountTreeNode(vehicleLoan);

	vCurrLiabilitiesNode.add(creditCardsNode);
	vCurrLiabilitiesNode.add(unpaidExpensesNode);
	vCurrLiabilitiesNode.add(loansCurrentPortionsNode);
	vCurrLiabilitiesNode.add(taxesPayableNode);

	liabilitiesNode.add(vCurrLiabilitiesNode);

	loansNonCurrentPortionsNode.add(mortgageLoanNode);
	loansNonCurrentPortionsNode.add(educationLoanNode);
	loansNonCurrentPortionsNode.add(vehicleLoanNode);

	nonCurrentLiabilitiesNode.add(loansNonCurrentPortionsNode);

	liabilitiesNode.add(nonCurrentLiabilitiesNode);
    }

    private void addNewDefaultEquityAccounts() {
	ResourceMap vRmap = JAccounting.getApplication().getContext().getResourceMap(this.getClass());

	EquityAccount vInitialCapital = new EquityAccount(-1, vRmap.getString("defaultAccounts.initialCapital.name"),
		vRmap.getString("defaultAccounts.initialCapital.description"),
		0.0, true);

	equityNode.add(new AccountTreeNode(vInitialCapital));
    }

    private void addNewDefaultRevenueAccounts() {
	ResourceMap vRmap = JAccounting.getApplication().getContext().getResourceMap(this.getClass());

	RevenueAccount earnedIncomes = new RevenueAccount(-1, vRmap.getString("defaultAccounts.earnedIncomes.name"),
		vRmap.getString("defaultAccounts.earnedIncomes.description"),
		0.0, true);
	AccountTreeNode earnedIncomesNode = new AccountTreeNode(earnedIncomes);
	RevenueAccount salary = new RevenueAccount(-1, vRmap.getString("defaultAccounts.salary.name"),
		vRmap.getString("defaultAccounts.salary.description"),
		0.0, true);
	AccountTreeNode salaryNode = new AccountTreeNode(salary);
	RevenueAccount bonuses = new RevenueAccount(-1, vRmap.getString("defaultAccounts.bonuses.name"),
		vRmap.getString("defaultAccounts.bonuses.description"),
		0.0, true);
	AccountTreeNode bonusesNode = new AccountTreeNode(bonuses);
	RevenueAccount commissions = new RevenueAccount(-1, vRmap.getString("defaultAccounts.commissions.name"),
		vRmap.getString("defaultAccounts.commissions.description"),
		0.0, true);
	AccountTreeNode commissionsNode = new AccountTreeNode(commissions);

	RevenueAccount portfolioIncomes = new RevenueAccount(-1, vRmap.getString("defaultAccounts.portfolioIncomes.name"),
		vRmap.getString("defaultAccounts.portfolioIncomes.description"),
		0.0, true);
	AccountTreeNode portfolioIncomesNode = new AccountTreeNode(portfolioIncomes);
	RevenueAccount interestIncomes = new RevenueAccount(-1, vRmap.getString("defaultAccounts.interestIncomes.name"),
		vRmap.getString("defaultAccounts.interestIncomes.description"),
		0.0, true);
	AccountTreeNode interestIncomesNode = new AccountTreeNode(interestIncomes);
	RevenueAccount checkingAccountInterest = new RevenueAccount(-1, vRmap.getString("defaultAccounts.checkingAccountInterest.name"),
		vRmap.getString("defaultAccounts.checkingAccountInterest.description"),
		0.0, true);
	AccountTreeNode checkingAccountInterestNode = new AccountTreeNode(checkingAccountInterest);
	RevenueAccount savingsAccountInterest = new RevenueAccount(-1, vRmap.getString("defaultAccounts.savingsAccountInterest.name"),
		vRmap.getString("defaultAccounts.savingsAccountInterest.description"),
		0.0, true);
	AccountTreeNode savingsAccountInterestNode = new AccountTreeNode(savingsAccountInterest);
	RevenueAccount bondsInterest = new RevenueAccount(-1, vRmap.getString("defaultAccounts.bondsInterest.name"),
		vRmap.getString("defaultAccounts.bondsInterest.description"),
		0.0, true);
	AccountTreeNode bondsInterestNode = new AccountTreeNode(bondsInterest);
	RevenueAccount bankCDInterest = new RevenueAccount(-1, vRmap.getString("defaultAccounts.bankCDInterest.name"),
		vRmap.getString("defaultAccounts.bankCDInterest.description"),
		0.0, true);
	AccountTreeNode bankCDInterestNode = new AccountTreeNode(bankCDInterest);
	RevenueAccount dividendIncomes = new RevenueAccount(-1, vRmap.getString("defaultAccounts.dividendIncomes.name"),
		vRmap.getString("defaultAccounts.dividendIncomes.description"),
		0.0, true);
	AccountTreeNode dividendIncomesNode = new AccountTreeNode(dividendIncomes);
	RevenueAccount gains = new RevenueAccount(-1, vRmap.getString("defaultAccounts.gains.name"),
		vRmap.getString("defaultAccounts.gains.description"),
		0.0, true);
	AccountTreeNode gainsNode = new AccountTreeNode(gains);

	RevenueAccount passiveIncomes = new RevenueAccount(-1, vRmap.getString("defaultAccounts.passiveIncomes.name"),
		vRmap.getString("defaultAccounts.passiveIncomes.description"),
		0.0, true);
	AccountTreeNode passiveIncomesNode = new AccountTreeNode(passiveIncomes);
	RevenueAccount rentalIncome = new RevenueAccount(-1, vRmap.getString("defaultAccounts.rentalIncome.name"),
		vRmap.getString("defaultAccounts.rentalIncome.description"),
		0.0, true);
	AccountTreeNode rentalIncomeNode = new AccountTreeNode(rentalIncome);
	RevenueAccount royaltiesIncome = new RevenueAccount(-1, vRmap.getString("defaultAccounts.royaltiesIncome.name"),
		vRmap.getString("defaultAccounts.royaltiesIncome.description"),
		0.0, true);
	AccountTreeNode royaltiesIncomeNode = new AccountTreeNode(royaltiesIncome);
	RevenueAccount giftsReceived = new RevenueAccount(-1, vRmap.getString("defaultAccounts.giftsReceived.name"),
		vRmap.getString("defaultAccounts.giftsReceived.description"),
		0.0, true);
	AccountTreeNode giftsReceivedNode = new AccountTreeNode(giftsReceived);

	earnedIncomesNode.add(salaryNode);
	earnedIncomesNode.add(bonusesNode);
	earnedIncomesNode.add(commissionsNode);

	revenuesNode.add(earnedIncomesNode);

	interestIncomesNode.add(checkingAccountInterestNode);
	interestIncomesNode.add(savingsAccountInterestNode);
	interestIncomesNode.add(bondsInterestNode);
	interestIncomesNode.add(bankCDInterestNode);

	portfolioIncomesNode.add(interestIncomesNode);
	portfolioIncomesNode.add(dividendIncomesNode);
	portfolioIncomesNode.add(gainsNode);

	revenuesNode.add(portfolioIncomesNode);

	passiveIncomesNode.add(rentalIncomeNode);
	passiveIncomesNode.add(royaltiesIncomeNode);
	passiveIncomesNode.add(giftsReceivedNode);

	revenuesNode.add(passiveIncomesNode);
    }

    private void addNewDefaultExpenseAccounts() {
	ResourceMap vRmap = JAccounting.getApplication().getContext().getResourceMap(this.getClass());

	ExpenseAccount adjustment = new ExpenseAccount(-1, vRmap.getString("defaultAccounts.adjustment.name"),
		vRmap.getString("defaultAccounts.adjustment.description"),
		0.0, true);
	AccountTreeNode adjustmentNode = new AccountTreeNode(adjustment);

	ExpenseAccount vehicleExpenses = new ExpenseAccount(-1, vRmap.getString("defaultAccounts.vehicleExpenses.name"),
		vRmap.getString("defaultAccounts.vehicleExpenses.description"),
		0.0, true);
	AccountTreeNode vehicleExpensesNode = new AccountTreeNode(vehicleExpenses);
	ExpenseAccount vehicleLease = new ExpenseAccount(-1, vRmap.getString("defaultAccounts.vehicleLease.name"),
		vRmap.getString("defaultAccounts.vehicleLease.description"),
		0.0, true);
	AccountTreeNode vehicleLeaseNode = new AccountTreeNode(vehicleLease);
	ExpenseAccount vehicleGas = new ExpenseAccount(-1, vRmap.getString("defaultAccounts.vehicleGas.name"),
		vRmap.getString("defaultAccounts.vehicleGas.description"),
		0.0, true);
	AccountTreeNode vehicleGasNode = new AccountTreeNode(vehicleGas);
	ExpenseAccount vehicleParking = new ExpenseAccount(-1, vRmap.getString("defaultAccounts.vehicleParking.name"),
		vRmap.getString("defaultAccounts.vehicleParking.description"),
		0.0, true);
	AccountTreeNode vehicleParkingNode = new AccountTreeNode(vehicleParking);
	ExpenseAccount vehicleRepairAndMaintenance = new ExpenseAccount(-1, vRmap.getString("defaultAccounts.vehicleRepairAndMaintenance.name"),
		vRmap.getString("defaultAccounts.vehicleRepairAndMaintenance.description"),
		0.0, true);
	AccountTreeNode vehicleRepairAndMaintenanceNode = new AccountTreeNode(vehicleRepairAndMaintenance);

	ExpenseAccount insuranceExpenses = new ExpenseAccount(-1, vRmap.getString("defaultAccounts.insuranceExpenses.name"),
		vRmap.getString("defaultAccounts.insuranceExpenses.description"),
		0.0, true);
	AccountTreeNode insuranceExpensesNode = new AccountTreeNode(insuranceExpenses);
	ExpenseAccount homeInsurance = new ExpenseAccount(-1, vRmap.getString("defaultAccounts.homeInsurance.name"),
		vRmap.getString("defaultAccounts.homeInsurance.description"),
		0.0, true);
	AccountTreeNode homeInsuranceNode = new AccountTreeNode(homeInsurance);
	ExpenseAccount autoInsurance = new ExpenseAccount(-1, vRmap.getString("defaultAccounts.autoInsurance.name"),
		vRmap.getString("defaultAccounts.autoInsurance.description"),
		0.0, true);
	AccountTreeNode autoInsuranceNode = new AccountTreeNode(autoInsurance);
	ExpenseAccount healthInsurance = new ExpenseAccount(-1, vRmap.getString("defaultAccounts.healthInsurance.name"),
		vRmap.getString("defaultAccounts.healthInsurance.description"),
		0.0, true);
	AccountTreeNode healthInsuranceNode = new AccountTreeNode(healthInsurance);
	ExpenseAccount lifeInsurance = new ExpenseAccount(-1, vRmap.getString("defaultAccounts.lifeInsurance.name"),
		vRmap.getString("defaultAccounts.lifeInsurance.description"),
		0.0, true);
	AccountTreeNode lifeInsuranceNode = new AccountTreeNode(lifeInsurance);

	ExpenseAccount serviceFees = new ExpenseAccount(-1, vRmap.getString("defaultAccounts.serviceFees.name"),
		vRmap.getString("defaultAccounts.serviceFees.description"),
		0.0, true);
	AccountTreeNode serviceFeesNode = new AccountTreeNode(serviceFees);
	ExpenseAccount bankServiceFees = new ExpenseAccount(-1, vRmap.getString("defaultAccounts.bankServiceFees.name"),
		vRmap.getString("defaultAccounts.bankServiceFees.description"),
		0.0, true);
	AccountTreeNode bankServiceFeesNode = new AccountTreeNode(bankServiceFees);
	ExpenseAccount legalServiceFees = new ExpenseAccount(-1, vRmap.getString("defaultAccounts.legalServiceFees.name"),
		vRmap.getString("defaultAccounts.legalServiceFees.description"),
		0.0, true);
	AccountTreeNode legalServiceFeesNode = new AccountTreeNode(legalServiceFees);
	ExpenseAccount brokerageFees = new ExpenseAccount(-1, vRmap.getString("defaultAccounts.brokerageFees.name"),
		vRmap.getString("defaultAccounts.brokerageFees.description"),
		0.0, true);
	AccountTreeNode brokerageFeesNode = new AccountTreeNode(brokerageFees);
	ExpenseAccount clothingCleaningFees = new ExpenseAccount(-1, vRmap.getString("defaultAccounts.clothingCleaningFees.name"),
		vRmap.getString("defaultAccounts.clothingCleaningFees.description"),
		0.0, true);
	AccountTreeNode clothingCleaningFeesNode = new AccountTreeNode(clothingCleaningFees);
	ExpenseAccount childCareFees = new ExpenseAccount(-1, vRmap.getString("defaultAccounts.childCareFees.name"),
		vRmap.getString("defaultAccounts.childCareFees.description"),
		0.0, true);
	AccountTreeNode childCareFeesNode = new AccountTreeNode(childCareFees);

	ExpenseAccount subscriptions = new ExpenseAccount(-1, vRmap.getString("defaultAccounts.subscriptions.name"),
		vRmap.getString("defaultAccounts.subscriptions.description"),
		0.0, true);
	AccountTreeNode subscriptionsNode = new AccountTreeNode(subscriptions);

	ExpenseAccount repairs = new ExpenseAccount(-1, vRmap.getString("defaultAccounts.repairs.name"),
		vRmap.getString("defaultAccounts.repairs.description"),
		0.0, true);
	AccountTreeNode repairsNode = new AccountTreeNode(repairs);
	ExpenseAccount homeRepairs = new ExpenseAccount(-1, vRmap.getString("defaultAccounts.homeRepairs.name"),
		vRmap.getString("defaultAccounts.homeRepairs.description"),
		0.0, true);
	AccountTreeNode homeRepairsNode = new AccountTreeNode(homeRepairs);
	ExpenseAccount equipmentRepairs = new ExpenseAccount(-1, vRmap.getString("defaultAccounts.equipmentRepairs.name"),
		vRmap.getString("defaultAccounts.equipmentRepairs.description"),
		0.0, true);
	AccountTreeNode equipmentRepairsNode = new AccountTreeNode(equipmentRepairs);

	ExpenseAccount taxes = new ExpenseAccount(-1, vRmap.getString("defaultAccounts.taxes.name"),
		vRmap.getString("defaultAccounts.taxes.description"),
		0.0, true);
	AccountTreeNode taxesNode = new AccountTreeNode(taxes);
	ExpenseAccount federalTaxes = new ExpenseAccount(-1, vRmap.getString("defaultAccounts.federalTaxes.name"),
		vRmap.getString("defaultAccounts.federalTaxes.description"),
		0.0, true);
	AccountTreeNode federalTaxesNode = new AccountTreeNode(federalTaxes);
	ExpenseAccount localTaxes = new ExpenseAccount(-1, vRmap.getString("defaultAccounts.localTaxes.name"),
		vRmap.getString("defaultAccounts.localTaxes.description"),
		0.0, true);
	AccountTreeNode localTaxesNode = new AccountTreeNode(localTaxes);
	ExpenseAccount propertyTax = new ExpenseAccount(-1, vRmap.getString("defaultAccounts.propertyTax.name"),
		vRmap.getString("defaultAccounts.propertyTax.description"),
		0.0, true);
	AccountTreeNode propertyTaxNode = new AccountTreeNode(propertyTax);

	ExpenseAccount utilities = new ExpenseAccount(-1, vRmap.getString("defaultAccounts.utilities.name"),
		vRmap.getString("defaultAccounts.utilities.description"),
		0.0, true);
	AccountTreeNode utilitiesNode = new AccountTreeNode(utilities);
	ExpenseAccount cableConsumption = new ExpenseAccount(-1, vRmap.getString("defaultAccounts.cableConsumption.name"),
		vRmap.getString("defaultAccounts.cableConsumption.description"),
		0.0, true);
	AccountTreeNode cableConsumptionNode = new AccountTreeNode(cableConsumption);
	ExpenseAccount satelliteConsumption = new ExpenseAccount(-1, vRmap.getString("defaultAccounts.satelliteConsumption.name"),
		vRmap.getString("defaultAccounts.satelliteConsumption.description"),
		0.0, true);
	AccountTreeNode satelliteConsumptionNode = new AccountTreeNode(satelliteConsumption);
	ExpenseAccount gasConsumption = new ExpenseAccount(-1, vRmap.getString("defaultAccounts.gasConsumption.name"),
		vRmap.getString("defaultAccounts.gasConsumption.description"),
		0.0, true);
	AccountTreeNode gasConsumptionNode = new AccountTreeNode(gasConsumption);
	ExpenseAccount electricityConsumption = new ExpenseAccount(-1, vRmap.getString("defaultAccounts.electricityConsumption.name"),
		vRmap.getString("defaultAccounts.electricityConsumption.description"),
		0.0, true);
	AccountTreeNode electricityConsumptionNode = new AccountTreeNode(electricityConsumption);
	ExpenseAccount waterConsumption = new ExpenseAccount(-1, vRmap.getString("defaultAccounts.waterConsumption.name"),
		vRmap.getString("defaultAccounts.waterConsumption.description"),
		0.0, true);
	AccountTreeNode waterConsumptionNode = new AccountTreeNode(waterConsumption);
	ExpenseAccount phoneConsumption = new ExpenseAccount(-1, vRmap.getString("defaultAccounts.phoneConsumption.name"),
		vRmap.getString("defaultAccounts.phoneConsumption.description"),
		0.0, true);
	AccountTreeNode phoneConsumptionNode = new AccountTreeNode(phoneConsumption);

	ExpenseAccount entertainment = new ExpenseAccount(-1, vRmap.getString("defaultAccounts.entertainment.name"),
		vRmap.getString("defaultAccounts.entertainment.description"),
		0.0, true);
	AccountTreeNode entertainmentNode = new AccountTreeNode(entertainment);
	ExpenseAccount travel = new ExpenseAccount(-1, vRmap.getString("defaultAccounts.travel.name"),
		vRmap.getString("defaultAccounts.travel.description"),
		0.0, true);
	AccountTreeNode travelNode = new AccountTreeNode(travel);
	ExpenseAccount meals = new ExpenseAccount(-1, vRmap.getString("defaultAccounts.meals.name"),
		vRmap.getString("defaultAccounts.meals.description"),
		0.0, true);
	AccountTreeNode mealsNode = new AccountTreeNode(meals);
	ExpenseAccount recreations = new ExpenseAccount(-1, vRmap.getString("defaultAccounts.recreations.name"),
		vRmap.getString("defaultAccounts.recreations.description"),
		0.0, true);
	AccountTreeNode recreationsNode = new AccountTreeNode(recreations);

	ExpenseAccount housingRent = new ExpenseAccount(-1, vRmap.getString("defaultAccounts.housingRent.name"),
		vRmap.getString("defaultAccounts.housingRent.description"),
		0.0, true);
	AccountTreeNode housingRentNode = new AccountTreeNode(housingRent);

	ExpenseAccount equipmentRents = new ExpenseAccount(-1, vRmap.getString("defaultAccounts.equipmentRents.name"),
		vRmap.getString("defaultAccounts.equipmentRents.description"),
		0.0, true);
	AccountTreeNode equipmentRentsNode = new AccountTreeNode(equipmentRents);

	ExpenseAccount depreciations = new ExpenseAccount(-1, vRmap.getString("defaultAccounts.depreciations.name"),
		vRmap.getString("defaultAccounts.depreciations.description"),
		0.0, true);
	AccountTreeNode depreciationsNode = new AccountTreeNode(depreciations);

	ExpenseAccount food = new ExpenseAccount(-1, vRmap.getString("defaultAccounts.food.name"),
		vRmap.getString("defaultAccounts.adjustment.description"),
		0.0, true);
	AccountTreeNode foodNode = new AccountTreeNode(food);

	ExpenseAccount gifts = new ExpenseAccount(-1, vRmap.getString("defaultAccounts.gifts.name"),
		vRmap.getString("defaultAccounts.gifts.description"),
		0.0, true);
	AccountTreeNode giftsNode = new AccountTreeNode(gifts);

	ExpenseAccount clothing = new ExpenseAccount(-1, vRmap.getString("defaultAccounts.clothing.name"),
		vRmap.getString("defaultAccounts.clothing.description"),
		0.0, true);
	AccountTreeNode clothingNode = new AccountTreeNode(clothing);

	ExpenseAccount education = new ExpenseAccount(-1, vRmap.getString("defaultAccounts.education.name"),
		vRmap.getString("defaultAccounts.education.description"),
		0.0, true);
	AccountTreeNode educationNode = new AccountTreeNode(education);

	ExpenseAccount groceries = new ExpenseAccount(-1, vRmap.getString("defaultAccounts.groceries.name"),
		vRmap.getString("defaultAccounts.groceries.description"),
		0.0, true);
	AccountTreeNode groceriesNode = new AccountTreeNode(groceries);

	ExpenseAccount hobbies = new ExpenseAccount(-1, vRmap.getString("defaultAccounts.hobbies.name"),
		vRmap.getString("defaultAccounts.hobbies.description"),
		0.0, true);
	AccountTreeNode hobbiesNode = new AccountTreeNode(hobbies);

	ExpenseAccount medicalExpenses = new ExpenseAccount(-1, vRmap.getString("defaultAccounts.medicalExpenses.name"),
		vRmap.getString("defaultAccounts.medicalExpenses.description"),
		0.0, true);
	AccountTreeNode medicalExpensesNode = new AccountTreeNode(medicalExpenses);

	ExpenseAccount transportation = new ExpenseAccount(-1, vRmap.getString("defaultAccounts.transportation.name"),
		vRmap.getString("defaultAccounts.transportation.description"),
		0.0, true);
	AccountTreeNode transportationNode = new AccountTreeNode(transportation);

	ExpenseAccount misc = new ExpenseAccount(-1, vRmap.getString("defaultAccounts.misc.name"),
		vRmap.getString("defaultAccounts.misc.description"),
		0.0, true);
	AccountTreeNode miscNode = new AccountTreeNode(misc);

	ExpenseAccount interestExpenses = new ExpenseAccount(-1, vRmap.getString("defaultAccounts.interestExpenses.name"),
		vRmap.getString("defaultAccounts.interestExpenses.description"),
		0.0, true);
	AccountTreeNode interestExpensesNode = new AccountTreeNode(interestExpenses);
	ExpenseAccount vehicleLoanInterest = new ExpenseAccount(-1, vRmap.getString("defaultAccounts.vehicleLoanInterest.name"),
		vRmap.getString("defaultAccounts.vehicleLoanInterest.description"),
		0.0, true);
	AccountTreeNode vehicleLoanInterestNode = new AccountTreeNode(vehicleLoanInterest);
	ExpenseAccount mortgageInterest = new ExpenseAccount(-1, vRmap.getString("defaultAccounts.mortgageInterest.name"),
		vRmap.getString("defaultAccounts.mortgageInterest.description"),
		0.0, true);
	AccountTreeNode mortgageInterestNode = new AccountTreeNode(mortgageInterest);
	ExpenseAccount educationLoanInterest = new ExpenseAccount(-1, vRmap.getString("defaultAccounts.educationLoanInterest.name"),
		vRmap.getString("defaultAccounts.educationLoanInterest.description"),
		0.0, true);
	AccountTreeNode educationLoanInterestNode = new AccountTreeNode(educationLoanInterest);
	ExpenseAccount creditCardInterest = new ExpenseAccount(-1, vRmap.getString("defaultAccounts.creditCardInterest.name"),
		vRmap.getString("defaultAccounts.creditCardInterest.description"),
		0.0, true);
	AccountTreeNode creditCardInterestNode = new AccountTreeNode(creditCardInterest);

	ExpenseAccount losses = new ExpenseAccount(-1, vRmap.getString("defaultAccounts.losses.name"),
		vRmap.getString("defaultAccounts.losses.description"),
		0.0, true);
	AccountTreeNode lossesNode = new AccountTreeNode(losses);


	expensesNode.add(adjustmentNode);

	vehicleExpensesNode.add(vehicleLeaseNode);
	vehicleExpensesNode.add(vehicleGasNode);
	vehicleExpensesNode.add(vehicleParkingNode);
	vehicleExpensesNode.add(vehicleRepairAndMaintenanceNode);

	expensesNode.add(vehicleExpensesNode);

	insuranceExpensesNode.add(homeInsuranceNode);
	insuranceExpensesNode.add(autoInsuranceNode);
	insuranceExpensesNode.add(healthInsuranceNode);
	insuranceExpensesNode.add(lifeInsuranceNode);

	expensesNode.add(insuranceExpensesNode);

	serviceFeesNode.add(bankServiceFeesNode);
	serviceFeesNode.add(legalServiceFeesNode);
	serviceFeesNode.add(brokerageFeesNode);
	serviceFeesNode.add(childCareFeesNode);
	serviceFeesNode.add(clothingCleaningFeesNode);

	expensesNode.add(serviceFeesNode);

	expensesNode.add(subscriptionsNode);

	repairsNode.add(homeRepairsNode);
	repairsNode.add(equipmentRepairsNode);
	
	expensesNode.add(repairsNode);

	taxesNode.add(federalTaxesNode);
	taxesNode.add(localTaxesNode);
	taxesNode.add(propertyTaxNode);

	expensesNode.add(taxesNode);

	utilitiesNode.add(cableConsumptionNode);
	utilitiesNode.add(satelliteConsumptionNode);
	utilitiesNode.add(gasConsumptionNode);
	utilitiesNode.add(electricityConsumptionNode);
	utilitiesNode.add(waterConsumptionNode);
	utilitiesNode.add(phoneConsumptionNode);

	expensesNode.add(utilitiesNode);

	entertainmentNode.add(travelNode);
	entertainmentNode.add(mealsNode);
	entertainmentNode.add(recreationsNode);

	expensesNode.add(entertainmentNode);
	expensesNode.add(housingRentNode);
	expensesNode.add(equipmentRentsNode);
	expensesNode.add(depreciationsNode);
	expensesNode.add(foodNode);
	expensesNode.add(giftsNode);
	expensesNode.add(hobbiesNode);
	expensesNode.add(groceriesNode);
	expensesNode.add(medicalExpensesNode);
	expensesNode.add(clothingNode);
	expensesNode.add(educationNode);
	expensesNode.add(transportationNode);
	expensesNode.add(miscNode);

	interestExpensesNode.add(mortgageInterestNode);
	interestExpensesNode.add(educationLoanInterestNode);
	interestExpensesNode.add(vehicleLoanInterestNode);
	interestExpensesNode.add(creditCardInterestNode);

	expensesNode.add(interestExpensesNode);

	expensesNode.add(lossesNode);
    }

}
