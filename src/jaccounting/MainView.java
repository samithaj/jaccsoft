/*
 * MainView.java
 */

package jaccounting;

import jaccounting.controllers.GeneralLedgerController;
import jaccounting.controllers.JournalController;
import jaccounting.controllers.MainController;
import jaccounting.models.GeneralLedger;
import jaccounting.views.ModifyAccountBox;
import jaccounting.views.ModifyTransactionBox;
import java.util.Observable;
import javax.swing.JToolBar;
import javax.swing.event.ChangeEvent;
import org.jdesktop.application.Action;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.TaskMonitor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observer;
import javax.swing.Timer;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeListener;

/**
 * The application's main frame.
 */
public class MainView extends FrameView implements ChangeListener, Observer {

    public MainView(SingleFrameApplication app) {
        super(app);

        initComponents();

        generalLedgerToolsBar.setVisible(false);
	tabsContainer.addChangeListener(this);
<<<<<<< .mine
	ResourceMap vRmap = JAccounting.getApplication().getContext().getResourceMap(this.getClass());
	vRmap.injectComponents(getComponent());
	
=======
	ResourceMap vRmap = JAccounting.getApplication().getContext().getResourceMap(this.getClass());
	vRmap.injectComponents(getComponent());

        // status bar initialization - message timeout, idle icon and busy animation, etc
>>>>>>> .r71
<<<<<<< .mine
	// status bar initialization - message timeout, idle icon and busy animation, etc
=======
        ResourceMap resourceMap = getResourceMap();
>>>>>>> .r71
        ResourceMap resourceMap = getResourceMap();
        int messageTimeout = resourceMap.getInteger("StatusBar.messageTimeout");
        messageTimer = new Timer(messageTimeout, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                statusMessageLabel.setText("");
            }
        });
        messageTimer.setRepeats(false);
        int busyAnimationRate = resourceMap.getInteger("StatusBar.busyAnimationRate");
        for (int i = 0; i < busyIcons.length; i++) {
            busyIcons[i] = resourceMap.getIcon("StatusBar.busyIcons[" + i + "]");
        }
        busyIconTimer = new Timer(busyAnimationRate, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                busyIconIndex = (busyIconIndex + 1) % busyIcons.length;
                statusAnimationLabel.setIcon(busyIcons[busyIconIndex]);
            }
        });
        idleIcon = resourceMap.getIcon("StatusBar.idleIcon");
        statusAnimationLabel.setIcon(idleIcon);
        progressBar.setVisible(false);

        // connecting action tasks to status bar via TaskMonitor
        TaskMonitor taskMonitor = new TaskMonitor(getApplication().getContext());
        taskMonitor.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                String propertyName = evt.getPropertyName();
                if ("started".equals(propertyName)) {
                    if (!busyIconTimer.isRunning()) {
                        statusAnimationLabel.setIcon(busyIcons[0]);
                        busyIconIndex = 0;
                        busyIconTimer.start();
                    }
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(true);
                } else if ("done".equals(propertyName)) {
                    busyIconTimer.stop();
                    statusAnimationLabel.setIcon(idleIcon);
                    progressBar.setVisible(false);
                    progressBar.setValue(0);
                } else if ("message".equals(propertyName)) {
                    String text = (String)(evt.getNewValue());
                    statusMessageLabel.setText((text == null) ? "" : text);
                    messageTimer.restart();
                } else if ("progress".equals(propertyName)) {
                    int value = (Integer)(evt.getNewValue());
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(false);
                    progressBar.setValue(value);
                }
            }
        });
    }

    @Action
    public void showAboutBox() {
        if (aboutBox == null) {
            JFrame mainFrame = JAccounting.getApplication().getMainFrame();
            aboutBox = new AboutBox(mainFrame);
            aboutBox.setLocationRelativeTo(mainFrame);
        }
        JAccounting.getApplication().show(aboutBox);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        stdToolsBar = new javax.swing.JToolBar();
        saveButton = new javax.swing.JButton();
        closeTabButton = new javax.swing.JButton();
        tabsContainer = new javax.swing.JTabbedPane();
        generalLedgerToolsBar = new javax.swing.JToolBar();
        newAccountButton = new javax.swing.JButton();
        editAccountButton = new javax.swing.JButton();
        deleteAccountButton = new javax.swing.JButton();
        openAccountButton = new javax.swing.JButton();
        journalToolsBar = new javax.swing.JToolBar();
        newTransactionButton = new javax.swing.JButton();
        editTransactionButton = new javax.swing.JButton();
        deleteTransactionButton = new javax.swing.JButton();
        menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu fileMenu = new javax.swing.JMenu();
        saveMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenuItem exitMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenu helpMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem aboutMenuItem = new javax.swing.JMenuItem();
        statusPanel = new javax.swing.JPanel();
        javax.swing.JSeparator statusPanelSeparator = new javax.swing.JSeparator();
        statusMessageLabel = new javax.swing.JLabel();
        statusAnimationLabel = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();

        mainPanel.setName("mainPanel"); // NOI18N

        stdToolsBar.setRollover(true);
        stdToolsBar.setName("stdToolsBar"); // NOI18N

        saveButton.setAction(JAccounting.getApplication().getContext().getActionMap(MainController.class, MainController.getInstance()).get("saveToFile"));
        saveButton.setFocusable(false);
        saveButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        saveButton.setName("saveButton"); // NOI18N
        saveButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        stdToolsBar.add(saveButton);

        closeTabButton.setAction(JAccounting.getApplication().getContext().getActionMap(MainController.class, MainController.getInstance()).get("closeTab"));
        closeTabButton.setFocusable(false);
        closeTabButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        closeTabButton.setName("closeTabButton"); // NOI18N
        closeTabButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        stdToolsBar.add(closeTabButton);

        tabsContainer.setName("tabsContainer"); // NOI18N

        generalLedgerToolsBar.setRollover(true);
        generalLedgerToolsBar.setName("generalLedgerToolsBar"); // NOI18N

        newAccountButton.setAction(JAccounting.getApplication().getContext().getActionMap(GeneralLedgerController.class, GeneralLedgerController.getInstance()).get("openNewAccountBox"));
        newAccountButton.setFocusable(false);
        newAccountButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        newAccountButton.setName("newAccountButton"); // NOI18N
        newAccountButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        generalLedgerToolsBar.add(newAccountButton);

        editAccountButton.setAction(JAccounting.getApplication().getContext().getActionMap(GeneralLedgerController.class, GeneralLedgerController.getInstance()).get("openEditAccountBox"));
        editAccountButton.setFocusable(false);
        editAccountButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        editAccountButton.setName("editAccountButton"); // NOI18N
        editAccountButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        generalLedgerToolsBar.add(editAccountButton);

        deleteAccountButton.setAction(JAccounting.getApplication().getContext().getActionMap(GeneralLedgerController.class, GeneralLedgerController.getInstance()).get("deleteAccount"));
        deleteAccountButton.setFocusable(false);
        deleteAccountButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        deleteAccountButton.setName("deleteAccountButton"); // NOI18N
        deleteAccountButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        generalLedgerToolsBar.add(deleteAccountButton);

        openAccountButton.setAction(JAccounting.getApplication().getContext().getActionMap(GeneralLedgerController.class, GeneralLedgerController.getInstance()).get("openAccountLedger"));
        openAccountButton.setFocusable(false);
        openAccountButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        openAccountButton.setName("openAccountButton"); // NOI18N
        openAccountButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        generalLedgerToolsBar.add(openAccountButton);

        journalToolsBar.setRollover(true);
        journalToolsBar.setName("journalToolsBar"); // NOI18N

        newTransactionButton.setAction(JAccounting.getApplication().getContext().getActionMap(JournalController.class, JournalController.getInstance()).get("openNewTransactionBox"));
        newTransactionButton.setFocusable(false);
        newTransactionButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        newTransactionButton.setName("newTransactionButton"); // NOI18N
        newTransactionButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        journalToolsBar.add(newTransactionButton);

        editTransactionButton.setAction(JAccounting.getApplication().getContext().getActionMap(JournalController.class, JournalController.getInstance()).get("openEditTransactionBox"));
        editTransactionButton.setFocusable(false);
        editTransactionButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        editTransactionButton.setName("editTransactionButton"); // NOI18N
        editTransactionButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        journalToolsBar.add(editTransactionButton);

        deleteTransactionButton.setAction(JAccounting.getApplication().getContext().getActionMap(JournalController.class, JournalController.getInstance()).get("deleteTransaction"));
        deleteTransactionButton.setFocusable(false);
        deleteTransactionButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        deleteTransactionButton.setName("deleteTransactionButton"); // NOI18N
        deleteTransactionButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        journalToolsBar.add(deleteTransactionButton);

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addComponent(stdToolsBar, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(generalLedgerToolsBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(journalToolsBar, javax.swing.GroupLayout.DEFAULT_SIZE, 257, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(tabsContainer, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)))
                .addContainerGap())
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(generalLedgerToolsBar, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(stdToolsBar, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(journalToolsBar, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tabsContainer, javax.swing.GroupLayout.DEFAULT_SIZE, 201, Short.MAX_VALUE)
                .addContainerGap())
        );

        menuBar.setName("menuBar"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(jaccounting.JAccounting.class).getContext().getResourceMap(MainView.class);
        fileMenu.setText(resourceMap.getString("fileMenu.text")); // NOI18N
        fileMenu.setName("fileMenu"); // NOI18N

        saveMenuItem.setAction(saveButton.getAction());
        saveMenuItem.setName("saveMenuItem"); // NOI18N
        fileMenu.add(saveMenuItem);

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(jaccounting.JAccounting.class).getContext().getActionMap(MainView.class, this);
        exitMenuItem.setAction(actionMap.get("quit")); // NOI18N
        exitMenuItem.setName("exitMenuItem"); // NOI18N
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        helpMenu.setText(resourceMap.getString("helpMenu.text")); // NOI18N
        helpMenu.setName("helpMenu"); // NOI18N

        aboutMenuItem.setAction(actionMap.get("showAboutBox")); // NOI18N
        aboutMenuItem.setName("aboutMenuItem"); // NOI18N
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        statusPanel.setName("statusPanel"); // NOI18N

        statusPanelSeparator.setName("statusPanelSeparator"); // NOI18N

        statusMessageLabel.setName("statusMessageLabel"); // NOI18N

        statusAnimationLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        statusAnimationLabel.setName("statusAnimationLabel"); // NOI18N

        progressBar.setName("progressBar"); // NOI18N

        javax.swing.GroupLayout statusPanelLayout = new javax.swing.GroupLayout(statusPanel);
        statusPanel.setLayout(statusPanelLayout);
        statusPanelLayout.setHorizontalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(statusPanelSeparator, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(statusMessageLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 230, Short.MAX_VALUE)
                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(statusAnimationLabel)
                .addContainerGap())
        );
        statusPanelLayout.setVerticalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addComponent(statusPanelSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(statusMessageLabel)
                    .addComponent(statusAnimationLabel)
                    .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(3, 3, 3))
        );

        setComponent(mainPanel);
        setMenuBar(menuBar);
        setStatusBar(statusPanel);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton closeTabButton;
    private javax.swing.JButton deleteAccountButton;
    private javax.swing.JButton deleteTransactionButton;
    private javax.swing.JButton editAccountButton;
    private javax.swing.JButton editTransactionButton;
    private javax.swing.JToolBar generalLedgerToolsBar;
    private javax.swing.JToolBar journalToolsBar;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JButton newAccountButton;
    private javax.swing.JButton newTransactionButton;
    private javax.swing.JButton openAccountButton;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JButton saveButton;
    private javax.swing.JMenuItem saveMenuItem;
    private javax.swing.JLabel statusAnimationLabel;
    private javax.swing.JLabel statusMessageLabel;
    private javax.swing.JPanel statusPanel;
    private javax.swing.JToolBar stdToolsBar;
    private javax.swing.JTabbedPane tabsContainer;
    // End of variables declaration//GEN-END:variables

