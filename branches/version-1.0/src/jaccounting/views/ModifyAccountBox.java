/*
 * ModifyAccountBox.java		1.0.0		09/2009
 * This file contains the account editor box class of the JAccounting application.
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
import jaccounting.ErrorCode;
import jaccounting.models.Account;
import java.util.Map;
import javax.swing.JTextField;
import org.jdesktop.application.Action;

/**
 * ModifyAccountBox is the gui class for editing/adding accounts.
 *
 * @author	    Boubacar Diallo
 * @version	    1.0.0
 * @see		    jaccounting.controllers.GeneralLedgerController
 * @see		    jaccounting.models.Account
 * @since	    1.0.0
 */
public class ModifyAccountBox extends javax.swing.JDialog {

    private GeneralLedgerController controller;

    private Account model;

    private boolean isNew;  // whether the account being edited is new or not


    /**
     * Sole constructor. This constructor initializes this view's controller and
     * model and creates its gui components.
     *
     * @param parent		    the parent frame of this dialog
     * @param controller	    the controller; a JournalController
     * @see			    jaccounting.controllers.JournalController
     * @see			    jaccounting.models.Account
     * @since			    1.0.0
     */
    public ModifyAccountBox(java.awt.Frame parent, GeneralLedgerController pController) {
        super(parent, true);
        controller = pController;
       
        initComponents();
        getRootPane().setDefaultButton(cancelButton);
	JAccounting.getApplication().getContext().getResourceMap(this.getClass()).injectComponents(this);
    }


    public void setModel(Account pModel) {
        this.model = pModel;
    }

    public void setIsNew(boolean pIsNew) {
	this.isNew = pIsNew;
    }

    public boolean getIsNew() {
	return isNew;
    }

    public Account getModel() {
	return model;
    }

    public JTextField getDescriptionTextField() {
	return descriptionTextField;
    }

    public JTextField getNameTextField() {
	return nameTextField;
    }

    public JTextField getNumberTextField() {
	return numberTextField;
    }

    /**
     * Intializes the inputs of this editor form to their values in the model.
     * This methods clears the error texts if any first.
     *
     * @since		    1.0.0
     */
    public void initFormFields() {
	clearErrors();
        nameTextField.setText(model.getName());
	numberTextField.setText((model.getNumber() > 0 ? model.getNumber()+"" : ""));
	descriptionTextField.setText(model.getDescription());
    }

    /**
     * Displays errors related to the input entered by the user.
     *
     * @param pErrors		map of errors indexed by field name
     * @since			1.0.0
     */
    public void displayErrors(Map<String, ErrorCode> pErrors) {
	if (pErrors.containsKey("number")) {
	    numberErrorLabel.setText(pErrors.get("number").message());
	    numberTextField.grabFocus();
	}

	if (pErrors.containsKey("name")) {
	    nameErrorLabel.setText(pErrors.get("name").message());
	    nameTextField.grabFocus();
	}
    }

