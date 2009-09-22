/*
 * MainController.java		1.0.0		09/2009
 * This file contains the controller class of the JAccounting application responsible
 * for top level general actions.
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

package jaccounting.controllers;

import jaccounting.JAccounting;
import jaccounting.ModelsMngr;
import jaccounting.UnPersistenceFailureException;
import jaccounting.models.Data;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdesktop.application.Action;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.Task;



/**
 * MainController is the singleton controller class responsible for general actions.
 * Its instance manages the main frame's components not managed by other controllers.
 * It provides actions to load the default file from the system or with a new
 * application Data, save the loaded file to disk, close the active tab of the
 * content's tabbed pane. It observes the application's currently loaded Data
 * for changes in order to disable/enable its save action.
 *
 * @author	    Boubacar Diallo
 * @version	    1.0.0
 * @see		    jaccounting.MainView
 * @see		    jaccounting.ModelsMngr
 * @see		    jaccounting.models.Transaction
 * @since	    1.0.0
 */
public class MainController extends BaseController implements Observer {

    /** flag to enable/disable saveToFile action */
    private boolean saveToFileEnabled;

    /** flag to enable/disable the closeTab action */
    private boolean closeTabEnabled;

    
    private MainController() {
	super();
	enableMainActions(false);
    }

    private static class InstanceHolder {
        private static final MainController INSTANCE   = new MainController();
    }


    /**
     * Gets the unique instance of MainController.
     *
     * @return		the unique instance of MainController
     * @since		1.0.0
     */
    public static MainController getInstance() {
        return InstanceHolder.INSTANCE;
    }

    public boolean isCloseTabEnabled() {
	return closeTabEnabled;
    }

    public boolean isSaveToFileEnabled() {
	return saveToFileEnabled;
    }

    private void performAfterLoadOperations() {
	// observe data to enable or disable save
	JAccounting.getApplication().getModelsMngr().getData().addObserver(this);
	// open general ledger
	GeneralLedgerController.getInstance().openGeneralLedger();
	// open journal
	JournalController.getInstance().openJournal();
	// show general ledger
	GeneralLedgerController.getInstance().openGeneralLedger();
    }

    /**
     * Handles change notifications from the observed application Data object. This
     * method enables the save action given that the loaded Data has changed.
     *
     * @param o		    the object observed; application Data object expected
     * @param arg	    additional information about changes to the observed
     */
    public void update(Observable o, Object arg) {
	if (o instanceof Data) {
	    enableSaveToFile(true);
	}
    }

    /**
     * Handles bubbling of a tab selection event from the content's tabbed pane.
     * Depending on the selected tab, this method shows/hides the general ledger
     * tools bar or the journal tools bar and enables/disables the close tab action.
     *
     * @param pRow	    the row of the selected transaction in the Journal model
     * @since		    1.0.0
     */
    public void tabSelected(int pIndex) {
	// disallow closing
	enableCloseTab(false);
	
	if (pIndex == 0) {
	    // hide all tools bars but the account ledger one
	    JAccounting.getApplication().getMainView().getJournalToolsBar().setVisible(false);
	    JAccounting.getApplication().getMainView().getGeneralLedgerToolsBar().setVisible(true);
	} else if (pIndex == 1) {
	    // hide all but the journal tools bar
	    JAccounting.getApplication().getMainView().getGeneralLedgerToolsBar().setVisible(false);
	    JAccounting.getApplication().getMainView().getJournalToolsBar().setVisible(true);
	} else {
	    // hide all tools bars
	    JAccounting.getApplication().getMainView().getGeneralLedgerToolsBar().setVisible(false);
	    JAccounting.getApplication().getMainView().getJournalToolsBar().setVisible(false);
	    // allow closing operation
	    enableCloseTab(true);
	}
    }

    /**
     * Handles bubbling of a no tab selection event from the content's tabbed pane.
     * This method hides the general ledger tools bar, the journal tools bar and
     * disables the close tab action.
     *
     * @since		    1.0.0
     */
    public void noTabSelected() {
	// disallow closing
	enableCloseTab(false);
	// hide all tools bars
	JAccounting.getApplication().getMainView().getGeneralLedgerToolsBar().setVisible(false);
	JAccounting.getApplication().getMainView().getJournalToolsBar().setVisible(false);
    }

    /**
     * Loads data contained in the default application file. This action runs
     * a Task in a separate thread to do the job.
     *
     * @return		    the Task object executing the job
     * @since		    1.0.0
     */
    @Action
    public Task loadDefaultFile() {
	ResourceMap vRmap = JAccounting.getApplication().getContext().getResourceMap(this.getClass());
        return new LoadFileTask(vRmap.getString("defaultFileName"));
    }