<<<<<<< .mine
    private final Timer messageTimer;
    private final Timer busyIconTimer;
    private final Icon idleIcon;
    private final Icon[] busyIcons = new Icon[15];
    private int busyIconIndex = 0;

    private JDialog aboutBox;

    private ModifyAccountBox modifyAccountBox;

    private ModifyTransactionBox modifyTransactionBox;

    private JOptionPane confirmActionBox;

    private JOptionPane userMessageBox;

    public javax.swing.JTabbedPane getTabsContainer() {
        return tabsContainer;
=======
    private final Timer messageTimer;
    private final Timer busyIconTimer;
    private final Icon idleIcon;
    private final Icon[] busyIcons = new Icon[15];
    private int busyIconIndex = 0;

    private JDialog aboutBox;

    private ModifyAccountBox modifyAccountBox;

    private ModifyTransactionBox modifyTransactionBox;

    private JOptionPane confirmActionBox;

    private JOptionPane userMessageBox;

    public javax.swing.JTabbedPane getTabsContainer() {
        return tabsContainer;
    }

    public JToolBar getGeneralLedgerToolsBar() {
        return generalLedgerToolsBar;
    }

    public JToolBar getJournalToolsBar() {
	return journalToolsBar;
    }

    public ModifyAccountBox getModifyAccountBox() {
        return modifyAccountBox;
    }

    public void setModifyAccountBox(ModifyAccountBox modifyAccountBox) {
        this.modifyAccountBox = modifyAccountBox;
    }

    public ModifyTransactionBox getModifyTransactionBox() {
	return modifyTransactionBox;
    }

    public void setModifyTransactionBox(ModifyTransactionBox modifyTransactionBox) {
	this.modifyTransactionBox = modifyTransactionBox;
    }

    public boolean showConfirmActionBox(Object pMessage) {
	ResourceMap vRmap = JAccounting.getApplication().getContext().getResourceMap(this.getClass());
	String vTitle = vRmap.getString("confirmActionBox.title");
	initConfirmActionBox(pMessage);
	return showOptionDialog(confirmActionBox, vTitle) == JOptionPane.OK_OPTION ? true : false;
    }

    public void showUserMessageBox(Object pMessage, int pMessageType, int pOptionType, Object[] pOptions) {
	ResourceMap vRmap = JAccounting.getApplication().getContext().getResourceMap(this.getClass());
	String vTitle = vRmap.getString("userMessageBox.title");
	initUserMessageBox(pMessage);
	showOptionDialog(userMessageBox, vTitle);
    }

    private void initConfirmActionBox(Object pMessage) {
	confirmActionBox = new JOptionPane(pMessage, JOptionPane.QUESTION_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
    }

    private void initUserMessageBox(Object pMessage) {
	userMessageBox = new JOptionPane(pMessage, JOptionPane.INFORMATION_MESSAGE, JOptionPane.OK_OPTION);
    }

    private int showOptionDialog(JOptionPane pPane, String pTitle) {
	JDialog vDialog = pPane.createDialog(pTitle);
	vDialog.show();
	Object rVal = pPane.getValue();
	return (rVal == null) ? JOptionPane.CLOSED_OPTION :
	    (rVal instanceof Integer ? ((Integer)rVal).intValue() : JOptionPane.CLOSED_OPTION);
    }

    public void stateChanged(ChangeEvent e) {
	if (((JTabbedPane)e.getSource()).equals(tabsContainer)) {
	    tabsContainerStateChanged();
	}
    }

    private void tabsContainerStateChanged() {
	int vIndex = tabsContainer.getSelectedIndex();
	if (vIndex != -1) {
	    MainController.getInstance().tabSelected(vIndex);
	} else {
	    MainController.getInstance().noTabSelected();
	}
    }

    public void newDataLoaded() {
	tabsContainer.removeAll();
	JAccounting.getApplication().getModelsMngr().getData()
		    .getGeneralLedger().addObserver(this);
    }

    public void update(Observable o, Object arg) {
	if (o instanceof GeneralLedger) {
	    int vIndex = tabsContainer.getTabCount() -1;
	    // close tabs of deleted accounts
	    while (vIndex > 1) {
		if (((GeneralLedger) o).getAccount(tabsContainer.getToolTipTextAt(vIndex)) == null) {
		    tabsContainer.remove(vIndex);
		    vIndex--;
		}
		vIndex--;
	    }
	}
    }

    public void closeCurrentTab() {
	tabsContainer.remove(tabsContainer.getSelectedIndex());
    }

>>>>>>> .r71
}

    public JToolBar getGeneralLedgerToolsBar() {
        return generalLedgerToolsBar;
    }

    public JToolBar getJournalToolsBar() {
	return journalToolsBar;
    }

    public ModifyAccountBox getModifyAccountBox() {
        return modifyAccountBox;
    }

    public void setModifyAccountBox(ModifyAccountBox modifyAccountBox) {
        this.modifyAccountBox = modifyAccountBox;
    }

    public ModifyTransactionBox getModifyTransactionBox() {
	return modifyTransactionBox;
    }

    public void setModifyTransactionBox(ModifyTransactionBox modifyTransactionBox) {
	this.modifyTransactionBox = modifyTransactionBox;
    }

    public boolean showConfirmActionBox(Object pMessage) {
	ResourceMap vRmap = JAccounting.getApplication().getContext().getResourceMap(this.getClass());
	String vTitle = vRmap.getString("confirmActionBox.title");
	initConfirmActionBox(pMessage);
	return showOptionDialog(confirmActionBox, vTitle) == JOptionPane.OK_OPTION ? true : false;
    }

    public void showUserMessageBox(Object pMessage, int pMessageType, int pOptionType, Object[] pOptions) {
	ResourceMap vRmap = JAccounting.getApplication().getContext().getResourceMap(this.getClass());
	String vTitle = vRmap.getString("userMessageBox.title");
	initUserMessageBox(pMessage);
	showOptionDialog(userMessageBox, vTitle);
    }

    private void initConfirmActionBox(Object pMessage) {
	confirmActionBox = new JOptionPane(pMessage, JOptionPane.QUESTION_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
    }

    private void initUserMessageBox(Object pMessage) {
	userMessageBox = new JOptionPane(pMessage, JOptionPane.INFORMATION_MESSAGE, JOptionPane.OK_OPTION);
    }

    private int showOptionDialog(JOptionPane pPane, String pTitle) {
	JDialog vDialog = pPane.createDialog(pTitle);
	vDialog.show();
	Object rVal = pPane.getValue();
	return (rVal == null) ? JOptionPane.CLOSED_OPTION :
	    (rVal instanceof Integer ? ((Integer)rVal).intValue() : JOptionPane.CLOSED_OPTION);
    }

    public void stateChanged(ChangeEvent e) {
	if (((JTabbedPane)e.getSource()).equals(tabsContainer)) {
	    tabsContainerStateChanged();
	}
    }

    private void tabsContainerStateChanged() {
	int vIndex = tabsContainer.getSelectedIndex();
	if (vIndex != -1) {
	    MainController.getInstance().tabSelected(vIndex);
	} else {
	    MainController.getInstance().noTabSelected();
	}
    }

    public void newDataLoaded() {
	tabsContainer.removeAll();
	JAccounting.getApplication().getModelsMngr().getData()
		    .getGeneralLedger().addObserver(this);
    }

    public void update(Observable o, Object arg) {
	if (o instanceof GeneralLedger) {
	    int vIndex = tabsContainer.getTabCount() -1;
	    // close tabs of deleted accounts
	    while (vIndex > 1) {
		if (((GeneralLedger) o).getAccount(tabsContainer.getToolTipTextAt(vIndex)) == null) {
		    tabsContainer.remove(vIndex);
		    vIndex--;
		}
		vIndex--;
	    }
	}
    }

    public void closeCurrentTab() {
	tabsContainer.remove(tabsContainer.getSelectedIndex());
    }

}
