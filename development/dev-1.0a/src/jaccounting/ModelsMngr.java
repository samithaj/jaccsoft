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

import jaccounting.exceptions.UnPersistenceFailureException;
import jaccounting.models.Data;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


/**
 * ModelsMngr is the singleton class representing the application's top level
 * domain. Its instance is responsible for managing the data aspect of the
 * application and persisting/unpersisting that data. The application's data
 * is encapsulated in a {@link jaccounting.models.Data Data} object and
 * the persistence is delegated to a {@link PersistenceHandler} object.
 *
 * @author	    Boubacar Diallo
 * @version	    1.0.0
 * @see		    PersistenceHandler
 * @see		    jaccounting.models.Data
 * @since	    1.0.0
 */
public class ModelsMngr {

    private PersistenceHandler persister;   // the data saver and loader

    private Data data;			    // the application's current data

    private String loadedFilename;	    // the name of the currently loaded file


    private ModelsMngr() {
	persister = new PersistenceHandler();
    }

    private static class InstanceHolder {
        private static final ModelsMngr INSTANCE = new ModelsMngr();
    }


    /**
     * Gets the ModelsMngr single instance. This methods guarantees a lazy
     * initialization of the single instance.
     *
     * @return		the ModelsMngr single instance
     * @since		1.0.0
     */
    public static ModelsMngr getInstance() {
        return InstanceHolder.INSTANCE;
    }

    public Data getData() {
        return data;
    }

    private Data generateDefaultData() {
	Data rData = new Data();
        rData.getGeneralLedger().addNewDefaultAccounts();
        return rData;
    }

    private void resetData() {
	data = generateDefaultData();
    }

    /**
     * Loads application data contained in a file. This method effectively delegats
     * the job to {@link PersistenceHandler#unpersist(java.io.InputStream) upersist) }.
     *
     * @param pFilename				the name of the file. It's relative to the
     *						application's local storage directory provided
     *						by the system.
     * @throws IOException			if an I/O error occured such as missing file
     * @throws UnPersistenceFailureException	if an exception related to unpersisting
     *						the application data contained in the
     *						file occured
     * @see					PersistenceHandler#unpersist(java.io.InputStream)
     * @since					1.0.0
     */
    public void load(String pFilename) throws IOException, UnPersistenceFailureException {
	InputStream vStream = JAccounting.getApplication().getContext()
				.getLocalStorage().openInputFile(pFilename);
	JAccounting.getApplication().getProgressReporter()
	    .reportUsingKey("messages.unpersistingFile");
	data = persister.unpersist(vStream);
	loadedFilename = pFilename;
	JAccounting.getApplication().getProgressReporter().reportFinished();
    }

    /**
     * Loads fresh application data with default values to be later saved in a file.
     *
     * @param pFilename		the name of the file to later save the data to
     * @since			1.0.0
     */
    public void loadNew(String pFilename) {
	resetData();
	loadedFilename = pFilename;
    }

    /**
     * Saves the application data to the currently loaded file. This method effectively
     * delegates the job to {@link PersistenceHandler#persist(jaccounting.models.Data,
     * java.io.OutputStream) persist}.
     *
     * @throws IOException	if an I/O error ocurred
     * @see			PersistenceHandler#persist(jaccounting.models.Data,
     *							    java.io.OutputStream)
     * @since			1.0.0
     */
    public void persit() throws IOException {
	OutputStream vStream = JAccounting.getApplication().getContext()
				.getLocalStorage().openOutputFile(loadedFilename);
	JAccounting.getApplication().getProgressReporter()
	    .reportUsingKey("messages.persistingFile");
	persister.persist(data, vStream);
	JAccounting.getApplication().getProgressReporter().reportFinished();
    }
    
}