    /**
     * Saves the application Data to the loaded file. This action runs
     * a Task in a separate thread to do the job.
     *
     * @return		    the Task object executing the job
     * @since		    1.0.0
     */
    @Action(enabledProperty = "saveToFileEnabled")
    public Task saveToFile() {
        return new SaveToFileTask();
    }

    /**
     * Closes the active tab of the content's tabbed pane. This methods delegates
     * the work to {@link jaccounting.MainView#closeCurrentTab()}.
     *
     * @see		    jaccounting.MainView#closeCurrentTab()
     * @since		    1.0.0
     */
    @Action(enabledProperty = "closeTabEnabled")
    public void closeTab() {
	JAccounting.getApplication().getMainView().closeCurrentTab();
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


    /**
     * LoadFileTask is the class for loading data file into the application.
     * A LoadFileTask object runs in the background to request loading of a
     * file from the ModelsMngr. If the file fails to be opened, a new fresh
     * application Data object is loaded instead.
     *
     * @version		    1.0.0
     * @see		    jaccounting.ModelsMngr
     * @since		    1.0.0
     */
    protected class LoadFileTask extends Task<Void, Void> {

	private String filename;    // name of the file to load
	

        LoadFileTask(String pFilename) {
            super(JAccounting.getApplication());
	    filename = pFilename;
        }


	/**
	 * Does the work of loading a file.
	 *
	 * @return				    Void
	 * @throws IOException			    if an I/O occured
	 * @throws UnPersistenceFailureException    if a parse of the file error occured
	 * @see					    jaccounting.ModelsMngr#load(java.lang.String)
	 * @see					    jaccounting.ModelsMngr#loadNew(java.lang.String) 
	 * @since				    1.0.0
	 */
        protected Void doInBackground() throws IOException, UnPersistenceFailureException {
	    ModelsMngr vModel = JAccounting.getApplication().getModelsMngr();
	    try {
		JAccounting.getApplication().getProgressReporter()
			    .reportUsingKey("messages.loadingFile");
		vModel.load(filename);
	    }
	    catch (IOException vEx) {
		JAccounting.getApplication().getProgressReporter()
			    .reportUsingKey("messages.loadingNewFile");
		vModel.loadNew(filename);
		enableSaveToFile(true);
	    }
	    finally {
		JAccounting.getApplication().getProgressReporter().reportFinished();
	    }
            return null;
        }

	/**
	 * Performs after success operations of this task. This method rests the
	 * main frame for the newly loaded Data and launches after load operations
	 * such as opening the general ledger interface and the journal interface.
	 *
	 * @param pvoid		    Void
	 * @see			    jaccounting.MainView#initForNewData()
	 * @since		    1.0.0
	 */
	@Override
        protected void succeeded(Void pVoid) {
            // reset view
	    JAccounting.getApplication().getMainView().initForNewData();
	    // launch default ops after a load
	    performAfterLoadOperations();
        }

	/**
	 * Performs after failure operations of this task. This method simply
	 * logs the cause of the failure.
	 * 
	 * @param cause		    the cause of the failure
	 * @since		    1.0.0
	 */
        @Override
        protected void failed(Throwable cause) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, "Failed to load file: " + filename, cause);
        }
    }

     /**
      * SaveToFileTask is the class for saving application Data to disk.
      * A SaveToFileTask object runs in the background to request saving of the
      * Data to the loaded file from the ModelsMngr.
      *
      * @version	    1.0.0
      * @see		    jaccounting.ModelsMngr
      * @since		    1.0.0
      */
    protected class SaveToFileTask extends Task<Void, Void> {

        SaveToFileTask() {
            super(JAccounting.getApplication());
        }


	/**
	 * Does the work of saving the loaded file to dsik.
	 *
	 * @return				    Void
	 * @throws IOException			    if an I/O occured
	 * @see					    jaccounting.ModelsMngr#persit()
	 * @since				    1.0.0
	 */
        protected Void doInBackground() throws IOException {
            ModelsMngr.getInstance().persit();
            return null;
        }

	/**
	 * Performs after success operations of this task. This method simply
	 * disables the save action.
	 *
	 * @param pvoid		    Void
	 * @since		    1.0.0
	 */
	@Override
	protected void succeeded(Void pVoid) {
	    // disable saving
	    enableSaveToFile(false);
	}

	/**
	 * Performs after failure operations of this task. This method simply
	 * logs the cause of the failure.
	 *
	 * @param cause		    the cause of the failure
	 * @since		    1.0.0
	 */
        @Override
        protected void failed(Throwable cause) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, "Failed to save to loaded file", cause);
        }

    }

}