/*
 * ModifyTransactionBox.java		1.0.0		09/2009
 * This file contains the transaction editor box class of the JAccounting application.
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
import jaccounting.ErrorCode;
import jaccounting.models.Transaction;
import java.util.Date;
import java.util.Map;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.DateFormatter;
import javax.swing.text.DefaultFormatterFactory;
import org.jdesktop.application.Action;
import org.jdesktop.application.ResourceMap;

/**
 * ModifyTransactionBox is the gui class for editing/adding transactions.
 *
 * @author	    Boubacar Diallo
 * @version	    1.0.0
 * @see		    jaccounting.controllers.JournalController
 * @see		    jaccounting.models.Transaction
 * @since	    1.0.0
 */
public class ModifyTransactionBox extends javax.swing.JDialog {

    private JournalController controller;

    private Transaction model;

    private boolean isNew;  // whether the transaction being edited is new or not


    /**
     * Sole constructor. This constructor initializes this view's controller and
     * model and creates its gui components.
     *
     * @param parent		    the parent frame of this dialog
     * @param controller	    the controller; a JournalController
     * @see			    jaccounting.controllers.JournalController
     * @see			    jaccounting.models.Transaction
     * @since			    1.0.0
     */
    public ModifyTransactionBox(java.awt.Frame parent, JournalController pController) {
        super(parent, true);
	controller = pController;
	
        initComponents();
	getRootPane().setDefaultButton(cancelButton);
	ResourceMap vRmap = JAccounting.getApplication().getContext().getResourceMap(this.getClass());
	vRmap.injectComponents(this);
    }


    public void setIsNew(boolean pVal) {
	isNew = pVal;
    }

    public boolean getIsNew() {
	return isNew;
    }

    public Transaction getModel() {
	return model;
    }

    public void setModel(Transaction model) {
	this.model = model;
    }

    public JTextField getAmountInput() {
	return amountInput;
    }

    public JComboBox getCreditAccountInput() {
	return creditAccountInput;
    }

    public JFormattedTextField getDateInput() {
	return dateInput;
    }

    public JComboBox getDebitAccountInput() {
	return debitAccountInput;
    }

    public JTextArea getMemoInput() {
	return memoInput;
    }

    public JTextField getRefNoInput() {
	return refNoInput;
    }

    /**
     * Intializes the inputs of this editor form to their values in the model.
     * This methods clears the error texts if any first.
     *
     * @since		    1.0.0
     */
    public void initFormFields() {
	clearErrors();
	dateInput.setValue(model.getDate());
	refNoInput.setText(model.getRefNo());
	memoInput.setText(model.getMemo());
	if (!isNew) {
	    creditAccountInput.setSelectedItem(controller.getAccountFullName(model.getDebitEntry().getTransferAccount()));
	    debitAccountInput.setSelectedItem(controller.getAccountFullName(model.getCreditEntry().getTransferAccount()));
	}
	amountInput.setText(model.getAmount()+"");
    }

    /**
     * Cancels the editing by disposing of this form dialog.
     *
     * @since			1.0.0
     */
    @Action
    public void cancel() {
	dispose();
    }

    private String[] buildAccountNamesList() {
	 return controller.getTransactionnableAccountFullNames();
    }

    /**
     * Clears the error texts from this form.
     *
     * @since			1.0.0
     */
    public void clearErrors() {
	dateErrorLabel.setText("");
	creditAccountErrorLabel.setText("");
	debitAccountErrorLabel.setText("");
	amountErrorLabel.setText("");
    }