    /**
     * Clears the error texts from this form.
     *
     * @since			1.0.0
     */
    public void clearErrors() {
	nameErrorLabel.setText("");
	numberErrorLabel.setText("");
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
        nameLabel = new javax.swing.JLabel();
        numberLabel = new javax.swing.JLabel();
        descriptionLabel = new javax.swing.JLabel();
        nameTextField = new javax.swing.JTextField();
        numberTextField = new javax.swing.JTextField();
        descriptionTextField = new javax.swing.JTextField();
        modifyButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        nameErrorLabel = new javax.swing.JLabel();
        numberErrorLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(jaccounting.JAccounting.class).getContext().getResourceMap(ModifyAccountBox.class);
        setTitle(resourceMap.getString("title")); // NOI18N
        setName("Form"); // NOI18N
        setResizable(false);

        boxInstructionsLabel.setText(resourceMap.getString("boxInstructionsLabel.text")); // NOI18N
        boxInstructionsLabel.setName("boxInstructionsLabel"); // NOI18N

        formPanel.setName("formPanel"); // NOI18N

        nameLabel.setText(resourceMap.getString("nameLabel.text")); // NOI18N
        nameLabel.setName("nameLabel"); // NOI18N

        numberLabel.setText(resourceMap.getString("numberLabel.text")); // NOI18N
        numberLabel.setName("numberLabel"); // NOI18N

        descriptionLabel.setText(resourceMap.getString("descriptionLabel.text")); // NOI18N
        descriptionLabel.setName("descriptionLabel"); // NOI18N

        nameTextField.setMinimumSize(new java.awt.Dimension(100, 20));
        nameTextField.setName("nameTextField"); // NOI18N
        nameTextField.setPreferredSize(new java.awt.Dimension(100, 20));

        numberTextField.setName("numberTextField"); // NOI18N
        numberTextField.setPreferredSize(new java.awt.Dimension(100, 20));

        descriptionTextField.setName("descriptionTextField"); // NOI18N
        descriptionTextField.setPreferredSize(new java.awt.Dimension(100, 20));

        modifyButton.setAction(JAccounting.getApplication().getContext().getActionMap(GeneralLedgerController.class, GeneralLedgerController.getInstance()).get("updateAccount"));
        modifyButton.setName("modifyButton"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(jaccounting.JAccounting.class).getContext().getActionMap(ModifyAccountBox.class, this);
        cancelButton.setAction(actionMap.get("cancel")); // NOI18N
        cancelButton.setName("cancelButton"); // NOI18N

        nameErrorLabel.setForeground(resourceMap.getColor("nameErrorLabel.foreground")); // NOI18N
        nameErrorLabel.setName("nameErrorLabel"); // NOI18N
        nameErrorLabel.setPreferredSize(new java.awt.Dimension(0, 20));

        numberErrorLabel.setText(resourceMap.getString("numberErrorLabel.text")); // NOI18N
        numberErrorLabel.setName("numberErrorLabel"); // NOI18N
        numberErrorLabel.setPreferredSize(new java.awt.Dimension(0, 20));

        javax.swing.GroupLayout formPanelLayout = new javax.swing.GroupLayout(formPanel);
        formPanel.setLayout(formPanelLayout);
        formPanelLayout.setHorizontalGroup(
            formPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, formPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(formPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(formPanelLayout.createSequentialGroup()
                        .addGroup(formPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(descriptionLabel)
                            .addComponent(numberLabel)
                            .addComponent(nameLabel))
                        .addGap(54, 54, 54)
                        .addGroup(formPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(descriptionTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(nameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(formPanelLayout.createSequentialGroup()
                                .addComponent(numberTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(formPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(numberErrorLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 225, Short.MAX_VALUE)
                                    .addComponent(nameErrorLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 225, Short.MAX_VALUE)))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, formPanelLayout.createSequentialGroup()
                        .addComponent(modifyButton)
                        .addGap(18, 18, 18)
                        .addComponent(cancelButton)))
                .addContainerGap())
        );
        formPanelLayout.setVerticalGroup(
            formPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(formPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(formPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(formPanelLayout.createSequentialGroup()
                        .addGroup(formPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(nameLabel)
                            .addComponent(nameErrorLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(nameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(formPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(numberLabel)
                            .addComponent(numberTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(numberErrorLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addGroup(formPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(descriptionLabel)
                    .addComponent(descriptionTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 96, Short.MAX_VALUE)
                .addGroup(formPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelButton)
                    .addComponent(modifyButton))
                .addGap(31, 31, 31))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(formPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(boxInstructionsLabel))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(boxInstructionsLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(formPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel boxInstructionsLabel;
    private javax.swing.JButton cancelButton;
    private javax.swing.JLabel descriptionLabel;
    private javax.swing.JTextField descriptionTextField;
    private javax.swing.JPanel formPanel;
    private javax.swing.JButton modifyButton;
    private javax.swing.JLabel nameErrorLabel;
    private javax.swing.JLabel nameLabel;
    private javax.swing.JTextField nameTextField;
    private javax.swing.JLabel numberErrorLabel;
    private javax.swing.JLabel numberLabel;
    private javax.swing.JTextField numberTextField;
    // End of variables declaration//GEN-END:variables

}