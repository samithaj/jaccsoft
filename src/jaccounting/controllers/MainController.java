/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jaccounting.controllers;

import jaccounting.JAccounting;
import jaccounting.ModelsMngr;
import java.io.File;
import jaccounting.models.Data;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdesktop.application.Action;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.Task;



/**
 *
 * @author bouba
 */
public class MainController extends BaseController implements Observer {
    private boolean saveToFileEnabled;
    private boolean closeTabEnabled;
    private MainController() {
	support = new PropertyChangeSupport(this);
	enableMainActions(false);
    }

    public void update(Observable o, Object arg) {
	if (o instanceof Data) {
	    enableSaveToFile(true);
	}
    }

    private static class MainControllerHolder {
        private static final MainController INSTANCE   = new MainController();
    }

    public static MainController getInstance() {
        return MainControllerHolder.INSTANCE;
    }

    public boolean isCloseTabEnabled() {
	return closeTabEnabled;
    }

    public boolean isSaveToFileEnabled() {
	return saveToFileEnabled;
    }

    protected void afterLoadOperations() {
	// open general ledger
	GeneralLedgerController.getInstance().openGeneralLedger();
	// open journal
	JournalController.getInstance().openJournal();
	// show general ledger
	GeneralLedgerController.getInstance().openGeneralLedger();
    }

    public void tabSelected(int pIndex) {
	// disallow closing
	enableCloseTab(false);
	
	if (pIndex == 0) {
	    // hide all tools bars but the account ledger one
	    JAccounting.getApplication().getView().getJournalToolsBar().setVisible(false);
	    JAccounting.getApplication().getView().getGeneralLedgerToolsBar().setVisible(true);
	} else if (pIndex == 1) {
	    // hide all but the journal tools bar
	    JAccounting.getApplication().getView().getGeneralLedgerToolsBar().setVisible(false);
	    JAccounting.getApplication().getView().getJournalToolsBar().setVisible(true);
	} else {
	    // hide all tools bars
	    JAccounting.getApplication().getView().getGeneralLedgerToolsBar().setVisible(false);
	    JAccounting.getApplication().getView().getJournalToolsBar().setVisible(false);
	    // allow closing operation
	    enableCloseTab(true);
	}
    }

    public void noTabSelected() {
	// disallow closing
	enableCloseTab(false);
	// hide all tools bars
	JAccounting.getApplication().getView().getGeneralLedgerToolsBar().setVisible(false);
	JAccounting.getApplication().getView().getJournalToolsBar().setVisible(false);
    }

    @Action
    public Task loadDefaultFile() {
	ResourceMap vRmap = JAccounting.getApplication().getContext().getResourceMap(this.getClass());
        return new LoadFileTask(vRmap.getString("defaultFileName"));
    }

    @Action(enabledProperty = "saveToFileEnabled")
    public Task saveToFile() {
        return new SaveToFileTask();
    }

    @Action(enabledProperty = "closeTabEnabled")
    public void closeTab() {
	JAccounting.getApplication().getView().closeCurrentTab();
    }

    private void enableMainActions(boolean b) {
	enableSaveToFile(b);
	enableCloseTab(b);
    }

    private void enableSaveToFile(boolean b) {
	saveToFileEnabled = b;
	support.firePropertyChange("saveToFileEnabled", !b, b);
    }

    private void enableCloseTab(boolean b) {
	closeTabEnabled = b;
	support.firePropertyChange("closeTabEnabled", !b, b);
    }


    protected class LoadFileTask extends Task<Void, Void> {;
        private String filename;

        public LoadFileTask(String pFilename) {
            super(JAccounting.getApplication());
	    filename = pFilename;
        }

        protected Void doInBackground() throws IOException, Exception {
	    ModelsMngr vModel = ModelsMngr.getInstance();
	    try {
		vModel.load(filename);
	    } catch (IOException vEx) {
		vModel.loadNew(filename);
		enableSaveToFile(true);
	    }
            return null;
        }

	@Override
        protected void succeeded(Void pvoid) {
            // reset view
	    JAccounting.getApplication().getView().newDataLoaded();
	    // observe data to enable or disable save
	    ModelsMngr.getInstance().getData().addObserver(MainController.this);
	    // launch default ops after a load
	    afterLoadOperations();
        }

        @Override
        protected void failed(Throwable cause) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, cause);
        }
    }

    protected class SaveToFileTask extends Task<Void, Void> {

        public SaveToFileTask() {
            super(JAccounting.getApplication());
        }

        protected Void doInBackground() throws IOException {
            ModelsMngr.getInstance().persit();
            return null;
        }

	@Override
	protected void succeeded(Void pVoid) {
	    // disable saving
	    enableSaveToFile(false);
	}

        @Override
        protected void failed(Throwable cause) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, cause);
        }

        /*@Override
        protected void interrupted(InterruptedException e) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, e);
        }*/

    }

}