    /**
     * Displays errors related to the input entered by the user.
     *
     * @param pErrors		map of error codes indexed by field name
     * @since			1.0.0
     */
    public void displayErrors(Map<String, ErrorCode> pErrors) {
	if (pErrors.containsKey("amount")) {
	    amountErrorLabel.setText(pErrors.get("amount").message());
	    amountErrorLabel.grabFocus();
	}

	if (pErrors.containsKey("debitAccount")) {
	    debitAccountErrorLabel.setText(pErrors.get("debitAccount").message());
	    debitAccountErrorLabel.grabFocus();
	}

	if (pErrors.containsKey("creditAccount")) {
	    creditAccountErrorLabel.setText(pErrors.get("creditAccount").message());
	    creditAccountErrorLabel.grabFocus();
	}

	if (pErrors.containsKey("date")) {
	    dateErrorLabel.setText(pErrors.get("date").message());
	    dateErrorLabel.grabFocus();
	}
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        boxInstructionsLabel = new javax.swing.JLabel();
        formPanel = new javax.swing.JPanel();
        dateLabel = new javax.swing.JLabel();
        refNoLabel = new javax.swing.JLabel();
        memoLabel = new javax.swing.JLabel();
        debitAccountLabel = new javax.swing.JLabel();
        refNoInput = new javax.swing.JTextField();
        amountInput = new javax.swing.JTextField();
        amountErrorLabel = new javax.swing.JLabel();
        dateErrorLabel = new javax.swing.JLabel();
        debitAccountErrorLabel = new javax.swing.JLabel();
        creditAccountLabel = new javax.swing.JLabel();
        amountLabel = new javax.swing.JLabel();
        memoScrollPane = new javax.swing.JScrollPane();
        memoInput = new javax.swing.JTextArea();
        dateInput = new javax.swing.JFormattedTextField();
        creditAccountErrorLabel = new javax.swing.JLabel();
        debitAccountInput = new javax.swing.JComboBox();
        creditAccountInput = new javax.swing.JComboBox();
        modifyButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setName("Form"); // NOI18N
        setResizable(false);

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(jaccounting.JAccounting.class).getContext().getResourceMap(ModifyTransactionBox.class);
        boxInstructionsLabel.setText(resourceMap.getString("boxInstructionsLabel.text")); // NOI18N
        boxInstructionsLabel.setName("boxInstructionsLabel"); // NOI18N

        formPanel.setName("formPanel"); // NOI18N
        formPanel.setPreferredSize(new java.awt.Dimension(380, 258));

        dateLabel.setText(resourceMap.getString("dateLabel.text")); // NOI18N
        dateLabel.setName("dateLabel"); // NOI18N

        refNoLabel.setText(resourceMap.getString("refNoLabel.text")); // NOI18N
        refNoLabel.setName("refNoLabel"); // NOI18N

        memoLabel.setText(resourceMap.getString("memoLabel.text")); // NOI18N
        memoLabel.setName("memoLabel"); // NOI18N

        debitAccountLabel.setText(resourceMap.getString("debitAccountLabel.text")); // NOI18N
        debitAccountLabel.setName("debitAccountLabel"); // NOI18N

        refNoInput.setName("refNoInput"); // NOI18N
        refNoInput.setPreferredSize(new java.awt.Dimension(100, 20));

        amountInput.setName("amountInput"); // NOI18N
        amountInput.setPreferredSize(new java.awt.Dimension(100, 20));

        amountErrorLabel.setForeground(resourceMap.getColor("amountErrorLabel.foreground")); // NOI18N
        amountErrorLabel.setName("amountErrorLabel"); // NOI18N
        amountErrorLabel.setPreferredSize(new java.awt.Dimension(0, 20));

        dateErrorLabel.setForeground(resourceMap.getColor("dateErrorLabel.foreground")); // NOI18N
        dateErrorLabel.setName("dateErrorLabel"); // NOI18N
        dateErrorLabel.setPreferredSize(new java.awt.Dimension(0, 20));

        debitAccountErrorLabel.setForeground(resourceMap.getColor("debitAccountErrorLabel.foreground")); // NOI18N
        debitAccountErrorLabel.setName("debitAccountErrorLabel"); // NOI18N
        debitAccountErrorLabel.setPreferredSize(new java.awt.Dimension(0, 20));

        creditAccountLabel.setText(resourceMap.getString("creditAccountLabel.text")); // NOI18N
        creditAccountLabel.setName("creditAccountLabel"); // NOI18N

        amountLabel.setText(resourceMap.getString("amountLabel.text")); // NOI18N
        amountLabel.setName("amountLabel"); // NOI18N

        memoScrollPane.setName("memoScrollPane"); // NOI18N

        memoInput.setColumns(20);
        memoInput.setRows(5);
        memoInput.setName("memoInput"); // NOI18N
        memoScrollPane.setViewportView(memoInput);

        dateInput.setText(resourceMap.getString("dateInput.text")); // NOI18N
        dateInput.setName("dateInput"); // NOI18N

        creditAccountErrorLabel.setForeground(resourceMap.getColor("creditAccountErrorLabel.foreground")); // NOI18N
        creditAccountErrorLabel.setName("creditAccountErrorLabel"); // NOI18N
        creditAccountErrorLabel.setPreferredSize(new java.awt.Dimension(0, 20));

        debitAccountInput.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        debitAccountInput.setName("debitAccountInput"); // NOI18N

        creditAccountInput.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        creditAccountInput.setName("creditAccountInput"); // NOI18N

        javax.swing.GroupLayout formPanelLayout = new javax.swing.GroupLayout(formPanel);
        formPanel.setLayout(formPanelLayout);
        formPanelLayout.setHorizontalGroup(
            formPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(formPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(formPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(refNoLabel)
                    .addComponent(memoLabel)
                    .addComponent(creditAccountLabel)
                    .addComponent(amountLabel)
                    .addComponent(debitAccountLabel)
                    .addComponent(dateLabel))
                .addGap(34, 34, 34)
                .addGroup(formPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(formPanelLayout.createSequentialGroup()
                        .addGroup(formPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(creditAccountInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(formPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(dateInput, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(refNoInput, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(debitAccountInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(amountInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(115, 115, 115)
                        .addGroup(formPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(creditAccountErrorLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 124, Short.MAX_VALUE)
                            .addComponent(dateErrorLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(debitAccountErrorLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 124, Short.MAX_VALUE)
                            .addComponent(amountErrorLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(memoScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        formPanelLayout.setVerticalGroup(
            formPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(formPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(formPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(formPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(dateLabel)
                        .addComponent(dateInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(dateErrorLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(formPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(refNoLabel)
                    .addComponent(refNoInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(formPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(memoLabel)
                    .addComponent(memoScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(formPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(formPanelLayout.createSequentialGroup()
                        .addGroup(formPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(formPanelLayout.createSequentialGroup()
                                .addComponent(debitAccountLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(creditAccountLabel))
                            .addGroup(formPanelLayout.createSequentialGroup()
                                .addComponent(debitAccountInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(creditAccountInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(formPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(amountLabel)
                            .addComponent(amountInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(formPanelLayout.createSequentialGroup()
                        .addComponent(debitAccountErrorLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(creditAccountErrorLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(amountErrorLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(58, 58, 58))
        );

        dateInput.setFormatterFactory(new DefaultFormatterFactory(new DateFormatter()));
        dateInput.setValue(new Date());
        debitAccountInput.setModel(new javax.swing.DefaultComboBoxModel(buildAccountNamesList()));
        creditAccountInput.setModel(new javax.swing.DefaultComboBoxModel(buildAccountNamesList()));

        modifyButton.setAction(JAccounting.getApplication().getContext().getActionMap(JournalController.class, JournalController.getInstance()).get("modifyTransaction"));
        modifyButton.setName("modifyButton"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(jaccounting.JAccounting.class).getContext().getActionMap(ModifyTransactionBox.class, this);
        cancelButton.setAction(actionMap.get("cancel")); // NOI18N
        cancelButton.setName("cancelButton"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(boxInstructionsLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 354, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(formPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 483, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(modifyButton)
                        .addGap(28, 28, 28)
                        .addComponent(cancelButton)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(boxInstructionsLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(formPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 295, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(modifyButton)
                    .addComponent(cancelButton))
                .addGap(13, 13, 13))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel amountErrorLabel;
    private javax.swing.JTextField amountInput;
    private javax.swing.JLabel amountLabel;
    private javax.swing.JLabel boxInstructionsLabel;
    private javax.swing.JButton cancelButton;
    private javax.swing.JLabel creditAccountErrorLabel;
    private javax.swing.JComboBox creditAccountInput;
    private javax.swing.JLabel creditAccountLabel;
    private javax.swing.JLabel dateErrorLabel;
    private javax.swing.JFormattedTextField dateInput;
    private javax.swing.JLabel dateLabel;
    private javax.swing.JLabel debitAccountErrorLabel;
    private javax.swing.JComboBox debitAccountInput;
    private javax.swing.JLabel debitAccountLabel;
    private javax.swing.JPanel formPanel;
    private javax.swing.JTextArea memoInput;
    private javax.swing.JLabel memoLabel;
    private javax.swing.JScrollPane memoScrollPane;
    private javax.swing.JButton modifyButton;
    private javax.swing.JTextField refNoInput;
    private javax.swing.JLabel refNoLabel;
    // End of variables declaration//GEN-END:variables

}
